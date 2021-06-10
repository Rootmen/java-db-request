package com.rootmen.DatabaseController.Entities.Parameter.ParameterClasses;


import com.rootmen.DatabaseController.Entities.Parameter.ParameterFactory;
import com.rootmen.DatabaseController.Utils.Databse.DatabaseMethods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class Parameter {
    private String ID;                      //ID парметра
    private String parameterName;           //Имя параметра в SQL запросе
    private ParameterType parameterType;    //Тип парметра
    private ParameterWhen parameterWhen;    //Тип парметра
    private String currentValue = null;     //Текущее значение

    public Parameter(ParameterPOJO parameter) {
        this.parseParameterPOJO(parameter, null, null);
    }

    public Parameter(ParameterPOJO parameter, Connection connection, HashMap<String, Parameter> parameters) {
        this.parseParameterPOJO(parameter, connection, parameters);
    }

    protected void parseParameterPOJO(ParameterPOJO parameter, Connection connection, HashMap<String, Parameter> parameters) {
        if (!parameter.isValid()) {
            throw new RuntimeException("Parameter POJO config is invalid");
        }
        ParameterType parameterType = ParameterFactory.searchParameterType(parameter.type);
        if (parameterType == null) {
            throw new RuntimeException("Parameter type is not valid");
        }
        if (parameter.raw != null) {
            setParameters(parameter.id.trim(), parameter.name.trim(), parameterType, parameter.getParameterWhen(), parameter.raw.trim());
        } else if (parameter.query != null && connection != null) {
            try {
                if (parameters == null) {
                    parameters = new HashMap<>();
                }
                String value = DatabaseMethods.getText(DatabaseMethods.generatedPreparedStatement(parameter.query, connection, parameters)).trim();
                setParameters(parameter.id.trim(), parameter.name.trim(), parameterType, parameter.getParameterWhen(), value.trim());
            } catch (SQLException error) {
                error.printStackTrace();
                throw new RuntimeException("Error in create parameter " + parameter.name + " query execute error in POJO parser SQL error");
            }

        }
    }

    public Parameter(String ID, String name, ParameterType type, String value) {
        setParameters(ID.trim(), name.trim(), type, new ParameterWhen(), value.trim());
    }

    public Parameter(String ID, String name, ParameterType type, ParameterWhen when, String value) {
        setParameters(ID.trim(), name.trim(), type, when, value.trim());
    }

    public Parameter(String ID, String name, ParameterType type, String query, Connection connection, HashMap<String, Parameter> parameters) {
        try {
            String value = DatabaseMethods.getText(DatabaseMethods.generatedPreparedStatement(query, connection, parameters)).trim();
            setParameters(ID.trim(), name.trim(), type, new ParameterWhen(), value.trim());
        } catch (SQLException error) {
            error.printStackTrace();
            throw new RuntimeException("Error in create parameter " + name + " query execute error");
        }
    }

    public Parameter(String ID, String name, ParameterType type, ParameterWhen when, String query, Connection connection, HashMap<String, Parameter> parameters) {
        try {
            String value = DatabaseMethods.getText(DatabaseMethods.generatedPreparedStatement(query, connection, parameters)).trim();
            setParameters(ID.trim(), name.trim(), type, when, value.trim());
        } catch (SQLException error) {
            error.printStackTrace();
            throw new RuntimeException("Error in create parameter " + name + " query execute error\n" + error.getSQLState() + "\n" + error.getMessage());
        }
    }

    private void setParameters(String ID, String name, ParameterType type, ParameterWhen when, String value) {
        if ((ID == null || ID.equals("")) || (name == null || name.equals("")) || (value == null) || (type == null) || (when == null)) {
            throw new RuntimeException("Parameter is invalid");
        } else {
            this.ID = ID;
            this.parameterName = name;
            this.parameterWhen = when;
            this.parameterType = type;
            this.currentValue = value;
        }
    }


    public void updateParameter(String parameter) {
        String when = parameterWhen.getNewValue(parameter);
        this.currentValue = (when == null) ? parameter : when;
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
        if (parameterType == null) {
            throw new RuntimeException("In run SQL parameter dont have initialize type");
        }
        parameterType.addParameterToStatement(statement, index, this.getValue());
    }

}
