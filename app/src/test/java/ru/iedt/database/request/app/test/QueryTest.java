package ru.iedt.database.request.app.test;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.tuples.Tuple2;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowIterator;
import io.vertx.mutiny.sqlclient.RowSet;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.iedt.database.request.controller.DatabaseController;

@QuarkusTest
public class QueryTest {
String DEFINITION_NAME = "QUERY_TEST";
@Inject DatabaseController databaseController;
@Inject PgPool client;

@Test
public void testClassDefaultRunningQuerySet() {
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

@Test
public void testRunningQuerySetMulti() {
	Tuple2<Integer, Multi<TestClass>> result =
		databaseController
			.runningQuerySetMulti(
				DEFINITION_NAME, "GET_TEST", new ArrayList<>(), TestClass.class, client)
			.await()
			.indefinitely();
	Assertions.assertEquals(result.getItem1().intValue(), 2);
	List<TestClass> array = result.getItem2().collect().asList().await().indefinitely();
	for (int g = 0; g < array.size(); g++) {
	TestClass testClass = array.get(g);
	System.out.printf("Элемент №%d\n", g);
	System.out.println(testClass);
	Assertions.assertEquals(testClass.param_1, 1);
	Assertions.assertEquals(testClass.param_2, 1);
	Assertions.assertEquals(testClass.param_3, 1);
	Assertions.assertEquals(testClass.param_4, "test");
	Assertions.assertArrayEquals(testClass.param_5, new Integer[] {1, 2, 3});
	Assertions.assertArrayEquals(testClass.param_6, new Integer[] {1, 2, 3});
	Assertions.assertEquals(
		testClass.param_7, new ArrayList<>(Arrays.stream(new Integer[] {1, 2, 3}).toList()));
	Assertions.assertEquals(
		testClass.param_8, new ArrayList<>(Arrays.stream(new Integer[] {1, 2, 3}).toList()));
	}
}

@Test
public void testRunningQuerySetUni() {
	TestClass result =
		databaseController
			.runningQuerySetUni(
				DEFINITION_NAME, "GET_TEST", new ArrayList<>(), TestClass.class, client)
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
		result.param_7, new ArrayList<>(Arrays.stream(new Integer[] {1, 2, 3}).toList()));
	Assertions.assertEquals(
		result.param_8, new ArrayList<>(Arrays.stream(new Integer[] {1, 2, 3}).toList()));
}
}
