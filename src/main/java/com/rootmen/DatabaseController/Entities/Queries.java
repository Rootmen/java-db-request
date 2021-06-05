package com.rootmen.DatabaseController.Entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rootmen.DatabaseController.Entities.Parameter.Parameter;
import com.rootmen.DatabaseController.Entities.Parameter.ParameterFactory;
import com.rootmen.DatabaseController.Entities.Parameter.ParametersType;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 *
 *
 *
 * */
public class Queries {
    private String ID = null;                           //ID запроса
    private String result = null;                       //Результат запроса
    private boolean async = false;                      //Асинхронный запрос
    private boolean queriesReady = false;               //Тег готовности запроса

    /*
     *
     *
     *
     * */
    Queries(String ID, String SQL, boolean async, HashMap<String, Parameter> parameters, Connection connection) throws SQLException {
        this.ID = ID;
        this.async = async;
        System.out.println(this.runQueries(connection, SQL, parameters));
    }

    public ArrayNode runQueries(Connection connection, String SQL, HashMap<String, Parameter> parameters) throws SQLException {
        String tokensSQL = SQL;
        ArrayList<String> tokenOrder = new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\$.*?\\$");
        Matcher matcher = pattern.matcher(SQL);
        while (matcher.find()) {
            String token = matcher.group();
            tokensSQL = tokensSQL.replaceFirst("\\$.*?\\$", "?");
            tokenOrder.add(token.substring(1, token.length() - 1));
        }
        PreparedStatement statement = connection.prepareStatement(tokensSQL);
        for (int g = 0; g < tokenOrder.size(); g++) {
            Parameter current = parameters.get(tokenOrder.get(g));
            if (current == null) {
                throw new RuntimeException("Parameters " + tokenOrder.get(g) + " is null");
            }
            current.addParameterToStatement(statement, g + 1);
        }
        return executeStatement(statement);
    }

    private static ArrayNode executeStatement(PreparedStatement statement) throws SQLException {
        statement.execute();
        ArrayNode json = JsonNodeFactory.instance.arrayNode();
        ResultSet resultSet = statement.getResultSet();
        if (resultSet == null) {
            statement.close();
            return json;
        }
        ResultSetMetaData metaData = resultSet.getMetaData();
        while (resultSet.next()) {
            int numColumns = metaData.getColumnCount();
            ObjectNode object = JsonNodeFactory.instance.objectNode();
            for (int i = 1; i < numColumns + 1; i++) {
                String column_name = metaData.getColumnName(i);
                if (metaData.getColumnType(i) == java.sql.Types.ARRAY) {
                    ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
                    arrayNode.addAll((ArrayNode) resultSet.getArray(column_name));
                    object.put(column_name, arrayNode);
                } else if (metaData.getColumnType(i) == java.sql.Types.BIGINT) {
                    object.put(column_name, resultSet.getInt(column_name));
                } else if (metaData.getColumnType(i) == java.sql.Types.BOOLEAN) {
                    object.put(column_name, resultSet.getBoolean(column_name));
                } else if (metaData.getColumnType(i) == java.sql.Types.BLOB) {
                    object.put(column_name, resultSet.getBlob(column_name).toString());
                } else if (metaData.getColumnType(i) == java.sql.Types.DOUBLE) {
                    object.put(column_name, resultSet.getDouble(column_name));
                } else if (metaData.getColumnType(i) == java.sql.Types.FLOAT) {
                    object.put(column_name, resultSet.getFloat(column_name));
                } else if (metaData.getColumnType(i) == java.sql.Types.INTEGER) {
                    object.put(column_name, resultSet.getInt(column_name));
                } else if (metaData.getColumnType(i) == java.sql.Types.NVARCHAR) {
                    object.put(column_name, resultSet.getNString(column_name));
                } else if (metaData.getColumnType(i) == java.sql.Types.VARCHAR) {
                    object.put(column_name, resultSet.getString(column_name));
                } else if (metaData.getColumnType(i) == java.sql.Types.TINYINT) {
                    object.put(column_name, resultSet.getInt(column_name));
                } else if (metaData.getColumnType(i) == java.sql.Types.SMALLINT) {
                    object.put(column_name, resultSet.getInt(column_name));
                } else if (metaData.getColumnType(i) == java.sql.Types.DATE) {
                    object.put(column_name, resultSet.getDate(column_name).toString());
                } else if (metaData.getColumnType(i) == java.sql.Types.TIMESTAMP) {
                    object.put(column_name, resultSet.getTimestamp(column_name).toString());
                } else {
                    object.put(column_name, String.valueOf(resultSet.getObject(column_name)));
                }
            }
            json.add(object);
        }
        return json;
    }

    public String getResult() {
        return result;
    }

    static final String DB_URL = "jdbc:postgresql://distance-course.ru:5432/main";
    static final String USER = "authorization_app";
    static final String PASS = "ga4kHTswrjcqwWDi51QA";

    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Parameter INT_VALUE = ParameterFactory.getParameter("INT_VALUE", "INT_VALUE", ParametersType.Int);
        INT_VALUE.updateParameter("1");
        Parameter INT_VALUE2 = ParameterFactory.getParameter("INT_VALUE2", "INT_VALUE2", ParametersType.Int);
        INT_VALUE2.updateParameter("2");
        Parameter STRING_VALUE1 = ParameterFactory.getParameter("STRING_VALUE1", "STRING_VALUE1", ParametersType.String);
        STRING_VALUE1.updateParameter("s1");
        Parameter STRING_VALUE2 = ParameterFactory.getParameter("STRING_VALUE2", "STRING_VALUE2", ParametersType.String);
        STRING_VALUE2.updateParameter("s2");
        HashMap<String, Parameter> parameters = new HashMap<>();
        parameters.put("INT_VALUE", INT_VALUE);
        parameters.put("INT_VALUE2", INT_VALUE2);
        parameters.put("STRING_VALUE1", STRING_VALUE1);
        parameters.put("STRING_VALUE2", STRING_VALUE2);
        Queries queries = new Queries("Main", "SELECT $INT_VALUE$ as aws, $STRING_VALUE1$ as aws2, $STRING_VALUE2$ as aws3, $STRING_VALUE1$ as aws4, $INT_VALUE$ as aws5,$INT_VALUE2$ as aws6;", false, parameters, connection);
        System.out.println(queries.getResult());
    }

}
