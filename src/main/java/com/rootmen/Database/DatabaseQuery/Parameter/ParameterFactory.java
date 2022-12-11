package com.rootmen.Database.DatabaseQuery.Parameter;

import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterExceptionErrorType;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ArrayElements.Type.ParameterArrayBigInteger;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ArrayElements.Type.ParameterArrayInteger;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ArrayElements.Type.ParameterArrayString;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ObjectsElements.Type.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;


public class ParameterFactory {

    public static Parameter<?> getParameter(String ID, String name, String type, String value) throws ParameterException {
        switch (type.toLowerCase()) {
            case "int":
            case "integer":
                return new ParameterInteger(ID, name, value);
            case "int_array":
            case "integer_array":
                return new ParameterArrayInteger(ID, name, generateArray(value, 0));
            case "bigint_array":
            case "biginteger_array":
                return new ParameterArrayBigInteger(ID, name, generateArray(value, new BigInteger("0")));
            case "str_array":
            case "string_array":
                return new ParameterArrayString(ID, name, generateArray(value, ""));
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

    public static <T> ArrayList<T> generateArray(String array, T type) throws ParameterException {
        try {
            ArrayList<T> arrayString = new ArrayList<>();
            if (array != null && !array.equals("")) {
                Constructor<T> constr = (Constructor<T>) type.getClass().getConstructor(String.class);
                String[] values = array.split(",");
                for (String value : values) {
                    arrayString.add(constr.newInstance(value));
                }
            }
            return arrayString;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ParameterExceptionErrorType(array, type.getClass().toString(), e);
        }
    }

}
