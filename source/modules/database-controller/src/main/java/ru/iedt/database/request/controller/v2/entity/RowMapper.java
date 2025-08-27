package ru.iedt.database.request.controller.v2.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.mutiny.sqlclient.Row;
import java.beans.ConstructorProperties;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class RowMapper {
    private static final Logger LOG = LoggerFactory.getLogger(RowMapper.class);
    private static final Map<Class<?>, Function<Row, ?>> mapperCache = new ConcurrentHashMap<>();

    static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    public static <T> Function<Row, T> getMapper(Class<T> dtoClass) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("[RowMapper] Requesting mapper for class: %s", dtoClass.getName()));
        }
        return (Function<Row, T>) mapperCache.computeIfAbsent(dtoClass, RowMapper::createMapper);
    }

    private static <T> Function<Row, T> createMapper(Class<T> dtoClass) {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("[RowMapper:createMapper] Creating mapper for class: %s", dtoClass.getName()));
            }

            Constructor<T> constructor = findAnnotatedConstructor(dtoClass);
            ConstructorProperties properties = constructor.getAnnotation(ConstructorProperties.class);
            String[] paramNames = properties.value();
            Class<?>[] paramTypes = constructor.getParameterTypes();
            Type[] genericTypes = constructor.getGenericParameterTypes();

            // Precompute column names and converters
            String[] columnNames = new String[paramNames.length];
            BiFunction<Row, String, Object>[] converters = new BiFunction[paramTypes.length];

            for (int i = 0; i < paramNames.length; i++) {
                columnNames[i] = camelToSnake(paramNames[i]);
                converters[i] = createConverter(paramTypes[i], genericTypes[i]);

                if (LOG.isTraceEnabled()) {
                    LOG.trace(String.format(
                            "[RowMapper:createMapper] Mapped parameter: %s (type: %s) to column: %s",
                            paramNames[i], paramTypes[i].getSimpleName(), columnNames[i]));
                }
            }

            return row -> {
                Object[] args = new Object[paramNames.length];
                for (int i = 0; i < paramNames.length; i++) {
                    try {
                        args[i] = converters[i].apply(row, columnNames[i]);

                        if (LOG.isTraceEnabled()) {
                            LOG.trace(String.format(
                                    "[RowMapper:row-mapping] Set argument %d: %s = %s (from column: %s)",
                                    i, paramNames[i], args[i], columnNames[i]));
                        }
                    } catch (Exception e) {
                        String errorMsg = String.format(
                                "Error mapping column '%s' for class %s", columnNames[i], dtoClass.getName());
                        LOG.error(errorMsg, e);
                        throw new RuntimeException(errorMsg, e);
                    }
                }
                try {
                    T result = constructor.newInstance(args);

                    if (LOG.isTraceEnabled()) {
                        LOG.trace(String.format("[RowMapper::row-mapping] Created instance: %s", result));
                    }
                    return result;
                } catch (Exception e) {
                    String errorMsg = String.format(
                            "DTO creation failed for: %s with args: %s", dtoClass.getName(), Arrays.toString(args));
                    LOG.error(errorMsg, e);
                    throw new RuntimeException(errorMsg, e);
                }
            };
        } catch (Exception e) {
            String errorMsg = "Mapper initialization failed for: " + dtoClass.getName();
            LOG.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    private static <T> Constructor<T> findAnnotatedConstructor(Class<T> dtoClass) {
        for (Constructor<?> c : dtoClass.getConstructors()) {
            if (c.isAnnotationPresent(ConstructorProperties.class)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format(
                            "[RowMapper] Found @ConstructorProperties in %s with params: %s",
                            dtoClass.getSimpleName(), Arrays.toString(c.getParameterTypes())));
                }
                return (Constructor<T>) c;
            }
        }
        throw new IllegalArgumentException("No @ConstructorProperties found in " + dtoClass.getName());
    }

    private static BiFunction<Row, String, Object> createConverter(Class<?> targetType, Type type) {
        if (targetType == ArrayList.class || targetType == LinkedList.class || targetType == List.class) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format(
                        "[RowMapper] Creating collection converter for type: %s", targetType.getSimpleName()));
            }
            return createConverterArray(targetType, type);
        }

        BiFunction<Row, String, Object> mapperObject = RowTypes.typeFunctionHashMap.get(targetType);
        if (mapperObject == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("[RowMapper] Creating JSON converter for type: %s", targetType.getName()));
            }
            mapperObject = ((row, string) -> {
                try {
                    Object value = mapper.readValue(row.getJson(string).toString(), targetType);

                    if (LOG.isTraceEnabled()) {
                        LOG.trace(String.format(
                                "[RowMapper:converter] Converted column '%s' to %s: %s",
                                string, targetType.getSimpleName(), value));
                    }
                    return value;
                } catch (JsonProcessingException e) {
                    LOG.error(
                            String.format(
                                    "JSON conversion error for column '%s' and type %s", string, targetType.getName()),
                            e);
                    throw new RuntimeException(e);
                }
            });
        }
        return mapperObject;
    }

    private static BiFunction<Row, String, Object> createConverterArray(Class<?> targetType, Type type) {
        if (type instanceof ParameterizedType) {
            Class<?> arrayClazz = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];

            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format(
                        "[RowMapper] Creating array converter for: %s<%s>",
                        targetType.getSimpleName(), arrayClazz.getSimpleName()));
            }

            BiFunction<Row, String, Object[]> convertor = RowTypes.typeArrayFunctionHashMap.get(arrayClazz);
            if (convertor == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format(
                            "[RowMapper] Using JSON array converter for: %s<%s>",
                            targetType.getSimpleName(), arrayClazz.getSimpleName()));
                }
                return ((row, string) -> {
                    JavaType jsonType = mapper.getTypeFactory()
                            .constructCollectionType((Class<? extends Collection>) targetType, arrayClazz);
                    try {
                        Object value = mapper.readValue(row.getJson(string).toString(), jsonType);

                        if (LOG.isTraceEnabled()) {
                            LOG.trace(String.format(
                                    "[RowMapper:converter] Converted column '%s' to %s: %s",
                                    string, jsonType.getTypeName(), value));
                        }
                        return value;
                    } catch (JsonProcessingException e) {
                        LOG.error(
                                String.format(
                                        "JSON array conversion error for column '%s' and type %s",
                                        string, jsonType.getTypeName()),
                                e);
                        throw new RuntimeException(e);
                    }
                });
            }

            // Обработка предопределенных конвертеров
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format(
                        "[RowMapper] Using predefined array converter for: %s<%s>",
                        targetType.getSimpleName(), arrayClazz.getSimpleName()));
            }

            if (targetType == ArrayList.class || targetType == List.class) {
                return convertor.andThen(array -> {
                    if (array == null) return null;
                    return new ArrayList<>(Arrays.stream(array).toList());
                });
            }
            if (targetType == LinkedList.class) {
                return convertor.andThen(array -> {
                    if (array == null) return null;
                    return new LinkedList<>(Arrays.stream(array).toList());
                });
            }
            throw new RuntimeException("Непонятный тип массива из List, ArrayList или LinkedList");
        }
        throw new RuntimeException("ParameterizedType не найден для List");
    }

    private static String camelToSnake(String str) {
        if (str == null || str.isEmpty()) return str;

        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        sb.append(Character.toLowerCase(chars[0]));

        for (int i = 1; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isUpperCase(c)) {
                char prev = chars[i - 1];
                if (!Character.isUpperCase(prev) || (i < chars.length - 1 && Character.isLowerCase(chars[i + 1]))) {
                    sb.append('_');
                }
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
