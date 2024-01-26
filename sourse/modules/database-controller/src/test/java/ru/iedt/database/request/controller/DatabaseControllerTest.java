package ru.iedt.database.request.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.iedt.database.request.controller.parameter.ParameterInput;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.type.ParameterLocalDate;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.type.ParameterLocalDateTime;

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
        map.put("ID6", new ParameterInput("ID6", LocalDate.of(2024, 1, 1).format(ParameterLocalDate.getFormatter())));
        map.put(
                "ID7",
                new ParameterInput(
                        "ID7",
                        LocalDateTime.of(2024, 1, 1, 12, 0, 1, 1).format(ParameterLocalDateTime.getFormatter())));
        Uni<List<Map<String, RowSet<Row>>>> uni =
                databaseController.runningQuerySet("demo", "TEST_SELECT", map, client);
        System.out.println(
                uni.await().indefinitely().get(0).get("main").iterator().next().toJson());
    }
}
