package com.rootmen.DatabaseController.Entities.Parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Parameter {
    private final String ID;                        //ID ��������
    private final String parameterType;             //��� ��������
    private final String parameterName;             //��� ��������� � SQL �������
    private final ParametersType parametersType;    //��� ��������� � SQL �������
    private String currentValue = null;             //������� ��������

    protected Parameter(String ID, String name, String typeName, ParametersType type) {
        this.ID = ID;
        this.parameterType = typeName;
        this.parameterName = name;
        this.parametersType = type;
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

    public String getType() {
        return parameterType;
    }

    public void addParameterToStatement(PreparedStatement statement, int index) throws SQLException {
        parametersType.addParameterToStatement(statement, index, this.getValue());
    }
}
