package ru.iedt.database.request.store.democlass;

import io.quarkus.runtime.annotations.RegisterForReflection;
import ru.iedt.database.request.store.DefinitionStore;
import ru.iedt.database.request.store.QueryStoreDefinition;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@RegisterForReflection
@DefinitionStore
public class Demo1QueryStoreDefinition extends QueryStoreDefinition {
    @Override
    public URI getStorePath() throws URISyntaxException {
        return Objects.requireNonNull(this.getClass().getResource("/store1.txt"))
                .toURI();
    }

    @Override
    public String getStoreName() {
        return "store1";
    }
}