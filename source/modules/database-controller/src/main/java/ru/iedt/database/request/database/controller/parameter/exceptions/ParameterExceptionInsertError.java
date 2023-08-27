package ru.iedt.database.request.database.controller.parameter.exceptions;

public class ParameterExceptionInsertError extends ParameterException {
    Exception origin = null;

    public ParameterExceptionInsertError(String value) {
        super(value);
    }
}