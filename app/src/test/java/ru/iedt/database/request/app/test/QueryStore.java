package ru.iedt.database.request.app.test;

import io.quarkus.runtime.annotations.RegisterForReflection;
import ru.iedt.database.request.store.DefinitionStore;
import ru.iedt.database.request.store.QueryStoreDefinition;

@RegisterForReflection
@DefinitionStore
public class QueryStore extends QueryStoreDefinition {

@Override
public String getResourcePatch() {
	return "/QUERY.xml";
}

@Override
public String getStoreName() {
	return "QUERY_TEST";
}

@Override
public Class<?> getResourceClass() {
	return this.getClass();
}
}
