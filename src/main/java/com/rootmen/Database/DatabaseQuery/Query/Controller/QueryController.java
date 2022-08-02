package com.rootmen.Database.DatabaseQuery.Query.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rootmen.Database.DatabaseQuery.JsonParser.MapperConfig;
import com.rootmen.Database.DatabaseQuery.Parameter.Parameter;
import com.rootmen.Database.DatabaseQuery.Query.Binder.ResultSetWrapper;

import java.math.BigDecimal;
import java.sql.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryController implements QueryInterface {
    protected boolean hasMoreResults;
    protected boolean isCompleted = false;
    protected boolean noCloseConnection = false;
    Connection connection;
    PreparedStatement statement;
    AbstractMap.SimpleEntry<ResultSetMetaData, ResultSet> activeResultSet = null;


    public QueryController(StringBuilder query, HashMap<String, Parameter<?>> parameters, Connection connection) throws SQLException {
        this.connection = connection;
        this.statement = generatePreparedStatement(query, parameters);
        this.hasMoreResults = this.statement.execute();
    }

    public QueryController(StringBuilder query, HashMap<String, Parameter<?>> parameters, Connection connection, boolean noCloseConnection) throws SQLException {
        this.connection = connection;
        this.noCloseConnection = noCloseConnection;
        this.statement = generatePreparedStatement(query, parameters);
        this.hasMoreResults = this.statement.execute();
    }


    @Override
    public boolean runQuery() {
        return true;
    }


    @Override
    public JsonNode getResult() throws SQLException {
        if (this.isCompleted) {
            throw new SQLException("Транзакция завершена");
        }
        return this.executeAll();
    }

    @Override
    public <T> ArrayList<T> getResult(Class<? extends ResultSetWrapper<T>> resultSetWrapper) throws SQLException {
        if (this.isCompleted) {
            throw new SQLException("Транзакция завершена");
        }
        return this.executeAll(resultSetWrapper);
    }


    @Override
    public ObjectNode getNextLine() throws SQLException {
        if (this.isCompleted) {
            return null;
        }
        return this.executeLine();
    }


    protected PreparedStatement generatePreparedStatement(StringBuilder text, HashMap<String, Parameter<?>> parameters) throws SQLException {
        String update = text.toString();
        for (Parameter<?> parameter : parameters.values()) {
            HashMap<String, String> when = parameter.getWhen();
            if (when == null || when.size() == 0) continue;
            if (when.get(parameter.getValue().toString()) == null && when.get(null) != null) {
                update = update.replaceAll("\\$" + parameter.getParameterName().substring(1, parameter.getParameterName().length() - 1) + "\\$", Matcher.quoteReplacement(when.get(null)));
            } else if (when.get(parameter.getValue().toString()) != null) {
                update = update.replaceAll("\\$" + parameter.getParameterName().substring(1, parameter.getParameterName().length() - 1) + "\\$", Matcher.quoteReplacement(when.get(parameter.getValue().toString())));
            }
        }
        text = new StringBuilder(update);
        Matcher matcher = Pattern.compile("\\$.*?\\$").matcher(text);
        ArrayList<String> parametersArray = new ArrayList<>();
        while (matcher.find()) {
            String token = text.substring(matcher.start(), matcher.end());
            if (parameters.containsKey(token)) {
                parametersArray.add(token);
            }
        }
        PreparedStatement statement = this.connection.prepareStatement(text.toString().replaceAll("\\$.*?\\$", "?"));
        for (int g = 0; g < parametersArray.size(); g++) {
            Parameter<?> current = parameters.get(parametersArray.get(g));
            if (current != null) {
                current.addParameterToStatement(statement, g + 1, this.connection);
            }
        }
        return statement;
    }


    protected <T> ArrayList<T> executeAll(Class<? extends ResultSetWrapper<T>> resultSetWrapper) throws SQLException {
        ArrayList<T> resultSetWrapperArrayList = new ArrayList<>();
        while (this.hasMoreResults || this.statement.getUpdateCount() > -1) {
            AbstractMap.SimpleEntry<ResultSetMetaData, ResultSet> data = this.getNextData(this.statement);
            if (data != null && data.getValue() != null) {
                while (data.getValue().next()) {
                    try {
                        ResultSetWrapper<T> tResultSetWrapper = resultSetWrapper.newInstance();
                        tResultSetWrapper.wrapperResultSet(data.getValue());
                        resultSetWrapperArrayList.add((T) tResultSetWrapper);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            this.hasMoreResults = this.statement.getMoreResults();
        }
        this.close();
        return resultSetWrapperArrayList;
    }

    protected JsonNode executeAll() throws SQLException {
        int index = 0;
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        while (this.hasMoreResults || this.statement.getUpdateCount() > -1) {
            AbstractMap.SimpleEntry<ResultSetMetaData, ResultSet> data = this.getNextData(this.statement);
            ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
            if (data != null && data.getValue() != null) {
                while (data.getValue().next()) {
                    arrayNode.add(getJsonObject(data));
                }
                objectNode.set(String.valueOf(index++), arrayNode);
            }
            this.hasMoreResults = this.statement.getMoreResults();
        }
        this.close();
        return (objectNode.size() == 1) ? objectNode.get("0") : objectNode;
    }


    protected ObjectNode executeLine() throws SQLException {
        while (this.hasMoreResults || this.statement.getUpdateCount() > -1) {
            if (this.activeResultSet == null) {
                this.activeResultSet = this.getNextData(this.statement);
            }
            if (this.activeResultSet != null && this.activeResultSet.getValue() != null) {
                if (this.activeResultSet.getValue().next()) {
                    return getJsonObject(this.activeResultSet);
                }
            }
            this.hasMoreResults = this.statement.getMoreResults();
        }
        this.close();
        this.isCompleted = true;
        return null;
    }


    protected static ObjectNode getJsonObject(AbstractMap.SimpleEntry<ResultSetMetaData, ResultSet> data) throws SQLException {
        int numColumns = data.getKey().getColumnCount();
        ObjectMapper mapper = MapperConfig.getMapper();
        ObjectNode object = mapper.createObjectNode();
        for (int i = 1; i < numColumns + 1; i++) {
            String column_name = data.getKey().getColumnName(i);
            if (data.getKey().getColumnType(i) == java.sql.Types.ARRAY) {
                Array array = data.getValue().getArray(column_name);
                if (array != null) {
                    object.set(column_name, mapper.valueToTree(new ArrayList<>(Arrays.asList((Object[]) array.getArray()))));
                } else {
                    object.set(column_name, null);
                }
            } else if (data.getKey().getColumnType(i) == java.sql.Types.BIGINT) {
                object.put(column_name, data.getValue().getBigDecimal(column_name));
            } else if (data.getKey().getColumnType(i) == java.sql.Types.BOOLEAN) {
                object.put(column_name, data.getValue().getBoolean(column_name));
            } else if (data.getKey().getColumnType(i) == java.sql.Types.BLOB) {
                object.put(column_name, data.getValue().getBlob(column_name).toString());
            } else if (data.getKey().getColumnType(i) == java.sql.Types.DOUBLE) {
                object.put(column_name, BigDecimal.valueOf(data.getValue().getDouble(column_name)));
            } else if (data.getKey().getColumnType(i) == java.sql.Types.FLOAT) {
                object.put(column_name, BigDecimal.valueOf(data.getValue().getFloat(column_name)));
            } else if (data.getKey().getColumnType(i) == java.sql.Types.INTEGER) {
                object.put(column_name, data.getValue().getInt(column_name));
            } else if (data.getKey().getColumnType(i) == java.sql.Types.NVARCHAR) {
                String result = data.getValue().getNString(column_name);
                if (result != null) {
                    result = result.trim();
                }
                object.put(column_name, result);
            } else if (data.getKey().getColumnType(i) == java.sql.Types.VARCHAR) {
                String result = data.getValue().getString(column_name);
                if (result != null) {
                    result = result.trim();
                }
                object.put(column_name, result);
            } else if (data.getKey().getColumnType(i) == java.sql.Types.TINYINT) {
                object.put(column_name, data.getValue().getInt(column_name));
            } else if (data.getKey().getColumnType(i) == java.sql.Types.SMALLINT) {
                object.put(column_name, data.getValue().getInt(column_name));
            } else if (data.getKey().getColumnType(i) == java.sql.Types.DATE) {
                object.put(column_name, data.getValue().getDate(column_name).toString());
            } else if (data.getKey().getColumnType(i) == java.sql.Types.TIMESTAMP) {
                object.put(column_name, data.getValue().getTimestamp(column_name).toString().trim());
            } else {
                try {
                    object.put(column_name, MapperConfig.getMapper().readTree(String.valueOf(data.getValue().getObject(column_name))));
                } catch (Exception e) {
                    object.put(column_name, String.valueOf(data.getValue().getObject(column_name)));
                }
            }
        }
        return object;
    }


    public AbstractMap.SimpleEntry<ResultSetMetaData, ResultSet> getNextData(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.getResultSet();
        if (resultSet == null) {
            return null;
        }
        resultSet.setFetchSize(10);
        return new AbstractMap.SimpleEntry<>(resultSet.getMetaData(), resultSet);
    }

    protected void close() throws SQLException {
        this.isCompleted = true;
        this.statement.close();
        this.statement = null;
        if (!this.noCloseConnection) {
            this.connection.close();
        }
    }
}
