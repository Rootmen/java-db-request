package com.rootmen.Database.DatabaseQuery.Parameter.Exceptions;

public class ParameterExceptionErrorType extends ParameterException {
    Exception origin = null;

    public ParameterExceptionErrorType(String value, String toConvert, Exception error) {
        super("Error in convert " + value + " to type " + toConvert);
    }
}