package com.rootmen.Database.DatabaseQuery.Parameter.Exceptions;

public class ErrorValueType extends Exception {
    Exception origin = null;

    public ErrorValueType(String value, String toConvert, Exception error) {
        super("Error in convert " + value + " to type " + toConvert);
    }
}
