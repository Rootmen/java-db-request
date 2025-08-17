package ru.iedt.database.request.controller.v2.utils;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.mutiny.sqlclient.*;
import io.vertx.mutiny.sqlclient.Pool;
import java.util.*;
import java.util.function.Function;
import ru.iedt.database.request.controller.parameter.ParameterInput;
import ru.iedt.database.request.structures.nodes.v3.Elements;
import ru.iedt.database.request.structures.nodes.v3.node.SQL;

public class DatabaseUtils {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseUtils.class);

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
            List<ParameterInput> parameterInputs, Map<String, Elements.Parameter<?>> parameterMap) {
        for (ParameterInput parameter : parameterInputs) {
            if (parameter.getName() != null) {
                parameterMap.get(parameter.getName()).setValue(parameter.getValue());
            }
        }
        return parameterMap;
    }

    public static <T> Multi<T> runQueries(
            Elements.Queries queries,
            Map<String, Elements.Parameter<?>> parameters,
            Map<String, Elements.Template> templates,
            String resultQueryName,
            Function<RowSet<Row>, Multi<T>> resultMapper,
            Pool client,
            String storeName,
            String queryName) {

        List<SQL.InsertData> queriesToExecute = queries.getSql().stream()
                .map(sql -> SQL.getInsertData(sql, parameters, templates))
                .toList();

        // Валидация того что у нас есть запрос помеченный как с данными
        if (queriesToExecute.stream().noneMatch(q -> q.getName().equals(resultQueryName))) {
            String errorMsg =
                    String.format("[%s:%s] Target query not found: %s", storeName, queryName, resultQueryName);
            IllegalArgumentException ex = new IllegalArgumentException(errorMsg);
            LOG.error(errorMsg, ex);
            return Multi.createFrom().failure(ex);
        }

        return executeQuery(queriesToExecute, parameters, resultQueryName, resultMapper, client, storeName, queryName);
    }

    private static <T> Multi<T> executeQuery(
            List<SQL.InsertData> queriesToExecute,
            Map<String, Elements.Parameter<?>> parameters,
            String resultQueryName,
            Function<RowSet<Row>, Multi<T>> resultMapper,
            Pool client,
            String storeName,
            String queryName) {
        // Оптимизация одиночного запроса
        if (queriesToExecute.size() == 1) {
            SQL.InsertData singleQuery = queriesToExecute.getFirst();
            return executeSingleQueryWithoutTransaction(
                    singleQuery, parameters, resultQueryName, resultMapper, client, storeName, queryName);
        }

        // Вызов запроса в режиме транзакции когда у нас несколько запросов подряд

        return executeSingleQueryInTransaction(
                queriesToExecute, parameters, resultQueryName, resultMapper, client, storeName, queryName);
    }

    /// Оптимизированный метод для запроса в транзакции
    private static <T> Multi<T> executeSingleQueryInTransaction(
            List<SQL.InsertData> queriesToExecute,
            Map<String, Elements.Parameter<?>> parameters,
            String resultQueryName,
            Function<RowSet<Row>, Multi<T>> resultMapper,
            Pool client,
            String storeName,
            String queryName) {
        return Multi.createFrom().emitter(emitter -> {
            client.withTransaction(transaction -> {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(String.format(
                                    "[%s:%s] Transaction started (%d queries)",
                                    storeName, queryName, queriesToExecute.size()));
                        }
                        Uni<Void> chain = Uni.createFrom().voidItem();

                        for (SQL.InsertData query : queriesToExecute) {
                            chain = chain.chain(() -> {
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug(String.format(
                                            "[%s:%s] Executing query: %s", storeName, queryName, query.getName()));
                                }
                                Tuple tuple = prepareTuple(query, parameters);
                                if (LOG.isTraceEnabled()) {
                                    LOG.trace(String.format(
                                            "[%s:%s] Parameters: %s", storeName, queryName, tuple.deepToString()));
                                    LOG.trace(String.format("[%s:%s] SQL: %s", storeName, queryName, query.getSql()));
                                }
                                Uni<RowSet<Row>> queryUni = transaction
                                        .preparedQuery(query.getSql())
                                        .execute(tuple)
                                        .onFailure()
                                        .invoke(t -> {
                                            String errorMsg = String.format(
                                                    "[%s:%s] Query execution error '%s'. SQL: %s Parameters: %s",
                                                    storeName,
                                                    queryName,
                                                    query.getName(),
                                                    query.getSql(),
                                                    buildParamsLog(query, tuple));
                                            LOG.error(errorMsg, t);
                                        });

                                // Возврат результата в запрпос помеченный как с данными
                                if (query.getName().equals(resultQueryName)) {
                                    return queryUni.onItem()
                                            .transformToMulti(resultMapper)
                                            .onItem()
                                            .invoke(item -> {
                                                if (LOG.isTraceEnabled()) {
                                                    LOG.trace(String.format(
                                                            "[%s:%s] Result item: %s", storeName, queryName, item));
                                                }
                                                emitter.emit(item);
                                            })
                                            .collect()
                                            .last()
                                            .invoke(ts -> emitter.complete())
                                            .replaceWithVoid();
                                }

                                return queryUni.replaceWithVoid();
                            });
                        }

                        return chain.onFailure().invoke(t -> {
                            transaction.close().subscribe().with(unused -> {});
                            String errorMsg = String.format("[%s:%s] Transaction error", storeName, queryName);
                            LOG.error(errorMsg, t);
                        });
                    })
                    .invoke(() -> {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(String.format("[%s:%s] Transaction completed", storeName, queryName));
                        }
                    })
                    .subscribe()
                    .with(unused -> {}, failure -> {
                        String errorMsg = String.format(
                                "[%s:%s] Operation error: %s", storeName, queryName, failure.getMessage());
                        LOG.error(errorMsg, failure);
                        emitter.fail(failure);
                    });
        });
    }

    private static <T> Multi<T> executeSingleQueryWithoutTransaction(
            SQL.InsertData query,
            Map<String, Elements.Parameter<?>> parameters,
            String resultQueryName,
            Function<RowSet<Row>, Multi<T>> resultMapper,
            Pool client,
            String storeName,
            String queryName) {

        if (!query.getName().equals(resultQueryName)) {
            String errorMsg =
                    String.format("[%s:%s] Query is not the target: %s", storeName, queryName, query.getName());
            LOG.warn(errorMsg);
            return Multi.createFrom().failure(new IllegalArgumentException(errorMsg));
        }

        Tuple tuple = prepareTuple(query, parameters);

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format(
                    "[%s:%s] Executing single query (no transaction): %s", storeName, queryName, query.getName()));
        }

        if (LOG.isTraceEnabled()) {
            LOG.trace(String.format("[%s:%s] Parameters: %s", storeName, queryName, tuple.deepToString()));
            LOG.trace(String.format("[%s:%s] SQL: %s", storeName, queryName, query.getSql()));
        }

        return client.preparedQuery(query.getSql())
                .execute(tuple)
                .onItem()
                .transformToMulti(resultMapper)
                .onFailure()
                .invoke(failure -> {
                    String errorMsg = String.format(
                            "[%s:%s] Query execution error '%s'. SQL: %s Parameters: %s",
                            storeName, queryName, query.getName(), query.getSql(), buildParamsLog(query, tuple));
                    LOG.error(errorMsg, failure);
                })
                .onItem()
                .invoke(item -> {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace(String.format("[%s:%s] Result item: %s", storeName, queryName, item));
                    }
                })
                .onCompletion()
                .invoke(
                        () -> {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug(String.format(
                                        "[%s:%s] Executing single query finished: %s",
                                        storeName, queryName, query.getName()));
                            }
                        });
    }

    // Генерация парметров в погдотоленном запросе
    private static Tuple prepareTuple(SQL.InsertData query, Map<String, Elements.Parameter<?>> params) {
        Tuple tuple = Tuple.tuple();
        query.getParametersTokens().forEach(token -> {
            Elements.Parameter<?> param = params.get(token);
            if (param != null) param.addToTuple(tuple);
        });
        return tuple;
    }

    // Генерация строки с текстовым описание параметров
    private static String buildParamsLog(SQL.InsertData query, Tuple tuple) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tuple.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(query.getParametersTokens().get(i)).append("=").append(tuple.getValue(i));
        }
        return sb.toString();
    }
}
