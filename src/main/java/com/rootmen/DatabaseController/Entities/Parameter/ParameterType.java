package com.rootmen.DatabaseController.Entities.Parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public enum ParameterType {
    String {
        public String getType() {
            return "string";
        }

        public void addParameterToStatement(PreparedStatement statement, int index, String value) throws SQLException {
            statement.setString(index, value);
        }
    },
    Int {
        public String getType() {
            return "int";
        }

        public void addParameterToStatement(PreparedStatement statement, int index, String value) throws SQLException {
            statement.setInt(index, Integer.parseInt(value));
        }
    };

    public abstract void addParameterToStatement(PreparedStatement statement, int index, String value) throws SQLException;

    public abstract String getType();
}