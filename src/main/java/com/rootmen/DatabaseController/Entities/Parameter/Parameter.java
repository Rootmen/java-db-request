package com.rootmen.DatabaseController.Entities.Parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Parameter {
    private final String ID;                        //ID парметра
    private final String parameterName;             //Имя параметра в SQL запросе
    private final ParametersType parametersType;    //Тип парметра
    private String currentValue = null;             //Текущее значение

    protected Parameter(String ID, String name, ParametersType type) {
        this.ID = ID;
        this.parameterName = name;
        this.parametersType = type;
    }

    protected Parameter(String ID, String name, ParametersType type, String value) {
        this.ID = ID;
        this.parameterName = name;
        this.parametersType = type;
        this.currentValue = value;
    }

    public void updateParameter(String parameter) {
        this.currentValue = parameter;
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

    public void addParameterToStatement(PreparedStatement statement, int index) throws SQLException {
        parametersType.addParameterToStatement(statement, index, this.getValue());
    }
}
