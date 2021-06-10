package com.rootmen.DatabaseController.Utils.Databse;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rootmen.DatabaseController.Entities.Parameter.Parameter;

import java.sql.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseMethods {

    public DatabaseMethods() {
    }

    public static ArrayNode getJSON(PreparedStatement statement) throws SQLException {
        try {
            boolean hasMoreResults = statement.execute();
            ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
            while (hasMoreResults || statement.getUpdateCount() > -1) {
                SimpleEntry<ResultSetMetaData, ResultSet> resultSet = getResultSet(statement);
                if (resultSet.getValue() != null) {
                    while (resultSet.getValue().next()) {
                        int numColumns = resultSet.getKey().getColumnCount();
                        ObjectNode object = JsonNodeFactory.instance.objectNode();
                        for (int i = 1; i < numColumns + 1; i++) {
                            String column_name = resultSet.getKey().getColumnName(i);
                            if (resultSet.getKey().getColumnType(i) == java.sql.Types.ARRAY) {
                                object.set(column_name, (ArrayNode) resultSet.getValue().getArray(column_name));
                            } else if (resultSet.getKey().getColumnType(i) == java.sql.Types.BIGINT) {
                                object.put(column_name, resultSet.getValue().getInt(column_name));
                            } else if (resultSet.getKey().getColumnType(i) == java.sql.Types.BOOLEAN) {
                                object.put(column_name, resultSet.getValue().getBoolean(column_name));
                            } else if (resultSet.getKey().getColumnType(i) == java.sql.Types.BLOB) {
                                object.put(column_name, resultSet.getValue().getBlob(column_name).toString());
                            } else if (resultSet.getKey().getColumnType(i) == java.sql.Types.DOUBLE) {
                                object.put(column_name, resultSet.getValue().getDouble(column_name));
                            } else if (resultSet.getKey().getColumnType(i) == java.sql.Types.FLOAT) {
                                object.put(column_name, resultSet.getValue().getFloat(column_name));
                            } else if (resultSet.getKey().getColumnType(i) == java.sql.Types.INTEGER) {
                                object.put(column_name, resultSet.getValue().getInt(column_name));
                            } else if (resultSet.getKey().getColumnType(i) == java.sql.Types.NVARCHAR) {
                                object.put(column_name, resultSet.getValue().getNString(column_name));
                            } else if (resultSet.getKey().getColumnType(i) == java.sql.Types.VARCHAR) {
                                object.put(column_name, resultSet.getValue().getString(column_name));
                            } else if (resultSet.getKey().getColumnType(i) == java.sql.Types.TINYINT) {
                                object.put(column_name, resultSet.getValue().getInt(column_name));
                            } else if (resultSet.getKey().getColumnType(i) == java.sql.Types.SMALLINT) {
                                object.put(column_name, resultSet.getValue().getInt(column_name));
                            } else if (resultSet.getKey().getColumnType(i) == java.sql.Types.DATE) {
                                object.put(column_name, resultSet.getValue().getDate(column_name).toString());
                            } else if (resultSet.getKey().getColumnType(i) == java.sql.Types.TIMESTAMP) {
                                object.put(column_name, resultSet.getValue().getTimestamp(column_name).toString());
                            } else {
                                object.put(column_name, String.valueOf(resultSet.getValue().getObject(column_name)));
                            }
                        }
                        arrayNode.add(object);

                    }
                }
            }
            return arrayNode;
        } catch (SQLException error) {
            error.printStackTrace();
            throw error;
        }
    }

    public static String getText(PreparedStatement statement) throws SQLException {
        try {
            boolean hasMoreResults = statement.execute();
            StringBuilder result = new StringBuilder();
            while (hasMoreResults || statement.getUpdateCount() > -1) {
                SimpleEntry<ResultSetMetaData, ResultSet> resultSet = getResultSet(statement);
                if (resultSet.getValue() != null) {
                    result = new StringBuilder();
                    while (resultSet.getValue().next()) {
                        int numColumns = resultSet.getKey().getColumnCount();
                        for (int i = 1; i < numColumns + 1; i++) {
                            result.append(resultSet.getValue().getString(i));
                        }
                    }
                }
                hasMoreResults = statement.getMoreResults();
            }
            return result.toString();
        } catch (SQLException error) {
            error.printStackTrace();
            throw error;
        }
    }

    public static SimpleEntry<ResultSetMetaData, ResultSet> getResultSet(PreparedStatement statement) {
        try {
            ResultSet resultSet = statement.getResultSet();
            return new SimpleEntry<>(resultSet.getMetaData(), resultSet);
        } catch (SQLException error) {
            error.printStackTrace();
            throw new RuntimeException("Error in ResultSet load");
        }
    }

    public static PreparedStatement generatedPreparedStatement(String query, Connection connection, HashMap<String, Parameter> parameters) throws SQLException {
        if (connection == null) {
            throw new RuntimeException("connection is null");
        }
        SimpleEntry<StringBuilder, ArrayList<String>> queryConfig = generateStatementText(query, parameters);
        PreparedStatement statement = connection.prepareStatement(queryConfig.getKey().toString());
        if (!insertParameters(statement, queryConfig.getValue(), parameters)) {
            throw new RuntimeException("Error in insert Parameters to PreparedStatement");
        }
        return statement;
    }

    public static SimpleEntry<StringBuilder, ArrayList<String>> generateStatementText(String queryRaw, HashMap<String, Parameter> parameters) {
        StringBuilder query = new StringBuilder(queryRaw);
        ArrayList<String> parametersOrder = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\$.*?\\$");
        Matcher matcher = pattern.matcher(query);
        while (matcher.find()) {
            String token = matcher.group();
            token = token.substring(1, token.length() - 1);
            if (parameters.containsKey(token)) {
                query.replace(matcher.start(), matcher.end(), "?");
                parametersOrder.add(token);
                matcher.reset();
            }
        }
        return new SimpleEntry<>(query, parametersOrder);
    }

    public static boolean insertParameters(PreparedStatement statement, ArrayList<String> tokenOrder, HashMap<String, Parameter> parameters) {
        try {
            for (int g = 0; g < tokenOrder.size(); g++) {
                Parameter current = parameters.get(tokenOrder.get(g));
                if (current == null) {
                    throw new RuntimeException("Parameters " + tokenOrder.get(g) + " is null");
                }
                current.addParameterToStatement(statement, g + 1);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return false;
        }
        return true;
    }
}
