package com.rootmen.Database.DatabaseQuery.Parameter;

import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ArrayElements.Type.ParameterArrayInteger;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ObjectsElements.Type.*;

import java.util.ArrayList;


public class ParameterFactory {

    public static Parameter<?> getParameter(String ID, String name, String type, String value) throws ParameterException {
        switch (type.toLowerCase()) {
            case "int":
            case "integer":
                return new ParameterInteger(ID, name, value);
            case "int_array":
            case "integer_array":
                ArrayList<Integer> array = new ArrayList<>();
                if (value != null && !value.equals("")) {
                    for (String item : value.split(",")) {
                        array.add(Integer.parseInt(item));
                    }
                }
                return new ParameterArrayInteger(ID, name, array, "int");
            case "bigint":
            case "biginteger":
                return new ParameterBigInteger(ID, name, value);
            case "long":
                return new ParameterLong(ID, name, value);
            case "str":
            case "string":
                return new ParameterString(ID, name, value);
            case "date":
                return new ParameterDate(ID, name, value);
            case "numeric":
                return new ParameterNumeric(ID, name, value);
        }
        throw new RuntimeException("Type " + type + "is not allowed");
    }

    public static Parameter<?> getArrayParameter(String ID, String name, String type, ArrayList<Integer> value) throws ParameterException {
        return new ParameterArrayInteger(ID, name, value, type);
    }
}
