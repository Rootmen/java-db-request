package com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ObjectsElements.Type;

import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterExceptionErrorType;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ObjectsElements.ParameterObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParameterString extends ParameterObjects<String> {

    public ParameterString(String ID, String name, String value) throws ParameterException {
        super(ID, name, value);
    }

    @Override
    public void addParameterToStatement(PreparedStatement statement, int index, Connection connection) throws SQLException {
        statement.setString(index, this.getValue());
    }

    @Override
    public ParameterException getExceptionError() {
        if (this.getValue() == null) {
            return new ParameterExceptionErrorType(this.getValue(), "String", new NullPointerException());
        }
        return null;
    }

}
