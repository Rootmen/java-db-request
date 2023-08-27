package ru.iedt.database.request.database.controller.parameter.parameter.elements.objects.elements.type;

import ru.iedt.database.request.database.controller.parameter.exceptions.ParameterException;
import ru.iedt.database.request.database.controller.parameter.exceptions.ParameterExceptionErrorType;
import ru.iedt.database.request.database.controller.parameter.parameter.elements.objects.elements.ParameterObjects;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ParameterLong extends ParameterObjects<Long> {


    public ParameterLong(String ID, String name, String value) throws ParameterException {
        this(ID, name, parseString(value));
    }


    public ParameterLong(String ID, String name, Long value) throws ParameterException {
        super(ID, name, value);
    }


    @Override
    public void addParameterToStatement(PreparedStatement statement, int index, Connection connection) throws SQLException {
        statement.setLong(index, this.getValue());
    }


    @Override
    public ParameterException getExceptionError() {
        if (this.getValue() == null) {
            return new ParameterExceptionErrorType("null", "BigInteger", new NullPointerException());
        }
        return null;
    }


    static Long parseString(String integer) throws ParameterException {
        try {
            return new BigInteger(integer).longValue();
        } catch (Exception e) {
            throw new ParameterExceptionErrorType(integer, "BigInteger", e);
        }
    }
}
