package com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ObjectsElements.Type;

import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterExceptionErrorType;import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ObjectsElements.ParameterObjects;import java.math.BigInteger;import java.sql.Connection;import java.sql.PreparedStatement;import java.sql.SQLException;


public class ParameterBigInteger extends ParameterObjects<BigInteger> {


    public ParameterBigInteger(String ID, String name, String value) throws ParameterException {
        this(ID, name, parseString(value));
    }


    public ParameterBigInteger(String ID, String name, BigInteger value) throws ParameterException {
        super(ID, name, value);
    }


    @Override
    public void addParameterToStatement(PreparedStatement statement, int index, Connection connection) throws SQLException {
        statement.setLong(index, this.getValue().longValueExact());
    }


    @Override
    public ParameterException getExceptionError() {
        if (this.getValue() == null) {
            return new ParameterExceptionErrorType("null", "BigInteger", new NullPointerException());
        }
        return null;
    }


    static BigInteger parseString(String integer) throws ParameterException {
        try {
            return new BigInteger(integer);
        } catch (Exception e) {
            throw new ParameterExceptionErrorType(integer, "BigInteger", e);
        }
    }
}
