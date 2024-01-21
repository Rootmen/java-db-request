package ru.iedt.database.request.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import ru.iedt.database.request.controller.parameter.ParameterInput;

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
                .flatMap(r -> client.query("INSERT INTO fruits (name) VALUES ('тест')")
                        .execute())
                .flatMap(r -> client.query("INSERT INTO fruits (name) VALUES ('тест')")
                        .execute())
                .await()
                .indefinitely();
        HashMap<String, ParameterInput> inputArrayList = new HashMap<>();
        inputArrayList.put("ID4", new ParameterInput( "ID4","-121313"));
        inputArrayList.put("ID3", new ParameterInput( "ID3","1"));
        inputArrayList.put("ID2", new ParameterInput( "ID2","2"));
        inputArrayList.put("ID1", new ParameterInput( "ID1","-3"));
        JsonObject jsonObject = databaseController
                .runningQuerySet("demo", "TEST_SELECT", inputArrayList, this.client)
                .get(0)
                .await()
                .indefinitely()
                .get("main")
                .await()
                .indefinitely()
                .iterator()
                .next()
                .toJson();
        System.out.println(jsonObject);
    }
}
