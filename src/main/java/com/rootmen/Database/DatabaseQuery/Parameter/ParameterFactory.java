package com.rootmen.Database.DatabaseQuery.Parameter;

import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ArrayElements.Type.ParameterArrayInteger;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ObjectsElements.Type.ParameterBigInteger;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ObjectsElements.Type.ParameterInteger;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ObjectsElements.Type.ParameterString;

import java.util.ArrayList;


public class ParameterFactory {

    public static Parameter<?> getParameter(String ID, String name, String type, String value) throws ParameterException {
        switch (type.toLowerCase()) {
            case "int":
            case "integer":
                return new ParameterInteger(ID, name, value);
            case "bigint":
            case "biginteger":
                return new ParameterBigInteger(ID, name, value);
            case "str":
            case "string":
                return new ParameterString(ID, name, value);
        }
        throw new RuntimeException("Type " + type + "is not allowed");
    }

    public static Parameter<?> getArrayParameter(String ID, String name, String type, ArrayList<Integer> value) throws ParameterException {
        return new ParameterArrayInteger(ID, name, value, type);
    }
}
