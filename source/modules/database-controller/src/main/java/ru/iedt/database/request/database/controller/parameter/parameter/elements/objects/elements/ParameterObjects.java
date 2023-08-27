package ru.iedt.database.request.database.controller.parameter.parameter.elements.objects.elements;

import ru.iedt.database.request.database.controller.parameter.exceptions.ParameterException;
import ru.iedt.database.request.database.controller.parameter.parameter.elements.ParameterAbstract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

abstract public class ParameterObjects<T> extends ParameterAbstract<T> {

    protected T currentValue;


    public ParameterObjects(String ID, String name, T value) throws ParameterException {
        super(ID, name);
        this.currentValue = value;
        ParameterException exception = this.getExceptionError();
        if (exception != null) {
            throw exception;
        }
    }


    abstract public void addParameterToStatement(PreparedStatement statement, int index, Connection connection) throws SQLException;


    public T parameterCalculate(Connection connection) throws SQLException {
        return currentValue;
    }


    abstract public ParameterException getExceptionError();

    @Override
    public T getValue() {
        return currentValue;
    }

}
