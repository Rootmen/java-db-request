package com.rootmen.Database.DatabaseQuery.Parameter;


import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ErrorValueType;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Parameter {
    private String ID;                      //ID параметра
    private String parameterName;           //Имя параметра в SQL запросе
    private String currentValue = null;     //Текущее значение

    public Parameter(String ID, String name, String value) {
        setParameters(ID.trim(), name.trim(), value.trim());
        Exception exception = this.valueVerification();
        if (exception) {
            throw exception;
        }
    }

    private void setParameters(String ID, String name, String value) {
        if ((ID == null || ID.equals("")) || (name == null || name.equals("")) || (value == null)) {
            throw new RuntimeException("Parameter is invalid");
        } else {
            this.ID = ID;
            this.parameterName = name;
            this.currentValue = value;
        }
    }


    public String getID() {
        return ID;
    }


    public String getName() {
        return parameterName;
    }


    public String getValue() {
        return currentValue;
    }


    public abstract void addParameterToStatement(PreparedStatement statement, int index) throws SQLException;

    public abstract Exception valueVerification();
}
