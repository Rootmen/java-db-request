package com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ObjectsElements.Type;

import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterExceptionErrorType;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ObjectsElements.ParameterObjects;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ParameterNumeric extends ParameterObjects<BigDecimal> {


    public ParameterNumeric(String ID, String name, String value) throws ParameterException {
        this(ID, name, parseString(value));
    }


    public ParameterNumeric(String ID, String name, BigDecimal value) throws ParameterException {
        super(ID, name, value);
    }


    @Override
    public void addParameterToStatement(PreparedStatement statement, int index, Connection connection) throws SQLException {
        statement.setBigDecimal(index, this.getValue());
    }


    @Override
    public ParameterException getExceptionError() {
        if (this.getValue() == null) {
            return new ParameterExceptionErrorType("null", "BigDecimal", new NullPointerException());
        }
        return null;
    }


    static BigDecimal parseString(String value) throws ParameterException {
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            throw new ParameterExceptionErrorType(value, "BigDecimal", e);
        }
    }
}
