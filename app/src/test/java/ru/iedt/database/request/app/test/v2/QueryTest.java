package ru.iedt.database.request.app.test.v2;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.mutiny.sqlclient.Pool;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.iedt.database.request.controller.v2.DatabaseController;
import ru.iedt.database.request.store.DefinitionStore;
import ru.iedt.database.request.store.QueryStoreDefinition;

@QuarkusTest
public class QueryTest {
    private static final Logger LOG = LoggerFactory.getLogger(QueryTest.class);

    @Inject
    DatabaseController databaseController;

    @Inject
    Pool client;

    @Test
    public void testRunningQuerySetMulti() {
        List<TestClass> result = databaseController
                .runQuerySetMulti(QueryStore.STORE_NAME, "GET_TEST_LIST_20", new ArrayList<>(), TestClass.class, client)
                .collect()
                .asList()
                .await()
                .indefinitely();
        Assertions.assertEquals(result.size(), 20);
    }

    @Test
    public void testRunningQuerySetJsonData() {
        List<TestClassJson> result = databaseController
                .runQuerySetMulti(QueryStore.STORE_NAME, "GET_TEST_JSON", TestClassJson.class, client)
                .collect()
                .asList()
                .await()
                .indefinitely();
        Assertions.assertEquals(result.size(), 20);
    }

    @Test
    public void testRunningQuerySetUni() {
        TestClass result = databaseController
                .runQuerySetUni(QueryStore.STORE_NAME, "GET_TEST", TestClass.class, client)
                .await()
                .indefinitely();
        Assertions.assertEquals(result.getParamIntegerOne(), 1);
        Assertions.assertEquals(result.getParamIntegerTwo(), 2);
        Assertions.assertEquals(result.getParamInteger3(), 3);
        Assertions.assertEquals(result.getParam4(), "test");
        Assertions.assertArrayEquals(result.getParam5(), new Integer[] {1, 2, 3});
        Assertions.assertArrayEquals(result.getParam6(), new Integer[] {1, 2, 3});
        Assertions.assertEquals(
                result.getParam7(),
                new ArrayList<>(Arrays.stream(new Integer[] {1, 2, 3}).toList()));
        Assertions.assertEquals(
                result.getParam8(), Arrays.stream(new Integer[] {1, 2, 3}).toList());
    }

    @DefinitionStore
    @RegisterForReflection
    public static class QueryStore extends QueryStoreDefinition {
        public static String STORE_NAME = "QUERY_V2";

        @Override
        public String getResourcePatch() {
            return "/QUERY_V2.xml";
        }

        @Override
        public String getStoreName() {
            return STORE_NAME;
        }

        @Override
        public Class<?> getResourceClass() {
            return this.getClass();
        }
    }
}
