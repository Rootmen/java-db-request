package ru.iedt.database.request.controller;

import com.fasterxml.jackson.databind.node.ArrayNode;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.iedt.database.request.controller.DatabaseController;
import ru.iedt.database.request.controller.parameter.ParameterInput;
import ru.iedt.database.request.store.QueryStoreDefinition;
import ru.iedt.database.request.store.QueryStoreList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

@QuarkusTest
public class DatabaseControllerTest {

    @Inject
    DatabaseController databaseController;

    @Inject
    PgPool client;

    @Test
    public void testDatabaseController() throws Exception {
        client.query("DROP TABLE IF EXISTS fruits")
                .execute()
                .flatMap(r -> client.query("CREATE TABLE fruits (id SERIAL PRIMARY KEY, name TEXT NOT NULL)")
                        .execute())
                .flatMap(r -> client.query("INSERT INTO fruits (name) VALUES ('тест')").execute())
                .flatMap(r -> client.query("INSERT INTO fruits (name) VALUES ('тест')").execute())
                .await()
                .indefinitely();
        HashMap<String, ParameterInput> inputArrayList = new HashMap<>();
        HashMap<String, Uni<RowSet<Row>>> stringUniHashMap = databaseController
                .runningQuerySet("demo", "TEST_SELECT", inputArrayList, this.client)
                .await()
                .indefinitely();

        stringUniHashMap
                .get("1")
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform((Row row) -> {
                    System.out.println(row.getInteger("id"));
                    return row.getInteger("id");
                })
                .toUni()
                .await()
                .indefinitely();

        stringUniHashMap
                .get("2")
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform((Row row) -> {
                    System.out.println(row.getInteger("id"));
                    return row.getInteger("id");
                })
                .toUni()
                .await()
                .indefinitely();
    }
}
