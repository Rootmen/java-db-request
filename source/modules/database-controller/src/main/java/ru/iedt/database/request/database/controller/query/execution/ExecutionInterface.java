package ru.iedt.database.request.database.controller.query.execution;

import java.sql.ResultSet;

abstract public class ExecutionInterface<T, F, D> {
    abstract public T getNextRow(ResultSet resultSet, F input);


    abstract public static class ExecutionInterfaceReturn<T, F, D> {
        abstract public void addResult(T result);
        abstract public D getResult();

        abstract public boolean isNeedSaveResult();
    }
}
