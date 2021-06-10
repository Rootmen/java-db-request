package com.rootmen.DatabaseController.Entities.Parameter;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;

import static com.rootmen.DatabaseController.Utils.DocumentParser.Parser.generatedPOJO;

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

    public static Parameter getParameter(String ID, String name, String type, ParameterWhen when, String query, Connection connection, HashMap<String, Parameter> parameters) {
        ParameterType[] types = ParameterType.values();
        for (ParameterType parameterType : types) {
            if (parameterType.getType().equalsIgnoreCase(type)) {
                return new Parameter(ID, name, parameterType, when, query, connection, parameters);
            }
        }
        throw new RuntimeException("Type " + type + "is not allowed");
    }

    public static Parameter getParameter(String ID, String name, String type, String query, Connection connection, HashMap<String, Parameter> parameters) {
        ParameterType parameterType = searchParameterType(type);
        if (parameterType != null) {
            return new Parameter(ID, name, parameterType, query, connection, parameters);
        }
        throw new RuntimeException("Type " + type + "is not allowed");
    }

    public static ParameterType searchParameterType(String type) {
        ParameterType[] types = ParameterType.values();
        for (ParameterType parameterType : types) {
            if (parameterType.getType().equalsIgnoreCase(type)) {
                return parameterType;
            }
        }
        return null;
    }

    public static Parameter getParameter(ObjectNode parameter) {
        ObjectMapper mapper = new ObjectMapper();
        Parameter.ParameterPOJO pojo = mapper.convertValue(parameter, Parameter.ParameterPOJO.class);
        return new Parameter(pojo);
    }

    public static Parameter getParameter(JsonNode parameter, Connection connection, HashMap<String, Parameter> parameters) {
        Parameter.ParameterPOJO config = generatedPOJO(parameter, Parameter.ParameterPOJO.class);
        if (config.ref != null) {
            throw new RuntimeException("POJO parameter have ref links");
        }
        return new Parameter(config, connection, parameters);
    }


}
