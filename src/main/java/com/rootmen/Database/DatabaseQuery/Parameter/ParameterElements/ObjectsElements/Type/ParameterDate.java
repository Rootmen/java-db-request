package com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ObjectsElements.Type;

import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterExceptionErrorType;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ObjectsElements.ParameterObjects;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;


public class ParameterDate extends ParameterObjects<Date> {


    public ParameterDate(String ID, String name, String value) throws ParameterException {
        this(ID, name, parseString(value));
    }


    public ParameterDate(String ID, String name, Date value) throws ParameterException {
        super(ID, name, value);
    }


    @Override
    public void addParameterToStatement(PreparedStatement statement, int index, Connection connection) throws SQLException {
        statement.setDate(index, new java.sql.Date(this.getValue().getTime()));
    }


    @Override
    public ParameterException getExceptionError() {
        if (this.getValue() == null) {
            return new ParameterExceptionErrorType("null", "Date", new NullPointerException());
        }
        return null;
    }


    static Date parseString(String value) throws ParameterException {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(value);
        } catch (Exception e) {
            throw new ParameterExceptionErrorType(value, "Date", e);
        }
    }
}
