package com.rootmen.Database.DatabaseQuery.Query.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.rootmen.Database.DatabaseQuery.Query.Binder.ResultSetWrapper;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Интерфейс для запроса.
 *
 * @version 1.0
 */
public interface QueryInterface {
    public boolean runQuery() throws SQLException;
    public JsonNode getResult() throws SQLException;

    <T> ArrayList<T> getResult(Class<? extends ResultSetWrapper<T>> resultSetWrapper) throws SQLException;

    public JsonNode getNextLine() throws SQLException;
}
