package com.rootmen.Database.DatabaseQuery.Query;

import com.fasterxml.jackson.databind.JsonNode;

import java.sql.SQLException;

/**
 * Интерфейс для запроса.
 *
 * @version 1.0
 */
public interface QueryInterface {
    public JsonNode getResult() throws SQLException;
    public JsonNode getNextLine() throws SQLException;
}
