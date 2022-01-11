package com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ArrayElements;

import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterExceptionErrorType;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ParameterAbstract;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class ParameterArray extends ParameterAbstract {

    ArrayList<?> currentValue;
    String arrayType;


    public ParameterArray(String ID, String name, ArrayList<?> value, String arrayType) throws ParameterException {
        super(ID, name);
        this.arrayType = arrayType;
        this.currentValue = value;
        ParameterException exception = this.getExceptionError();
        if (exception != null) {
            throw exception;
        }
    }


    @Override
    public void addParameterToStatement(PreparedStatement statement, int index, Connection connection) throws SQLException {
        Array array = connection.createArrayOf(this.arrayType, this.currentValue.toArray());
        statement.setArray(index, array);
    }


    @Override
    public <T> T getValue() {
        return (T) currentValue;
    }


    @Override
    public void parameterCalculate(Connection connection) throws SQLException {

    }

    @Override
    public ParameterException getExceptionError() {
        if (this.currentValue == null || this.arrayType == null) {
            return new ParameterExceptionErrorType("null", "Array", new NullPointerException());
        }
        return null;
    }
}
