package com.rootmen.DatabaseController.Entities.Parameter;


import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParameterFactory {

    public static Parameter getParameter(String ID, String name, ParametersType type) {
        return new Parameter(ID, name, type.getType(), type);
    }
}
