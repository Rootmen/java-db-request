package com.rootmen.Database.DatabaseQuery.Parameter.ParameterTypes;

import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterExceptionErrorType;
import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.Parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParameterInteger extends Parameter {

    public ParameterInteger(String ID, String name, String value) throws ParameterException {
        super(ID, name, value);
    }

    @Override
    public void addParameterToStatement(PreparedStatement statement, int index) throws SQLException {
        statement.setInt(index, Integer.parseInt(this.getValue()));
    }

    @Override
    public ParameterException getExceptionError() {
        try {
            Integer.parseInt(this.getValue());
            return null;
        } catch (Exception e) {
            return new ParameterExceptionErrorType(this.getValue(), "Integer", e);
        }
    }
}
