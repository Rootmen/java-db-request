package ru.iedt.database.request.controller.v2;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import jakarta.inject.Singleton;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import ru.iedt.database.request.controller.entity.BaseEntity;
import ru.iedt.database.request.controller.parameter.ParameterInput;
import ru.iedt.database.request.controller.v2.utils.DatabaseUtils;
import ru.iedt.database.request.parser.elements.v3.ParserEngine;
import ru.iedt.database.request.store.QueryStoreDefinition;
import ru.iedt.database.request.store.QueryStoreList;
import ru.iedt.database.request.structures.nodes.v3.Elements;

@Singleton
public class DatabaseController {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseController.class);
    private static final Map<String, Elements.Definition> QUERY_STORE_DEFINITION_MAP = new HashMap<>();
    private static final ArrayList<QueryStoreDefinition> INITIALIZERS = QueryStoreList.getStoresMetadata();

    static {
        for (QueryStoreDefinition queryStoreDefinition : INITIALIZERS) {
            String storeName = queryStoreDefinition.getStoreName();
            if (QUERY_STORE_DEFINITION_MAP.containsKey(storeName)) {
                RuntimeException ex = new RuntimeException("Multiple stores with the same name!");
                LOG.error(String.format("Duplicate store name detected: %s", storeName), ex);
                throw ex;
            }
            InputStream file = queryStoreDefinition
                    .getResourceClass()
                    .getResourceAsStream(queryStoreDefinition.getResourcePatch());
            Elements.Definition definition = ParserEngine.parsingXml(file);
            QUERY_STORE_DEFINITION_MAP.put(storeName, definition);
        }
    }

    private <T> Multi<T> runQuerySet(
            String storeName,
            String querySetName,
            ArrayList<ParameterInput> parameterInputs,
            String resultQueryName,
            Function<RowSet<Row>, Multi<T>> resultMapper,
            PgPool client) {

        Elements.Definition definition = QUERY_STORE_DEFINITION_MAP.get(storeName);
        if (definition == null) {
            RuntimeException ex = new RuntimeException("Definition store not found");
            LOG.error(String.format("[%s] Definition store not found", storeName), ex);
            return Multi.createFrom().failure(ex);
        }

        Elements.QuerySet querySet = definition.getQuerySet().get(querySetName);
        if (querySet == null) {
            RuntimeException ex = new RuntimeException("QuerySet not found");
            LOG.error(String.format("[%s:%s] QuerySet not found", storeName, querySetName), ex);
            return Multi.createFrom().failure(ex);
        }

        List<Elements.Queries> queriesList = querySet.getQueries();
        if (queriesList.isEmpty()) {
            RuntimeException ex = new RuntimeException("Empty queries list");
            LOG.error(String.format("[%s:%s] Empty queries list", storeName, querySetName), ex);
            return Multi.createFrom().failure(ex);
        }

        if (queriesList.size() != 1) {
            RuntimeException ex = new RuntimeException("Queries list has more 1 element");
            LOG.error(String.format("[%s:%s] Queries list has more 1 element", storeName, querySetName), ex);
            return Multi.createFrom().failure(ex);
        }

        Elements.Queries queries = queriesList.getFirst();
        Map<String, Elements.Parameter<?>> parameters =
                DatabaseUtils.createParameters(parameterInputs, querySet.getParameters());
        Map<String, Elements.Template> templates = definition.getTemplate();

        return DatabaseUtils.runQueries(
                queries, parameters, templates, resultQueryName, resultMapper, client, storeName, querySetName);
    }

    // Остальные методы без изменений
    public <T extends BaseEntity> Multi<T> runQuerySetMulti(
            String storeName,
            String querySetName,
            ArrayList<ParameterInput> parameterInputs,
            Class<T> entityClass,
            PgPool client) {
        return this.runQuerySetMulti(storeName, querySetName, parameterInputs, "main", entityClass, client);
    }

    public <T extends BaseEntity> Multi<T> runQuerySetMulti(
            String storeName,
            String querySetName,
            ArrayList<ParameterInput> parameterInputs,
            String resultQueryName,
            Class<T> entityClass,
            PgPool client) {

        Function<RowSet<Row>, Multi<T>> resultMapper =
                rowSet -> rowSet.toMulti().map(row -> BaseEntity.from(row, entityClass));

        return runQuerySet(storeName, querySetName, parameterInputs, resultQueryName, resultMapper, client);
    }

    public <T extends BaseEntity> Uni<T> runQuerySetUni(
            String storeName,
            String querySetName,
            ArrayList<ParameterInput> parameterInputs,
            Class<T> entityClass,
            PgPool client) {

        return runQuerySetUni(storeName, querySetName, parameterInputs, "main", entityClass, client);
    }

    public <T extends BaseEntity> Uni<T> runQuerySetUni(
            String storeName,
            String querySetName,
            ArrayList<ParameterInput> parameterInputs,
            String resultQueryName,
            Class<T> entityClass,
            PgPool client) {

        return runQuerySetMulti(storeName, querySetName, parameterInputs, resultQueryName, entityClass, client)
                .collect()
                .first();
    }
}
