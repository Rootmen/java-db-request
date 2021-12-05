package com.rootmen.Database.DatabaseQuery.Parameter;

import com.rootmen.Database.DatabaseQuery.Parameter.ParameterTypes.ParameterInteger;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterTypes.ParameterString;


public class ParameterFactory {

    public static Parameter getParameter(String ID, String name, String type, String value) {
        switch (type.toLowerCase()) {
            case "int":
            case "integer":
                return new ParameterInteger(ID, name, value);
            case "str":
            case "string":
                return new ParameterString(ID, name, value);
        }
        throw new RuntimeException("Type " + type + "is not allowed");
    }
}
