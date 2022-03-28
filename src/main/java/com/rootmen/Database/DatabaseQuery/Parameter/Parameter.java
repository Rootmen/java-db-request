package com.rootmen.Database.DatabaseQuery.Parameter;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Интерфейс для параметра в запросе.
 *
 * @version 1.0
 */
public interface Parameter<T> {

    void addParameterToStatement(PreparedStatement statement, int index, Connection connection) throws SQLException;

    T parameterCalculate(Connection connection) throws SQLException;

    T getValue();
}
