package ru.iedt.database.request.controller;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.PreparedQuery;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.inject.Singleton;
import ru.iedt.database.request.controller.parameter.ParameterInput;
import ru.iedt.database.request.parser.elements.v3.ParserEngine;
import ru.iedt.database.request.store.QueryStoreDefinition;
import ru.iedt.database.request.store.QueryStoreList;
import ru.iedt.database.request.structures.nodes.v3.Elements;
import ru.iedt.database.request.structures.nodes.v3.node.SQL;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class DatabaseController {
    private final Map<String, Elements.Definition> QUERY_STORE_DEFINITION_MAP = new HashMap<>();

    DatabaseController() {
        ArrayList<QueryStoreDefinition> queryStoreDefinitionArrayList = QueryStoreList.getStoresMetadata();
        for (QueryStoreDefinition queryStoreDefinition : queryStoreDefinitionArrayList) {
            try {
                String storeName = queryStoreDefinition.getStoreName();
                if (QUERY_STORE_DEFINITION_MAP.containsKey(storeName)) {
                    throw new RuntimeException("Несколько хранилищ с одинаковым названием!");
                }
                URI file = queryStoreDefinition.getStorePath();
                if (!Files.exists(Paths.get(file))) {
                    throw new RuntimeException(String.format(
                            "Nor found file %s",
                            queryStoreDefinition.getStorePath().getPath()));
                }
                Elements.Definition definition = ParserEngine.parsingXml(file);
                QUERY_STORE_DEFINITION_MAP.put(storeName, definition);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<Uni<Map<String, Uni<RowSet<Row>>>>> runningQuerySet(
            String storeName, String queryName, HashMap<String, ParameterInput> parameterInputs, PgPool client) {
        Elements.Definition definition = QUERY_STORE_DEFINITION_MAP.get(storeName);
        if (definition == null) throw new RuntimeException("Хранилище Definition не найдено");
        Elements.QuerySet querySet = definition.getQuerySet().get(queryName);
        Map<String, Elements.Parameter<?>> parameters = createParameters(parameterInputs, querySet.getParameters());
        List<Elements.Queries> queries = querySet.getQueries();
        List<Uni<Map<String, Uni<RowSet<Row>>>>> result = new ArrayList<>();
        for (Elements.Queries query : queries) {
            result.add(runQueries(query, parameters, client));
        }
        return result;
    }

    private Map<String, Elements.Parameter<?>> createParameters(
            Map<String, ParameterInput> parameterInputs, Map<String, Elements.Parameter<?>> parameterMap) {
        for (Map.Entry<String, Elements.Parameter<?>> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            Elements.Parameter<?> parameters = entry.getValue();
            ParameterInput parameterInput = parameterInputs.get(key);
            if (parameterInput != null) parameters.setValue(parameterInput.getValue());
        }
        return parameterMap;
    }

    private Uni<Map<String, Uni<RowSet<Row>>>> runQueries(
            Elements.Queries queries, Map<String, Elements.Parameter<?>> parameters, PgPool client) {
        List<Elements.SQL> sqlList = queries.getSql();
        List<SQL.InsertData> insertDataArray = new ArrayList<>();
        for (Elements.SQL sql : sqlList) {
            insertDataArray.add(SQL.getInsertData(sql, parameters));
        }
        return client.withTransaction(connection -> {
            Map<String, Uni<RowSet<Row>>> unis = new HashMap<>();
            for (SQL.InsertData insertData : insertDataArray) {
                Tuple tuple = Tuple.tuple();
                for (String token : insertData.getParametersTokens()) {
                    Elements.Parameter<?> parameter = parameters.get(token);
                    parameter.addToTuple(tuple);
                }
                PreparedQuery<RowSet<Row>> preparedQuery = connection.preparedQuery(insertData.getSql());
                Uni<RowSet<Row>> query = preparedQuery.execute(tuple);
                unis.put(insertData.getName(), query);
            }
            return Uni.createFrom().item(unis);
        });
    }

    public Map<String, Elements.Definition> getDefinitions() {
        return QUERY_STORE_DEFINITION_MAP;
    }
}
