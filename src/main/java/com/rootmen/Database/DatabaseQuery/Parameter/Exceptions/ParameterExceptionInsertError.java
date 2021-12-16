package com.rootmen.Database.DatabaseQuery.Parameter.Exceptions;

public class ParameterExceptionInsertError extends ParameterException {
    Exception origin = null;

    public ParameterExceptionInsertError(String value) {
        super(value);
    }
}