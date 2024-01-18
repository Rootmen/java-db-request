package ru.iedt.database.request.controller;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.inject.Singleton;
import ru.iedt.database.request.controller.parameter.ParameterInput;
import ru.iedt.database.request.parser.elements.v3.ParserEngine;
import ru.iedt.database.request.store.QueryStoreDefinition;
import ru.iedt.database.request.store.QueryStoreList;
import ru.iedt.database.request.structures.nodes.v3.Elements;

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
                if (QUERY_STORE_DEFINITION_MAP.containsKey(storeName))
                    throw new RuntimeException("Несколько хранилищ с одинаковым названием!");
                URI file = queryStoreDefinition.getStorePath();
                if (!Files.exists(Paths.get(file)))
                    throw new RuntimeException(String.format(
                            "Nor found file %s",
                            queryStoreDefinition.getStorePath().getPath()));
                Elements.Definition definition = ParserEngine.parsingXml(file);
                QUERY_STORE_DEFINITION_MAP.put(storeName, definition);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Uni<HashMap<String, Uni<RowSet<Row>>>> runningQuerySet(
            String storeName, String queryName, HashMap<String, ParameterInput> parameterInputs, PgPool client) {
        Elements.Definition definition = QUERY_STORE_DEFINITION_MAP.get(storeName);
        if (definition == null) throw new RuntimeException("Хранилище Definition не найдено");
        Elements.QuerySet querySet = definition.getQuerySet().get(queryName);
        Map<String, Elements.Parameter> parameters = querySet.getParameters();
        List<Elements.Queries> queries = querySet.getQueries();
        return client.withTransaction(conn -> {
            HashMap<String, Uni<RowSet<Row>>> result = new HashMap<>();
            Uni<RowSet<Row>> insertOne = conn.preparedQuery("INSERT INTO fruits (name) VALUES ($1) RETURNING id")
                    .execute(Tuple.of("тест"));
            result.put("1", insertOne);
            Uni<RowSet<Row>> insertTwo = conn.preparedQuery("INSERT INTO fruits (name) VALUES ($1) RETURNING id")
                    .execute(Tuple.of("тест"));
            result.put("2", insertTwo);
            return Uni.createFrom().item(result);
        });
    }

    public Map<String, Elements.Definition> getDefinitions() {
        return QUERY_STORE_DEFINITION_MAP;
    }
}
