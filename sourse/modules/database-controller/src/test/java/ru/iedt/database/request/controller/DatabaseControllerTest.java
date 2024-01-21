package ru.iedt.database.request.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import ru.iedt.database.request.controller.parameter.ParameterInput;
import ru.iedt.database.request.controller.users.UsersModel;
import ru.iedt.database.request.controller.users.dto.UserAccount;

@QuarkusTest
public class DatabaseControllerTest {


    @Inject
    UsersModel usersModel;

    @Inject
    PgPool client;

    @Test
    public void testDatabaseController() throws Exception {
        client.query("DROP TABLE IF EXISTS fruits")
                .execute()
                .flatMap(r -> client.query("CREATE SCHEMA IF NOT EXISTS dnauthorization;")
                        .execute())
                .flatMap(r -> client.query(
                                "CREATE TABLE IF NOT EXISTS dnauthorization.users_account\n" + "                (\n"
                                        + "                        account_id                      uuid PRIMARY KEY default gen_random_uuid(),\n"
                                        + "                account_name                    text                                       NOT NULL UNIQUE,\n"
                                        + "        account_mail                    text                                       NOT NULL UNIQUE,\n"
                                        + "                account_password_verifier       text                                       NOT NULL,\n"
                                        + "                account_salt                    text                                       NOT NULL,\n"
                                        + "                account_last_password_update    timestamp        DEFAULT CURRENT_TIMESTAMP NOT NULL,\n"
                                        + "                account_password_reset_interval integer          DEFAULT 90                NOT NULL\n"
                                        + ");")
                        .execute())
                .flatMap(r -> client.query("Insert Into dnauthorization.users_account(account_name ,account_mail,account_password_verifier,account_salt) values ('test', 'test', 'test','test') ;")
                        .execute())
                .await()
                .indefinitely();

        System.out.println(usersModel.getAllUserAccount(client).toUni().await().indefinitely());


    }
}
