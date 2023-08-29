package ru.iedt.database.request.demo;

import ru.iedt.database.request.database.store.DefinitionStore;
import ru.iedt.database.request.database.store.QueryStoreDefinition;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;


/* ВАЖНО пакет должен быть в package ru.* */
@DefinitionStore
public class StoreDefinitionEim extends QueryStoreDefinition {
    @Override
    public URI getStorePath() throws URISyntaxException {
        return Objects.requireNonNull(StoreDefinitionEim.class.getResource("/EIM")).toURI();
    }

    @Override
    public String getStoreName() {
        return "EIM";
    }
}
