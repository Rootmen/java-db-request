package ru.iedt.database.request.database.controller.binder;

import ru.iedt.database.request.database.controller.binder.error.BinderError;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface POJOBinder<T> {

    void wrapperResultSet(ResultSet resultSet) throws BinderError, SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
}
