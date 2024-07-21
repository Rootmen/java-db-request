package ru.iedt.database.request.controller.entity;

import io.vertx.mutiny.sqlclient.Row;
import io.vertx.sqlclient.data.Numeric;
import java.math.BigDecimal;
import java.nio.Buffer;
import java.time.*;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.BiFunction;

public class RowTypes {
  static HashMap<Class<?>, Boolean> primitiveMap = new HashMap<>();

  static {
    primitiveMap.put(int.class, true);
    primitiveMap.put(double.class, true);
    primitiveMap.put(long.class, true);
    primitiveMap.put(boolean.class, true);
    primitiveMap.put(short.class, true);
    primitiveMap.put(float.class, true);
    primitiveMap.put(int[].class, true);
    primitiveMap.put(double[].class, true);
    primitiveMap.put(long[].class, true);
    primitiveMap.put(boolean[].class, true);
    primitiveMap.put(short[].class, true);
    primitiveMap.put(float[].class, true);
  }

  static HashMap<Class<?>, BiFunction<Row, String, Object>> typeFunctionHashMap = new HashMap<>();

  static {
    typeFunctionHashMap.put(Integer.class, Row::getInteger);
    typeFunctionHashMap.put(Double.class, Row::getDouble);
    typeFunctionHashMap.put(String.class, Row::getString);
    typeFunctionHashMap.put(Long.class, Row::getLong);
    typeFunctionHashMap.put(Boolean.class, Row::getBoolean);
    typeFunctionHashMap.put(Short.class, Row::getShort);
    typeFunctionHashMap.put(Float.class, Row::getFloat);
    typeFunctionHashMap.put(Buffer.class, Row::getBuffer);
    typeFunctionHashMap.put(Numeric.class, Row::getBuffer);
    typeFunctionHashMap.put(Temporal.class, Row::getTemporal);
    typeFunctionHashMap.put(LocalDate.class, Row::getLocalDate);
    typeFunctionHashMap.put(LocalTime.class, Row::getLocalTime);
    typeFunctionHashMap.put(LocalDateTime.class, Row::getLocalDateTime);
    typeFunctionHashMap.put(OffsetTime.class, Row::getOffsetTime);
    typeFunctionHashMap.put(OffsetDateTime.class, Row::getOffsetDateTime);
    typeFunctionHashMap.put(UUID.class, Row::getUUID);
    typeFunctionHashMap.put(BigDecimal.class, Row::getBigDecimal);
    typeFunctionHashMap.put(Boolean[].class, Row::getArrayOfBooleans);
    typeFunctionHashMap.put(Short[].class, Row::getArrayOfShorts);
    typeFunctionHashMap.put(Integer[].class, Row::getArrayOfIntegers);
    typeFunctionHashMap.put(Long[].class, Row::getArrayOfLongs);
    typeFunctionHashMap.put(Float[].class, Row::getArrayOfFloats);
    typeFunctionHashMap.put(Double[].class, Row::getArrayOfDoubles);
    typeFunctionHashMap.put(Numeric[].class, Row::getArrayOfNumerics);
    typeFunctionHashMap.put(String[].class, Row::getArrayOfStrings);
    typeFunctionHashMap.put(Temporal[].class, Row::getArrayOfTemporals);
    typeFunctionHashMap.put(LocalDate[].class, Row::getArrayOfLocalDates);
    typeFunctionHashMap.put(LocalTime[].class, Row::getArrayOfLocalTimes);
    typeFunctionHashMap.put(LocalDateTime[].class, Row::getArrayOfLocalDateTimes);
    typeFunctionHashMap.put(OffsetTime[].class, Row::getArrayOfOffsetTimes);
    typeFunctionHashMap.put(OffsetDateTime[].class, Row::getArrayOfOffsetDateTimes);
    typeFunctionHashMap.put(UUID[].class, Row::getArrayOfUUIDs);
    typeFunctionHashMap.put(BigDecimal[].class, Row::getArrayOfBigDecimals);
  }

  static HashMap<Class<?>, BiFunction<Row, String, Object[]>> typeArrayFunctionHashMap =
      new HashMap<>();

  static {
    typeArrayFunctionHashMap.put(Boolean.class, Row::getArrayOfBooleans);
    typeArrayFunctionHashMap.put(Short.class, Row::getArrayOfShorts);
    typeArrayFunctionHashMap.put(Integer.class, Row::getArrayOfIntegers);
    typeArrayFunctionHashMap.put(Long.class, Row::getArrayOfLongs);
    typeArrayFunctionHashMap.put(Float.class, Row::getArrayOfFloats);
    typeArrayFunctionHashMap.put(Double.class, Row::getArrayOfDoubles);
    typeArrayFunctionHashMap.put(Numeric.class, Row::getArrayOfNumerics);
    typeArrayFunctionHashMap.put(String.class, Row::getArrayOfStrings);
    typeArrayFunctionHashMap.put(Temporal.class, Row::getArrayOfTemporals);
    typeArrayFunctionHashMap.put(LocalDate.class, Row::getArrayOfLocalDates);
    typeArrayFunctionHashMap.put(LocalTime.class, Row::getArrayOfLocalTimes);
    typeArrayFunctionHashMap.put(LocalDateTime.class, Row::getArrayOfLocalDateTimes);
    typeArrayFunctionHashMap.put(OffsetTime.class, Row::getArrayOfOffsetTimes);
    typeArrayFunctionHashMap.put(OffsetDateTime.class, Row::getArrayOfOffsetDateTimes);
    typeArrayFunctionHashMap.put(UUID.class, Row::getArrayOfUUIDs);
    typeArrayFunctionHashMap.put(BigDecimal.class, Row::getArrayOfBigDecimals);
  }
}
