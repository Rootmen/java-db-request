package com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ObjectsElements;

import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ParameterAbstract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

abstract public class ParameterObjects<T> extends ParameterAbstract {

    T currentValue;
    String arrayType;


    public ParameterObjects(String ID, String name, T value) throws ParameterException {
        super(ID, name);
        this.arrayType = arrayType;
        this.currentValue = value;
        ParameterException exception = this.getExceptionError();
        if (exception != null) {
            throw exception;
        }
    }



    abstract public void addParameterToStatement(PreparedStatement statement, int index, Connection connection) throws SQLException;



    public void parameterCalculate(Connection connection) throws SQLException {

    }



    abstract public ParameterException getExceptionError();


    public T getValue() {
        return currentValue;
    }
}
