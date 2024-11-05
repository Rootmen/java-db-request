package ru.iedt.database.request.controller.utils;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.groups.UniJoin;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.PreparedQuery;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import ru.iedt.database.request.controller.parameter.ParameterInput;
import ru.iedt.database.request.structures.nodes.v3.Elements;
import ru.iedt.database.request.structures.nodes.v3.node.SQL;

public class DatabaseUtils {

    @Deprecated
    public static Map<String, Elements.Parameter<?>> createParametersInputs(
            Map<String, ParameterInput> parameterInputs, Map<String, Elements.Parameter<?>> parameterMap) {
        for (Map.Entry<String, Elements.Parameter<?>> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            Elements.Parameter<?> parameters = entry.getValue();
            ParameterInput parameterInput = parameterInputs.get(key);
            if (parameterInput != null) parameters.setValue(parameterInput.getValue());
        }
        return parameterMap;
    }

    public static Map<String, Elements.Parameter<?>> createParameters(
            ArrayList<ParameterInput> parameterInputs, Map<String, Elements.Parameter<?>> parameterMap) {
        for (ParameterInput parameter : parameterInputs) {
            if (parameter.getName() != null) {
                parameterMap.get(parameter.getName()).setValue(parameter.getValue());
            }
        }
        return parameterMap;
    }

    @SuppressWarnings("unchecked")
    public static Uni<Map<String, RowSet<Row>>> runQueries(
            Elements.Queries queries,
            Map<String, Elements.Parameter<?>> parameters,
            Map<String, Elements.Template> template,
            PgPool client,
            String storeName,
            String queryName) {
        List<Elements.SQL> sqlList = queries.getSql();
        List<SQL.InsertData> insertDataArray = new ArrayList<>();
        sqlList.forEach(sql -> insertDataArray.add(SQL.getInsertData(sql, parameters, template)));
        if (insertDataArray.size() == 1) {
            return client.getConnection().flatMap(connection -> {
                UniJoin.Builder<RowSet<Row>> builder = Uni.join().builder();
                SQL.InsertData insertData = insertDataArray.getFirst();
                List<String> unisKey = new ArrayList<>();
                Tuple tuple = Tuple.tuple();
                for (String token : insertData.getParametersTokens()) {
                    Elements.Parameter<?> parameter = parameters.get(token);
                    parameter.addToTuple(tuple);
                }
                PreparedQuery<RowSet<Row>> preparedQuery = connection.preparedQuery(insertData.getSql());
                builder.add(preparedQuery.execute(tuple).onFailure().invoke(throwable -> {
                    System.err.println("Хранилище : " + storeName);
                    System.err.println("Запрос : " + queryName);
                    System.err.println("Параметры : " + parameters);
                    System.err.println("Текст запроса : " + insertData.getSql());
                    throwable.printStackTrace();
                }));
                unisKey.add(insertData.getName());
                return builder.joinAll()
                        .andCollectFailures()
                        .onItem()
                        .transform(rowSets -> {
                            Map<String, RowSet<Row>> map = new LinkedHashMap<>();
                            for (int g = 0; g < rowSets.size(); g++) {
                                map.put(unisKey.get(g), rowSets.get(g));
                            }
                            return map;
                        })
                        .onFailure()
                        .call(connection::close)
                        .flatMap(stringRowSetMap -> connection.close().replaceWith(stringRowSetMap));
            });
        }

        return client.getConnection()
                .onItem()
                .transformToUni(connection -> connection.begin().onItem().transformToUni(transaction -> {
                    UniJoin.Builder<RowSet<Row>> builder = Uni.join().builder();
                    List<String> unisKey = new ArrayList<>();
                    for (SQL.InsertData insertData : insertDataArray) {
                        Tuple tuple = Tuple.tuple();
                        for (String token : insertData.getParametersTokens()) {
                            Elements.Parameter<?> parameter = parameters.get(token);
                            parameter.addToTuple(tuple);
                        }
                        PreparedQuery<RowSet<Row>> preparedQuery = connection.preparedQuery(insertData.getSql());
                        builder.add(preparedQuery.execute(tuple).onFailure().invoke(throwable -> {
                            System.err.println("Хранилище : " + storeName);
                            System.err.println("Запрос : " + queryName);
                            System.err.println("Параметры : " + parameters);
                            System.err.println("Текст запроса : " + insertData.getSql());
                            throwable.printStackTrace();
                        }));
                        unisKey.add(insertData.getName());
                    }
                    return builder.joinAll()
                            .andCollectFailures()
                            .onItem()
                            .transform(rowSets -> {
                                Map<String, RowSet<Row>> map = new LinkedHashMap<>();
                                for (int g = 0; g < rowSets.size(); g++) {
                                    map.put(unisKey.get(g), rowSets.get(g));
                                }
                                return map;
                            })
                            .onItem()
                            .transformToUni(stringRowSetMap -> transaction
                                    .commit()
                                    .onFailure()
                                    .invoke(() -> System.out.println("Ошибка в транзакции. Отмена дейстаий"))
                                    .onFailure()
                                    .recoverWithUni(
                                            throwable -> transaction.rollback().flatMap(unused -> connection.close()))
                                    .onItem()
                                    .transformToUni(unused -> connection.close())
                                    .replaceWith(stringRowSetMap));
                }));
    }
}
