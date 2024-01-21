package ru.iedt.database.request.controller.users;

import io.quarkus.runtime.annotations.RegisterForReflection;
import ru.iedt.database.request.store.DefinitionStore;
import ru.iedt.database.request.store.QueryStoreDefinition;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@RegisterForReflection
@DefinitionStore
public class UsersModelStore extends QueryStoreDefinition {
    @Override
    public URI getStorePath() throws URISyntaxException {
        return Objects.requireNonNull(this.getClass().getResource("/query/USERS_MODEL.xml"))
                .toURI();
    }

    @Override
    public String getStoreName() {
        return "users_model";
    }
}