package ru.iedt.database.request.database.controller.parameter.exceptions;

import java.sql.SQLException;

public class ParameterException extends SQLException {

    public ParameterException(String value) {
        super(value);
    }

}
