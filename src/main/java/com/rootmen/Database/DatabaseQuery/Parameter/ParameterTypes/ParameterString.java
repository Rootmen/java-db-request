package com.rootmen.Database.DatabaseQuery.Parameter.ParameterTypes;

import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterExceptionErrorType;
import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.Parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParameterString extends Parameter {

    public ParameterString(String ID, String name, String value) throws ParameterException {
        super(ID, name, value);
    }

    @Override
    public void addParameterToStatement(PreparedStatement statement, int index) throws SQLException {
        statement.setString(index, this.getValue());
    }

    @Override
    public ParameterException getExceptionError() {
        try {
            return null;
        } catch (Exception e) {
            return new ParameterExceptionErrorType(this.getValue(), "Integer", e);
        }
    }

}
