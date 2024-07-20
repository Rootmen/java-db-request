package ru.iedt.database.request.structures.nodes.v3.node.parameter;

import static ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterTypes.*;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import ru.iedt.database.request.structures.nodes.v3.Elements;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.array.type.ParameterArrayBigInteger;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.array.type.ParameterArrayBoolean;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.array.type.ParameterArrayInteger;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.array.type.ParameterArrayString;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.type.*;

public class ParameterFactory {

  @SuppressWarnings("unchecked")
  public static <T> Elements.Parameter<?> getParameter(String name, String type, T value)
      throws RuntimeException {
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
      case BOOLEAN_ARRAY:
        return new ParameterArrayBoolean((ArrayList<Boolean>) value, name);
      case BIGINT:
      case "biginteger":
        return new ParameterBigInteger((BigInteger) value, name);
      case "long":
        return new ParameterLong((Long) value, name);
      case STRING:
      case "str":
      case "string":
        return new ParameterString((String) value, name);
      case JSON:
        return new ParameterJson((JsonObject) value, name);
      case JSON_ARRAY:
        return new ParameterJsonArray((JsonArray) value, name);
      case TIMESTAMP:
        return new ParameterLocalDateTime((LocalDateTime) value, name);
      case DATE:
        return new ParameterLocalDate((LocalDate) value, name);
      case NUMERIC:
        return new ParameterNumeric((BigDecimal) value, name);
      case UUID:
        return new ParameterUUID((String) value, name);
      case BOOLEAN:
        return new ParameterBoolean((Boolean) value, name);
    }
    throw new RuntimeException("Type " + type + " is not allowed");
  }

  public static Elements.Parameter<?> getParameter(String name, String type, String value)
      throws RuntimeException {
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
      case BOOLEAN_ARRAY:
        return new ParameterArrayBoolean(generateArray(value, Boolean.class), name);
      case BIGINT:
      case "biginteger":
        return new ParameterBigInteger(value, name);
      case "long":
        return new ParameterLong(value, name);
      case STRING:
      case "str":
      case "string":
        return new ParameterString(value, name);
      case TIMESTAMP:
        return new ParameterLocalDateTime(value, name);
      case DATE:
        return new ParameterLocalDate(value, name);
      case NUMERIC:
        return new ParameterNumeric(value, name);
      case UUID:
        return new ParameterUUID(value, name);
      case JSON:
        return new ParameterJson(value, name);
      case JSON_ARRAY:
        return new ParameterJsonArray(value, name);
      case BOOLEAN:
        return new ParameterBoolean(value, name);
    }
    throw new RuntimeException("Type " + type + " is not allowed");
  }

  @SuppressWarnings("unchecked")
  public static <T> ArrayList<T> generateArray(String array, Class<T> type)
      throws RuntimeException {
    if (type.equals(Integer.class)) {
      return (ArrayList<T>) generateArrayInteger(array);
    } else if (type.equals(BigInteger.class)) {
      return (ArrayList<T>) generateArrayBigInteger(array);
    }

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

  public static ArrayList<Integer> generateArrayInteger(String array) throws RuntimeException {
    ArrayList<Integer> arrayString = new ArrayList<>();
    if (array != null && !array.isEmpty()) {
      String[] values = array.split(",");
      for (String value : values) {
        arrayString.add(Integer.parseInt(value.trim()));
      }
    }
    return arrayString;
  }

  public static ArrayList<BigInteger> generateArrayBigInteger(String array)
      throws RuntimeException {
    ArrayList<BigInteger> arrayString = new ArrayList<>();
    if (array != null && !array.isEmpty()) {
      String[] values = array.split(",");
      for (String value : values) {
        arrayString.add(new BigInteger(value.trim()));
      }
    }
    return arrayString;
  }
}
