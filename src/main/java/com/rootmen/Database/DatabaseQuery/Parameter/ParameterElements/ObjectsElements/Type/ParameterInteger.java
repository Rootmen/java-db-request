package com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ObjectsElements.Type;

import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterExceptionErrorType;
import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ObjectsElements.ParameterObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParameterInteger extends ParameterObjects<Integer> {


    public ParameterInteger(String ID, String name, String value) throws ParameterException {
        this(ID, name, parseString(value));
    }


    public ParameterInteger(String ID, String name, Integer value) throws ParameterException {
        super(ID, name, value);
    }


    @Override
    public void addParameterToStatement(PreparedStatement statement, int index, Connection connection) throws SQLException {
        statement.setInt(index, this.getValue());
    }


    @Override
    public ParameterException getExceptionError() {
        if (this.getValue() == null) {
            return new ParameterExceptionErrorType("null", "Integer", new NullPointerException());
        }
        return null;
    }


    static Integer parseString(String integer) throws ParameterException {
        try {
            return Integer.parseInt(integer);
        } catch (Exception e) {
            throw new ParameterExceptionErrorType(integer, "Integer", e);
        }
    }
}
