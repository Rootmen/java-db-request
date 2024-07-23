package ru.iedt.database.request.store.democlass;

import io.quarkus.runtime.annotations.RegisterForReflection;
import ru.iedt.database.request.store.DefinitionStore;
import ru.iedt.database.request.store.QueryStoreDefinition;

@RegisterForReflection
@DefinitionStore
public class Demo3QueryStoreDefinition extends QueryStoreDefinition {
  @Override
  public String getResourcePatch() {
    return "/store3.txt";
  }

  @Override
  public String getStoreName() {
    return "store3";
  }

  @Override
  public Class<?> getResourceClass() {
    return this.getClass();
  }
}
