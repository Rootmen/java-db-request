package com.rootmen.DatabaseController.Entities.Parameter;


public class ParameterFactory {

    public static Parameter getParameter(String ID, String name, ParametersType type) {
        return new Parameter(ID, name, type);
    }

    public static Parameter getParameter(String ID, String name, ParametersType type, String value) {
        return new Parameter(ID, name, type, value);
    }
}
