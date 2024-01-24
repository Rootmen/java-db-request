package ru.iedt.database.request.structures.nodes.v3.node.parameter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import ru.iedt.database.request.structures.nodes.v3.Elements;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.array.type.ParameterArrayBigInteger;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.array.type.ParameterArrayInteger;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.array.type.ParameterArrayString;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.type.*;

public class ParameterFactory {

    public static Elements.Parameter<?> getParameter(String name, String type, String value) throws RuntimeException {
        switch (type.toLowerCase()) {
            case "int":
            case "integer":
                return new ParameterInteger(value, name);
            case "int_array":
            case "integer_array":
                return new ParameterArrayInteger(generateArray(value, Integer.class), name);
            case "bigint_array":
            case "biginteger_array":
                return new ParameterArrayBigInteger(generateArray(value, BigInteger.class), name);
            case "str_array":
            case "string_array":
                return new ParameterArrayString(generateArray(value, String.class), name);
            case "bigint":
            case "biginteger":
                return new ParameterBigInteger(value, name);
            case "long":
                return new ParameterLong(value, name);
            case "text":
            case "str":
            case "string":
                return new ParameterString(value, name);
            case "date":
                return new ParameterLocalDateTime(value, name);
            case "numeric":
                return new ParameterNumeric(value, name);
            case "uuid":
                return new ParameterUUID(value, name);
        }
        throw new RuntimeException("Type " + type + " is not allowed");
    }

    public static <T> ArrayList<T> generateArray(String array, Class<T> type) throws RuntimeException {
        try {
            ArrayList<T> arrayString = new ArrayList<>();
            if (array != null && !array.isEmpty()) {
                Constructor<T> constr = type.getConstructor(String.class);
                String[] values = array.split(",");
                for (String value : values) {
                    arrayString.add(constr.newInstance(value));
                }
            }
            return arrayString;
        } catch (NoSuchMethodException
                | InvocationTargetException
                | InstantiationException
                | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
