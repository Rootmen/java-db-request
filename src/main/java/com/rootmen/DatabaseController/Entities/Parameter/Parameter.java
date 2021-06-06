package com.rootmen.DatabaseController.Entities.Parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Parameter {
    private final String ID;                        //ID ��������
    private final String parameterName;             //��� ��������� � SQL �������
    private final ParametersType parametersType;    //��� ��������
    private String currentValue = null;             //������� ��������

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
