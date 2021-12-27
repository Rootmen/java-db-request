package com.rootmen.Database.DatabaseQuery.Parameter;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Parameter {

    void parameterCalculate(Connection connection) throws SQLException;

    void addParameterToStatement(PreparedStatement statement, int index, Connection connection) throws SQLException;

    <T> T getValue();
}
