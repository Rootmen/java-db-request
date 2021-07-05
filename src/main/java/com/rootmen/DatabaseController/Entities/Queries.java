package com.rootmen.DatabaseController.Entities;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.rootmen.DatabaseController.DocumentReader.Parser.Utils.ParserMethods;
import com.rootmen.DatabaseController.Entities.Parameter.ParameterClasses.Parameter;
import com.rootmen.DatabaseController.Utils.Databse.DatabaseMethods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *
 *
 *
 *
 *
 *
 **/
public class Queries {
    private String ID = null;                           //ID запроса
    private String query = null;                        //запрос
    private ArrayNode result = null;                    //Результат запроса
    private boolean queriesReady = false;               //Тег готовности запроса
    private boolean queriesError = false;               //Тег готовности запроса
    private boolean async = false;                      //Асинхронный запрос
    HashMap<String, Parameter> parameters = null;
    Connection connection = null;

    Queries(String ID, String query, boolean async, HashMap<String, Parameter> parameters, Connection connection) throws SQLException {
        if (this.configures(ID, query, async, parameters, connection)) {
            this.executeQueries();
        }
    }

    private boolean configures(String ID, String query, boolean async, HashMap<String, Parameter> parameters, Connection connection) {
        if (ID == null || query == null || connection == null) {
            return false;
        }
        this.ID = ID;
        this.async = async;
        this.query = query;
        this.parameters = parameters;
        this.connection = connection;
        return true;
    }


    private void executeQueries() throws SQLException {
        if (!async) {
            PreparedStatement statement = DatabaseMethods.generatedPreparedStatement(query, connection, parameters);
            this.result = DatabaseMethods.getJSON(statement);
            this.queriesReady = true;
        }
    }

    public ArrayNode getResult() {
        if (queriesReady) {
            return this.result;
        } else throw new RuntimeException();
    }

    public <E> E getConvertResult(JavaType type) {
        return ParserMethods.convertValue(getResult(), type);
    }

    /*public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Parameter INT_VALUE = ParameterFactory.getParameter("INT_VALUE", "INT_VALUE", ParameterType.Int, "1");
        Parameter INT_VALUE2 = ParameterFactory.getParameter("INT_VALUE2", "INT_VALUE2", ParameterType.Int, "2");
        Parameter STRING_VALUE1 = ParameterFactory.getParameter("STRING_VALUE1", "STRING_VALUE1", ParameterType.String, "s1");
        Parameter STRING_VALUE2 = ParameterFactory.getParameter("STRING_VALUE2", "STRING_VALUE2", ParameterType.String, "s2");
        HashMap<String, Parameter> parameters = new HashMap<>();
        parameters.put("INT_VALUE", INT_VALUE);
        parameters.put("INT_VALUE2", INT_VALUE2);
        parameters.put("STRING_VALUE1", STRING_VALUE1);
        parameters.put("STRING_VALUE2", STRING_VALUE2);
        Parameter STRING_VALUE3 = ParameterFactory.getParameter("STRING_VALUE2", "STRING_VALUE2", ParameterType.String, "SELECT $INT_VALUE$ as aws, $STRING_VALUE1$ as aws2, $STRING_VALUE2$ as aws3, $STRING_VALUE1$ as aws4, $INT_VALUE$ as aws5,$INT_VALUE2$ as aws6;", connection, parameters);
        System.out.println(STRING_VALUE3.getValue());
    }*/

}
