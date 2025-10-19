package ru.iedt.database.request.app.test;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.tuples.Tuple2;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.iedt.database.request.controller.DatabaseController;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QueryTest {
    private static final Logger LOG = LoggerFactory.getLogger(QueryTest.class);

    String DEFINITION_NAME = "QUERY_TEST";

    @Inject
    DatabaseController databaseController;

    @Inject
    PgPool client;

    @Test
    public void testRunningQuerySetMulti() {
        List<TestClass> result = databaseController
                .runningQuerySetMulti(DEFINITION_NAME, "GET_TEST", new ArrayList<>(), TestClass.class, client)
                .onItem()
                .transformToMulti(Tuple2::getItem2)
                .collect()
                .asList()
                .await()
                .indefinitely();

        TestClass testClass = result.getFirst();
        LOG.info(String.format("Element #%d\n", 1));
        LOG.info(testClass);
        Assertions.assertEquals(testClass.param_1, 1);
        Assertions.assertEquals(testClass.param_2, 1);
        Assertions.assertEquals(testClass.param_3, 1);
        Assertions.assertEquals(testClass.param_4, "test");
        Assertions.assertArrayEquals(testClass.param_5, new Integer[] {1, 2, 3});
        Assertions.assertArrayEquals(testClass.param_6, new Integer[] {1, 2, 3});
        Assertions.assertEquals(
                testClass.param_7,
                new ArrayList<>(Arrays.stream(new Integer[] {1, 2, 3}).toList()));
        Assertions.assertEquals(
                testClass.param_8,
                new ArrayList<>(Arrays.stream(new Integer[] {1, 2, 3}).toList()));

        testClass = result.get(1);
        LOG.info(String.format("Element #%d\n", 2));
        LOG.info(testClass);
        Assertions.assertEquals(testClass.param_1, 1);
        Assertions.assertEquals(testClass.param_2, 1);
        Assertions.assertEquals(testClass.param_3, 1);
        Assertions.assertEquals(testClass.param_4, "test2");
        Assertions.assertArrayEquals(testClass.param_5, new Integer[] {2, 3, 4});
        Assertions.assertArrayEquals(testClass.param_6, new Integer[] {2, 3, 4});
        Assertions.assertEquals(
                testClass.param_7,
                new ArrayList<>(Arrays.stream(new Integer[] {2, 3, 4}).toList()));
        Assertions.assertEquals(
                testClass.param_8,
                new ArrayList<>(Arrays.stream(new Integer[] {2, 3, 4}).toList()));
    }

    @Test
    public void testRunningQuerySetUni() {
        TestClass result = databaseController
                .runningQuerySetUni(DEFINITION_NAME, "GET_TEST", new ArrayList<>(), TestClass.class, client)
                .await()
                .indefinitely();

        System.out.println(result);
        Assertions.assertEquals(result.param_1, 1);
        Assertions.assertEquals(result.param_2, 1);
        Assertions.assertEquals(result.param_3, 1);
        Assertions.assertEquals(result.param_4, "test");
        Assertions.assertArrayEquals(result.param_5, new Integer[] {1, 2, 3});
        Assertions.assertArrayEquals(result.param_6, new Integer[] {1, 2, 3});
        Assertions.assertEquals(
                result.param_7,
                new ArrayList<>(Arrays.stream(new Integer[] {1, 2, 3}).toList()));
        Assertions.assertEquals(
                result.param_8,
                new ArrayList<>(Arrays.stream(new Integer[] {1, 2, 3}).toList()));
    }

    @Test
    public void testRunningQuerySetJson() {
        TestClass2 result = databaseController
                .runningQuerySetUni(DEFINITION_NAME, "GET_TEST_JSON", new ArrayList<>(), TestClass2.class, client)
                .await()
                .indefinitely();
        System.out.println(result);
        System.out.println(result.param_2.get(0).param_1);
    }

    @Test
    public void testRunningQuerySetTransaction() {
        TestClass2 result = databaseController
                .runningQuerySetUni(
                        DEFINITION_NAME, "GET_TEST_TRANSACTION", new ArrayList<>(), TestClass2.class, client)
                .await()
                .indefinitely();
        System.out.println(result);
        System.out.println(result.param_2.get(0).param_1);
    }
}
