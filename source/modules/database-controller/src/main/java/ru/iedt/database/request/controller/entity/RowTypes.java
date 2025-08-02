package ru.iedt.database.request.controller.entity;

import io.vertx.mutiny.sqlclient.Row;
import io.vertx.sqlclient.data.Numeric;
import java.math.BigDecimal;
import java.nio.Buffer;
import java.time.*;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.function.BiFunction;

public class RowTypes {
    public static final Map<Class<?>, Boolean> primitiveMap;

    static {
        primitiveMap = Map.ofEntries(
                Map.entry(int.class, true),
                Map.entry(double.class, true),
                Map.entry(long.class, true),
                Map.entry(boolean.class, true),
                Map.entry(short.class, true),
                Map.entry(float.class, true),
                Map.entry(int[].class, true),
                Map.entry(double[].class, true),
                Map.entry(long[].class, true),
                Map.entry(boolean[].class, true),
                Map.entry(short[].class, true),
                Map.entry(float[].class, true));
    }

    public static Map<Class<?>, BiFunction<Row, String, Object>> typeFunctionHashMap;

    static {
        typeFunctionHashMap = Map.ofEntries(
                Map.entry(Integer.class, Row::getInteger),
                Map.entry(Double.class, Row::getDouble),
                Map.entry(String.class, Row::getString),
                Map.entry(Long.class, Row::getLong),
                Map.entry(Boolean.class, Row::getBoolean),
                Map.entry(Short.class, Row::getShort),
                Map.entry(Float.class, Row::getFloat),
                Map.entry(Buffer.class, Row::getBuffer),
                Map.entry(Numeric.class, Row::getBuffer),
                Map.entry(Temporal.class, Row::getTemporal),
                Map.entry(LocalDate.class, Row::getLocalDate),
                Map.entry(LocalTime.class, Row::getLocalTime),
                Map.entry(LocalDateTime.class, Row::getLocalDateTime),
                Map.entry(OffsetTime.class, Row::getOffsetTime),
                Map.entry(OffsetDateTime.class, Row::getOffsetDateTime),
                Map.entry(UUID.class, Row::getUUID),
                Map.entry(BigDecimal.class, Row::getBigDecimal),
                Map.entry(Boolean[].class, Row::getArrayOfBooleans),
                Map.entry(Short[].class, Row::getArrayOfShorts),
                Map.entry(Integer[].class, Row::getArrayOfIntegers),
                Map.entry(Long[].class, Row::getArrayOfLongs),
                Map.entry(Float[].class, Row::getArrayOfFloats),
                Map.entry(Double[].class, Row::getArrayOfDoubles),
                Map.entry(Numeric[].class, Row::getArrayOfNumerics),
                Map.entry(String[].class, Row::getArrayOfStrings),
                Map.entry(Temporal[].class, Row::getArrayOfTemporals),
                Map.entry(LocalDate[].class, Row::getArrayOfLocalDates),
                Map.entry(LocalTime[].class, Row::getArrayOfLocalTimes),
                Map.entry(LocalDateTime[].class, Row::getArrayOfLocalDateTimes),
                Map.entry(OffsetTime[].class, Row::getArrayOfOffsetTimes),
                Map.entry(OffsetDateTime[].class, Row::getArrayOfOffsetDateTimes),
                Map.entry(UUID[].class, Row::getArrayOfUUIDs),
                Map.entry(BigDecimal[].class, Row::getArrayOfBigDecimals));
    }

    public static Map<Class<?>, BiFunction<Row, String, Object[]>> typeArrayFunctionHashMap;

    static {
        typeArrayFunctionHashMap = Map.ofEntries(
                Map.entry(Boolean.class, Row::getArrayOfBooleans),
                Map.entry(Short.class, Row::getArrayOfShorts),
                Map.entry(Integer.class, Row::getArrayOfIntegers),
                Map.entry(Long.class, Row::getArrayOfLongs),
                Map.entry(Float.class, Row::getArrayOfFloats),
                Map.entry(Double.class, Row::getArrayOfDoubles),
                Map.entry(Numeric.class, Row::getArrayOfNumerics),
                Map.entry(String.class, Row::getArrayOfStrings),
                Map.entry(Temporal.class, Row::getArrayOfTemporals),
                Map.entry(LocalDate.class, Row::getArrayOfLocalDates),
                Map.entry(LocalTime.class, Row::getArrayOfLocalTimes),
                Map.entry(LocalDateTime.class, Row::getArrayOfLocalDateTimes),
                Map.entry(OffsetTime.class, Row::getArrayOfOffsetTimes),
                Map.entry(OffsetDateTime.class, Row::getArrayOfOffsetDateTimes),
                Map.entry(UUID.class, Row::getArrayOfUUIDs),
                Map.entry(BigDecimal.class, Row::getArrayOfBigDecimals));
    }
}
