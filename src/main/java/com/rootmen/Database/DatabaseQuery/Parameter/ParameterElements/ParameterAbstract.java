package com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements;


import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.Parameter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public abstract class ParameterAbstract<T> implements Parameter<T> {
    protected String ID;                                      //ID параметра
    protected String parameterName;                           //Имя параметра в SQL запросе
    protected HashMap<String, String> when = new HashMap<>(); //Условия замены

    /**
     * <p>Абстрактный конструктор для параметра запроса</p>.
     *
     * @param ID   Идентификатор параметра.
     * @param name Название параметра в запросе.
     */
    public ParameterAbstract(String ID, String name) throws ParameterException {
        setParameters(ID.trim(), name.trim());
    }

    /**
     * <p>Получение идентификатора параметра</p>.
     *
     * @return идентификатора параметра.
     */
    public String getParameterID() {
        return ID;
    }

    /**
     * <p>Получение названия параметра</p>.
     *
     * @return текстовое название параметра.
     */
    @Override
    public String getParameterName() {
        return parameterName;
    }


    /*
     * ----------------------------------------------------------------------------------------------------
     * ----------------------------------------------------------------------------------------------------
     * Приватные методы для внутренней работы
     * ----------------------------------------------------------------------------------------------------
     * ----------------------------------------------------------------------------------------------------
     */
    protected boolean isCorrectString(String... strings) {
        for (String string : strings) {
            if (string == null || string.equals("")) return false;
        }
        return true;
    }

    private void setParameters(String ID, String name) {
        if (!this.isCorrectString(ID, name)) {
            throw new RuntimeException("Parameter is invalid");
        } else {
            this.ID = ID;
            this.parameterName = name;
        }
    }

    /*
     * ----------------------------------------------------------------------------------------------------
     * ----------------------------------------------------------------------------------------------------
     * Блок абстрактных методов для реализации в потомках
     * ----------------------------------------------------------------------------------------------------
     * ----------------------------------------------------------------------------------------------------
     */
    public abstract void addParameterToStatement(PreparedStatement statement, int index, Connection connection) throws SQLException;

    public abstract ParameterException getExceptionError();

    public abstract T getValue();

    public abstract T parameterCalculate(Connection connection) throws SQLException;

    @Override
    public void setWhen(HashMap<String, String> when) {
        this.when = when;
    }

    @Override
    public HashMap<String, String> getWhen() {
        return this.when;
    }
}
