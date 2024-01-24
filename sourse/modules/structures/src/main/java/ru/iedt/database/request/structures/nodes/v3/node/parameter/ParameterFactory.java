package ru.iedt.database.request.structures.nodes.v3.node.parameter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import ru.iedt.database.request.structures.nodes.v3.Elements;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.array.type.ParameterArrayBigInteger;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.array.type.ParameterArrayInteger;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.array.type.ParameterArrayString;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.type.*;

import static ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterTypes.*;

public class ParameterFactory {

    public static <T> Elements.Parameter<?> getParameter(String name, String type, T value) throws RuntimeException {
        switch (type.toLowerCase()) {
            case INTEGER:
            case "integer":
                return new ParameterInteger((Integer) value, name);
            case INTEGER_ARRAY:
            case "int_array":
            case "integer_array":
                return new ParameterArrayInteger((ArrayList<Integer>) value, name);
            case BIGINT_ARRAY:
            case "bigint_array":
            case "biginteger_array":
                return new ParameterArrayBigInteger((ArrayList<BigInteger>) value, name);
            case STRING_ARRAY:
            case "str_array":
            case "string_array":
                return new ParameterArrayString((ArrayList<String>) value, name);
            case BIGINT:
            case "biginteger":
                return new ParameterBigInteger((BigInteger) value, name);
            case "long":
                return new ParameterLong((Long) value, name);
            case STRING:
            case "str":
            case "string":
                return new ParameterString((String) value, name);
            case DATE:
                return new ParameterLocalDateTime((LocalDateTime) value, name);
            case NUMERIC:
                return new ParameterNumeric((BigDecimal) value, name);
            case UUID:
                return new ParameterUUID((String) value, name);
        }
        throw new RuntimeException("Type " + type + " is not allowed");
    }

    public static Elements.Parameter<?> getParameter(String name, String type, String value) throws RuntimeException {
        switch (type.toLowerCase()) {
            case INTEGER:
            case "integer":
                return new ParameterInteger(value, name);
            case INTEGER_ARRAY:
            case "int_array":
            case "integer_array":
                return new ParameterArrayInteger(generateArray(value, Integer.class), name);
            case BIGINT_ARRAY:
            case "bigint_array":
            case "biginteger_array":
                return new ParameterArrayBigInteger(generateArray(value, BigInteger.class), name);
            case STRING_ARRAY:
            case "str_array":
            case "string_array":
                return new ParameterArrayString(generateArray(value, String.class), name);
            case BIGINT:
            case "biginteger":
                return new ParameterBigInteger(value, name);
            case "long":
                return new ParameterLong(value, name);
            case STRING:
            case "str":
            case "string":
                return new ParameterString(value, name);
            case DATE:
                return new ParameterLocalDateTime(value, name);
            case NUMERIC:
                return new ParameterNumeric(value, name);
            case UUID:
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
