package ru.iedt.database.request.controller;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.*;
import jakarta.inject.Singleton;
import java.io.InputStream;
import java.util.*;
import ru.iedt.database.request.controller.entity.BaseEntity;
import ru.iedt.database.request.controller.parameter.ParameterInput;
import ru.iedt.database.request.controller.utils.DatabaseUtils;
import ru.iedt.database.request.parser.elements.v3.ParserEngine;
import ru.iedt.database.request.store.QueryStoreDefinition;
import ru.iedt.database.request.store.QueryStoreList;
import ru.iedt.database.request.structures.nodes.v3.Elements;

@Singleton
public class DatabaseController {

private static final Map<String, Elements.Definition> QUERY_STORE_DEFINITION_MAP =
	new HashMap<>();
private static final ArrayList<QueryStoreDefinition> INITIALIZERS =
	QueryStoreList.getStoresMetadata();

static {
	for (QueryStoreDefinition queryStoreDefinition : INITIALIZERS) {
	String storeName = queryStoreDefinition.getStoreName();
	if (QUERY_STORE_DEFINITION_MAP.containsKey(storeName)) {
		throw new RuntimeException("Несколько хранилищ с одинаковым названием!");
	}
	InputStream file =
		queryStoreDefinition
			.getResourceClass()
			.getResourceAsStream(queryStoreDefinition.getResourcePatch());
	Elements.Definition definition = ParserEngine.parsingXml(file);
	QUERY_STORE_DEFINITION_MAP.put(storeName, definition);
	}
}

@Deprecated()
public Uni<List<Map<String, RowSet<Row>>>> runningQuerySet(
	String storeName,
	String queryName,
	Map<String, ParameterInput> parameterInputs,
	PgPool client) {
	Elements.Definition definition = QUERY_STORE_DEFINITION_MAP.get(storeName);
	if (definition == null) throw new RuntimeException("Хранилище Definition не найдено");
	Elements.QuerySet querySet = definition.getQuerySet().get(queryName);
	Map<String, Elements.Parameter<?>> parameters =
		DatabaseUtils.createParametersInputs(parameterInputs, querySet.getParameters());
	Map<String, Elements.Template> template = definition.getTemplate();
	List<Elements.Queries> queries = querySet.getQueries();
	List<Uni<Map<String, RowSet<Row>>>> unis = new ArrayList<>();
	for (Elements.Queries query : queries) {
	unis.add(DatabaseUtils.runQueries(query, parameters, template, client, storeName, queryName));
	}
	return Uni.join().all(unis).andCollectFailures();
}

public Uni<List<Map<String, RowSet<Row>>>> runningQuerySet(
	String storeName,
	String queryName,
	ArrayList<ParameterInput> parameterInputs,
	PgPool client) {
	Elements.Definition definition = QUERY_STORE_DEFINITION_MAP.get(storeName);
	if (definition == null) throw new RuntimeException("Хранилище Definition не найдено");
	Elements.QuerySet querySet = definition.getQuerySet().get(queryName);
	Map<String, Elements.Parameter<?>> parameters =
		DatabaseUtils.createParameters(parameterInputs, querySet.getParameters());
	Map<String, Elements.Template> template = definition.getTemplate();
	List<Elements.Queries> queries = querySet.getQueries();
	List<Uni<Map<String, RowSet<Row>>>> unis = new ArrayList<>();
	for (Elements.Queries query : queries) {
	unis.add(DatabaseUtils.runQueries(query, parameters, template, client, storeName, queryName));
	}
	return Uni.join().all(unis).andCollectFailures();
}

public Uni<RowSet<Row>> generateQuery(
	String storeName,
	String queryName,
	String sqlName,
	Integer index,
	ArrayList<ParameterInput> parameterInputs,
	PgPool client) {
	return this.runningQuerySet(storeName, queryName, parameterInputs, client)
		.onItem()
		.transform(maps -> maps.get(index))
		.onItem()
		.transform(map -> map.get(sqlName));
}

public <T extends BaseEntity> Uni<T> runningQuerySetUni(
	String storeName,
	String queryName,
	ArrayList<ParameterInput> parameterInputs,
	Class<T> tClass,
	PgPool client) {
	return this.runningQuerySetUni(
		storeName, queryName, "main", 0, parameterInputs, tClass, client);
}

public <T extends BaseEntity> Uni<T> runningQuerySetUni(
	String storeName,
	String queryName,
	String sqlName,
	ArrayList<ParameterInput> parameterInputs,
	Class<T> tClass,
	PgPool client) {
	return this.runningQuerySetUni(
		storeName, queryName, sqlName, 0, parameterInputs, tClass, client);
}

public <T extends BaseEntity> Uni<T> runningQuerySetUni(
	String storeName,
	String queryName,
	String sqlName,
	Integer index,
	ArrayList<ParameterInput> parameterInputs,
	Class<T> tClass,
	PgPool client) {
	return this.generateQuery(storeName, queryName, sqlName, index, parameterInputs, client)
		.onItem()
		.transform(RowSet::iterator)
		.onItem()
		.transform(rowRowIterator -> rowRowIterator.hasNext() ? rowRowIterator.next() : null)
		.onItem()
		.transform(row -> T.from(row, tClass));
}

public <T extends BaseEntity> Uni<Tuple2<Integer, Multi<T>>> runningQuerySetMulti(
	String storeName,
	String queryName,
	ArrayList<ParameterInput> parameterInputs,
	Class<T> tClass,
	PgPool client) {
	return this.runningQuerySetMulti(
		storeName, queryName, "main", 0, parameterInputs, tClass, client);
}

public <T extends BaseEntity> Uni<Tuple2<Integer, Multi<T>>> runningQuerySetMulti(
	String storeName,
	String queryName,
	String sqlName,
	ArrayList<ParameterInput> parameterInputs,
	Class<T> tClass,
	PgPool client) {
	return this.runningQuerySetMulti(
		storeName, queryName, sqlName, 0, parameterInputs, tClass, client);
}

public <T extends BaseEntity> Uni<Tuple2<Integer, Multi<T>>> runningQuerySetMulti(
	String storeName,
	String queryName,
	String sqlName,
	Integer index,
	ArrayList<ParameterInput> parameterInputs,
	Class<T> tClass,
	PgPool client) {
	return this.generateQuery(storeName, queryName, sqlName, index, parameterInputs, client)
		.onItem()
		.transform(
			rows ->
				Tuple2.of(
					rows.size(), rows.toMulti().onItem().transform(row -> T.from(row, tClass))));
}
}
