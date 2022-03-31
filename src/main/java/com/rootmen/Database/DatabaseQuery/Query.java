package com.rootmen.Database.DatabaseQuery;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rootmen.Database.DatabaseQuery.Parameter.Parameter;

import java.sql.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Query {
    private StringBuilder query = null;
    private Connection connection = null;
    private HashMap<String, Parameter<?>> parameters = null;
    private final ArrayList<String> parametersArray = new ArrayList<>();


    public Query(StringBuilder query, HashMap<String, Parameter<?>> parameters, ConnectionsManager connection) throws SQLException {
        this.connection = connection.getConnection();
        this.generateQuery(query, parameters);
    }

    public Query(StringBuilder query, HashMap<String, Parameter<?>> parameters) throws SQLException {
        this.connection = (new ConnectionsManager()).getConnection();
        this.generateQuery(query, parameters);
    }


    public JsonNode runQuery() throws SQLException {
        PreparedStatement statement = this.getStatement();
        return this.execute(statement);
    }


    private void generateQuery(StringBuilder text, HashMap<String, Parameter<?>> parameters) {
        this.parameters = parameters;
        Matcher matcher = Pattern.compile("\\$.*?\\$").matcher(text);
        while (matcher.find()) {
            String token = text.substring(matcher.start(), matcher.end());
            if (parameters.containsKey(token)) {
                this.parametersArray.add(token);
            }
        }
        this.query = new StringBuilder(text.toString().replaceAll("\\$.*?\\$", "?"));
    }


    private PreparedStatement getStatement() throws SQLException {
        PreparedStatement statement = this.connection.prepareStatement(query.toString());
        for (int g = 0; g < this.parametersArray.size(); g++) {
            Parameter<?> current = this.parameters.get(parametersArray.get(g));
            if (current != null) {
                current.addParameterToStatement(statement, g + 1, this.connection);
            }
        }
        return statement;
    }


    private JsonNode execute(PreparedStatement statement) throws SQLException {
        boolean hasMoreResults = statement.execute();
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        int index = 0;
        while (hasMoreResults || statement.getUpdateCount() > -1) {
            SimpleEntry<ResultSetMetaData, ResultSet> data = this.getNextData(statement);
            ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
            if (data != null && data.getValue() != null) {
                while (data.getValue().next()) {
                    int numColumns = data.getKey().getColumnCount();
                    ObjectNode object = JsonNodeFactory.instance.objectNode();
                    for (int i = 1; i < numColumns + 1; i++) {
                        String column_name = data.getKey().getColumnName(i);
                        if (data.getKey().getColumnType(i) == java.sql.Types.ARRAY) {
                            Array array = data.getValue().getArray(column_name);
                            if (array != null) {
                                ObjectMapper mapper = new ObjectMapper();
                                object.set(column_name, mapper.valueToTree(new ArrayList<>(Arrays.asList((Object[]) array.getArray()))));
                            } else {
                                object.set(column_name, null);
                            }
                        } else if (data.getKey().getColumnType(i) == java.sql.Types.BIGINT) {
                            object.put(column_name, data.getValue().getInt(column_name));
                        } else if (data.getKey().getColumnType(i) == java.sql.Types.BOOLEAN) {
                            object.put(column_name, data.getValue().getBoolean(column_name));
                        } else if (data.getKey().getColumnType(i) == java.sql.Types.BLOB) {
                            object.put(column_name, data.getValue().getBlob(column_name).toString());
                        } else if (data.getKey().getColumnType(i) == java.sql.Types.DOUBLE) {
                            object.put(column_name, data.getValue().getDouble(column_name));
                        } else if (data.getKey().getColumnType(i) == java.sql.Types.FLOAT) {
                            object.put(column_name, data.getValue().getFloat(column_name));
                        } else if (data.getKey().getColumnType(i) == java.sql.Types.INTEGER) {
                            object.put(column_name, data.getValue().getInt(column_name));
                        } else if (data.getKey().getColumnType(i) == java.sql.Types.NVARCHAR) {
                            object.put(column_name, data.getValue().getNString(column_name));
                        } else if (data.getKey().getColumnType(i) == java.sql.Types.VARCHAR) {
                            object.put(column_name, data.getValue().getString(column_name));
                        } else if (data.getKey().getColumnType(i) == java.sql.Types.TINYINT) {
                            object.put(column_name, data.getValue().getInt(column_name));
                        } else if (data.getKey().getColumnType(i) == java.sql.Types.SMALLINT) {
                            object.put(column_name, data.getValue().getInt(column_name));
                        } else if (data.getKey().getColumnType(i) == java.sql.Types.DATE) {
                            object.put(column_name, data.getValue().getDate(column_name).toString());
                        } else if (data.getKey().getColumnType(i) == java.sql.Types.TIMESTAMP) {
                            object.put(column_name, data.getValue().getTimestamp(column_name).toString());
                        } else {
                            object.put(column_name, String.valueOf(data.getValue().getObject(column_name)));
                        }
                    }
                    arrayNode.add(object);
                }
                objectNode.set(String.valueOf(index++), arrayNode);
            }
            hasMoreResults = statement.getMoreResults();
        }
        return (objectNode.size() == 1) ? objectNode.get("0") : objectNode;
    }


    public SimpleEntry<ResultSetMetaData, ResultSet> getNextData(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.getResultSet();
        return (resultSet == null) ? null : new SimpleEntry<>(resultSet.getMetaData(), resultSet);
    }


    public StringBuilder getQuery() {
        return this.query;
    }
}
