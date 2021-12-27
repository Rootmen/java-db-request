package com.rootmen.Database.DatabaseQuery.Parameter.Exceptions;

import java.sql.SQLException;

public class ParameterException extends SQLException {

    public ParameterException(String value) {
        super(value);
    }

}
