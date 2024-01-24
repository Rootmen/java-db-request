package ru.iedt.database.request.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.iedt.database.request.controller.parameter.ParameterInput;

@QuarkusTest
public class DatabaseControllerTest {

    @Inject
    PgPool client;

    @Inject
    DatabaseController databaseController;

    @Test
    public void testDatabaseController() throws Exception {
        client.query("DROP TABLE IF EXISTS fruits")
                .execute()
                .flatMap(r -> client.query("CREATE SCHEMA IF NOT EXISTS dnauthorization;")
                        .execute())
                .await()
                .indefinitely();
        Map<String, ParameterInput> map = new HashMap<>();
        map.put("ID2", new ParameterInput("ID2", "-10"));
        Uni<List<Map<String, RowSet<Row>>>> uni =
                databaseController.runningQuerySet("demo", "TEST_SELECT", map, client);
        System.out.println(
                uni.await().indefinitely().get(0).get("main").iterator().next().toJson());
    }
}
