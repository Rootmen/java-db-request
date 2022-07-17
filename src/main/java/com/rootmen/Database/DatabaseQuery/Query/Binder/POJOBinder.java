package com.rootmen.Database.DatabaseQuery.Query.Binder;

import com.rootmen.Database.DatabaseQuery.Query.Binder.Error.BinderError;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface POJOBinder<T> {

    void wrapperResultSet(ResultSet resultSet) throws BinderError, SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
}
