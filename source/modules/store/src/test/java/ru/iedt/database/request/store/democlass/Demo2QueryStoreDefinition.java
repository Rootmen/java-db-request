package ru.iedt.database.request.store.democlass;

import io.quarkus.runtime.annotations.RegisterForReflection;
import ru.iedt.database.request.store.DefinitionStore;
import ru.iedt.database.request.store.QueryStoreDefinition;

@RegisterForReflection
@DefinitionStore
public class Demo2QueryStoreDefinition extends QueryStoreDefinition {

@Override
public String getResourcePatch() {
	return "/store2.txt";
}

@Override
public String getStoreName() {
	return "store2";
}

@Override
public Class<?> getResourceClass() {
	return this.getClass();
}
}
