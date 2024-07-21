package ru.iedt.database.request.app.test;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowIterator;
import io.vertx.mutiny.sqlclient.RowSet;
import jakarta.inject.Inject;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import ru.iedt.database.request.controller.DatabaseController;

@QuarkusTest
public class QueryTest {
  String DEFINITION_NAME = "QUERY_TEST";
  @Inject DatabaseController databaseController;
  @Inject PgPool client;

  @Test
  public void testClassUni() {
    databaseController
        .runningQuerySet(DEFINITION_NAME, "GET_TEST", new HashMap<>(), client)
        .onItem()
        .transform(maps -> maps.getFirst().get("main"))
        .onItem()
        .transform(RowSet::iterator)
        .onItem()
        .transform(RowIterator::next)
        .onItem()
        .transform(row -> TestClass.from(row, TestClass.class))
        .map(
            testClass -> {
              System.out.println(testClass.toString());
              return null;
            })
        .replaceWithVoid()
        .await()
        .indefinitely();
  }
}
