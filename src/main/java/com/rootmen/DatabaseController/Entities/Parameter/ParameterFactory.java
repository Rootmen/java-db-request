package com.rootmen.DatabaseController.Entities.Parameter;


import java.sql.Connection;
import java.util.HashMap;

public class ParameterFactory {

    public static Parameter getParameter(String ID, String name, ParameterType type, String query, Connection connection, HashMap<String, Parameter> parameters) {
        return new Parameter(ID, name, type, query, connection, parameters);
    }

    public static Parameter getParameter(String ID, String name, ParameterType type, ParameterWhen when, String query, Connection connection, HashMap<String, Parameter> parameters) {
        return new Parameter(ID, name, type, when, query, connection, parameters);
    }

    public static Parameter getParameter(String ID, String name, ParameterType type, String value) {
        return new Parameter(ID, name, type, value);
    }

    public static Parameter getParameter(String ID, String name, ParameterType type, ParameterWhen when, String value) {
        return new Parameter(ID, name, type, when, value);
    }

    public static Parameter getParameter(String ID, String name, String type, String value) {
        ParameterType[] types = ParameterType.values();
        for (ParameterType parameterType : types) {
            if (parameterType.getType().equalsIgnoreCase(type)) {
                return new Parameter(ID, name, parameterType, value);
            }
        }
        throw new RuntimeException("Type " + type + "is not allowed");
    }

    public static Parameter getParameter(String ID, String name, String type, ParameterWhen when, String value) {
        ParameterType[] types = ParameterType.values();
        for (ParameterType parameterType : types) {
            if (parameterType.getType().equalsIgnoreCase(type)) {
                return new Parameter(ID, name, parameterType, when, value);
            }
        }
        throw new RuntimeException("Type " + type + "is not allowed");
    }
}
