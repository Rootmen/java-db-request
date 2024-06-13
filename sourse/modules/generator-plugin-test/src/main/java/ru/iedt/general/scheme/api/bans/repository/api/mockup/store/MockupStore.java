package ru.iedt.general.scheme.api.bans.repository.api.mockup.store;


import ru.iedt.database.request.store.DefinitionStore;
import ru.iedt.database.request.store.QueryStoreDefinition;


@DefinitionStore
public class MockupStore extends QueryStoreDefinition {

  @Override
  public String getResourcePatch() {
    return "/query/mockup/MOCKUP.xml";
  }

  @Override
  public String getStoreName() {
    return "MOCKUP";
  }

  @Override
  public Class<?> getResourceClass() {
    return this.getClass();
  }
}
