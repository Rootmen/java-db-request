package com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements;


import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.Parameter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class ParameterAbstract implements Parameter {
    private String ID;                                  //ID параметра
    private String parameterName;                       //Имя параметра в SQL запросе

    public ParameterAbstract(String ID, String name) throws ParameterException {
        setParameters(ID.trim(), name.trim());
    }


    private void setParameters(String ID, String name) {
        if ((ID == null || ID.equals("")) || (name == null || name.equals(""))) {
            throw new RuntimeException("Parameter is invalid");
        } else {
            this.ID = ID;
            this.parameterName = name;
        }
    }


    public String getID() {
        return ID;
    }


    public String getParameterName() {
        return parameterName;
    }


    public abstract void addParameterToStatement(PreparedStatement statement, int index, Connection connection) throws SQLException;

    public abstract ParameterException getExceptionError();
}
