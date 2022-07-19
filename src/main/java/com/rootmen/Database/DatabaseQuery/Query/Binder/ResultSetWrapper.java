package com.rootmen.Database.DatabaseQuery.Query.Binder;

import com.rootmen.Database.DatabaseQuery.Query.Binder.Error.Types.ExceptionDuplicateAnnotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

abstract public class ResultSetWrapper<T> implements POJOBinder<T> {

    public final static Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();

    static {
        map.put(boolean.class, Boolean.class);
        map.put(byte.class, Byte.class);
        map.put(short.class, Short.class);
        map.put(char.class, Character.class);
        map.put(int.class, Integer.class);
        map.put(long.class, Long.class);
        map.put(float.class, Float.class);
        map.put(double.class, Double.class);
    }

    public final static Map<Class<?>, Class<?>> mapNumber = new HashMap<Class<?>, Class<?>>();

    static {
        mapNumber.put(int.class, Integer.class);
        mapNumber.put(long.class, Long.class);
        mapNumber.put(float.class, Float.class);
        mapNumber.put(double.class, Double.class);
    }

    @Override
    public void wrapperResultSet(ResultSet resultSet) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ExceptionDuplicateAnnotation {
        final Collection<FieldData> annotationParameters = this.getAnnotationParameters();
        for (FieldData fieldData : annotationParameters) {
            Field field = fieldData.field;
            field.setAccessible(true);
            String name = fieldData.column;
            String value = resultSet.getString(name);
            if (value == null) {
                continue;
            }
            value = value.trim();
            try {
                Class<?> type = field.getType();
                if (mapNumber.containsKey(type)) {
                    value = value.replaceAll(",", ".");
                }
                if (type.isPrimitive()) {
                    field.set(this, map.get(type).getConstructor(String.class).newInstance(value));
                } else {
                    field.set(this, type.getConstructor(String.class).newInstance(value));
                }
            } catch (Exception e) {
                System.err.println("Поле " + name + " не удалось привязать к значению");
            }
        }
    }

    /**
     * Функция получения списка параметров с аннотацией {@link ParameterAnnotation}
     *
     * @return возвращает {@link HashMap<String,Field>} со списком парамеров.
     */
    private Collection<FieldData> getAnnotationParameters() throws ExceptionDuplicateAnnotation {
        HashMap<String, FieldData> annotationParameters = new HashMap<>();
        Class<? extends ResultSetWrapper<T>> clazz = (Class<? extends ResultSetWrapper<T>>) this.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ParameterAnnotation.class)) {
                ParameterAnnotation parameterAnnotation = field.getAnnotation(ParameterAnnotation.class);
                String column = parameterAnnotation.column();
                if (annotationParameters.containsKey(column)) {
                    throw new ExceptionDuplicateAnnotation(column, clazz.getName());
                }
                annotationParameters.put(column, new FieldData(column, field));
            }
        }
        return annotationParameters.values();
    }

    private static class FieldData {
        String column;
        Field field;

        public FieldData(String column, Field field) {
            this.column = column;
            this.field = field;
        }
    }

}

//package com.rootmen.Database.DatabaseQuery.Query.Binder;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.rootmen.Database.DatabaseQuery.Query.Binder.Error.Types.ExceptionDuplicateAnnotation;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.sql.*;
//import java.util.HashMap;
//import java.util.Map;
//
//abstract public class ResultSetWrapper<T> implements POJOBinder<T> {
//
//    public final static Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();
//
//    static {
//        map.put(boolean.class, Boolean.class);
//        map.put(byte.class, Byte.class);
//        map.put(short.class, Short.class);
//        map.put(char.class, Character.class);
//        map.put(int.class, Integer.class);
//        map.put(long.class, Long.class);
//        map.put(float.class, Float.class);
//        map.put(double.class, Double.class);
//    }
//
//    public final static Map<Class<?>, Class<?>> mapNumber = new HashMap<Class<?>, Class<?>>();
//
//    static {
//        mapNumber.put(int.class, Integer.class);
//        mapNumber.put(long.class, Long.class);
//        mapNumber.put(float.class, Float.class);
//        mapNumber.put(double.class, Double.class);
//    }
//
//    @Override
//    public void wrapperResultSet(ResultSet resultSet) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ExceptionDuplicateAnnotation {
//        final HashMap<String, FieldData> annotationParameters = this.getAnnotationParameters();
//        ResultSetMetaData metadata = resultSet.getMetaData();
//        int numColumns = metadata.getColumnCount();
//        for (int i = 1; i < numColumns + 1; i++) {
//            String column_name = metadata.getColumnName(i);
//            FieldData fieldData = annotationParameters.get(column_name);
//            if (fieldData == null) continue;
//            Class<?> type = fieldData.field.getType();
//            if (metadata.getColumnType(i) == java.sql.Types.ARRAY) {
//                Array array = resultSet.getArray(column_name);
//                fieldData.field.set(this, array);
//            } else if (metadata.getColumnType(i) == java.sql.Types.BIGINT) {
//                if (type.isPrimitive()) {
//                    System.err.println("Поле " + column_name + " не удалось привязать к примитивному значению так как оно BIGINT");
//                }
//                BigDecimal bigDecimal = resultSet.getBigDecimal(column_name);
//                if (type == BigDecimal.class) {
//                    fieldData.field.set(this, bigDecimal);
//                } else if (type == BigInteger.class) {
//                    fieldData.field.set(this, bigDecimal.toBigInteger());
//                }
//            } else if (metadata.getColumnType(i) == java.sql.Types.BOOLEAN) {
//                if (type == Boolean.class || type == boolean.class) {
//                    boolean resultSetBoolean = resultSet.getBoolean(column_name);
//                    fieldData.field.set(this, resultSetBoolean);
//                } else {
//                    System.err.println("Поле " + column_name + " не удалось привязать к значению так как поле не Boolean");
//                }
//            } else if (metadata.getColumnType(i) == java.sql.Types.BLOB) {
//                if (type == Blob.class || type == String.class) {
//                    Blob resultSetBlob = resultSet.getBlob(column_name);
//                    if (type == Blob.class) {
//                        fieldData.field.set(this, resultSetBlob);
//                    } else {
//                        fieldData.field.set(this, resultSetBlob.toString());
//                    }
//                } else {
//                    System.err.println("Поле " + column_name + " не удалось привязать к значению так как поле не Blob и не String");
//                }
//            } else if (metadata.getColumnType(i) == java.sql.Types.DOUBLE) {
//                if (type == Double.class || type == double.class) {
//                    Double resultSetDouble = resultSet.getDouble(column_name);
//                    if (type.isPrimitive()) {
//                        fieldData.field.set(this, resultSetDouble.doubleValue());
//                    } else {
//                        fieldData.field.set(this, resultSetDouble);
//                    }
//                } else {
//                    System.err.println("Поле " + column_name + " не удалось привязать к значению так как поле не Double");
//                }
//            } else if (metadata.getColumnType(i) == java.sql.Types.FLOAT) {
//                if (type == Float.class || type == float.class) {
//                    Float resultSetDouble = resultSet.getFloat(column_name);
//                    if (type.isPrimitive()) {
//                        fieldData.field.set(this, resultSetDouble.floatValue());
//                    } else {
//                        fieldData.field.set(this, resultSetDouble);
//                    }
//                } else {
//                    System.err.println("Поле " + column_name + " не удалось привязать к значению так как поле не Float");
//                }
//            } else if (metadata.getColumnType(i) == java.sql.Types.INTEGER) {
//                if (type == Integer.class || type == int.class) {
//                    Integer resultSetDouble = resultSet.getInt(column_name);
//                    if (type.isPrimitive()) {
//                        fieldData.field.set(this, resultSetDouble.floatValue());
//                    } else {
//                        fieldData.field.set(this, resultSetDouble);
//                    }
//                } else {
//                    System.err.println("Поле " + column_name + " не удалось привязать к значению так как поле не Integer");
//                }
//            } else if (metadata.getColumnType(i) == java.sql.Types.NVARCHAR || metadata.getColumnType(i) == java.sql.Types.VARCHAR) {
//                object.put(column_name, resultSet.getNString(column_name));
//            } else if (metadata.getColumnType(i) == java.sql.Types.TINYINT) {
//                object.put(column_name, resultSet.getInt(column_name));
//            } else if (metadata.getColumnType(i) == java.sql.Types.SMALLINT) {
//                object.put(column_name, resultSet.getInt(column_name));
//            } else if (metadata.getColumnType(i) == java.sql.Types.DATE) {
//                object.put(column_name, resultSet.getDate(column_name).toString());
//            } else if (metadata.getColumnType(i) == java.sql.Types.TIMESTAMP) {
//                object.put(column_name, resultSet.getTimestamp(column_name).toString());
//            } else {
//                try {
//                    object.put(column_name, new ObjectMapper().readTree(String.valueOf(resultSet.getObject(column_name))));
//                } catch (Exception e) {
//                    object.put(column_name, String.valueOf(resultSet.getObject(column_name)));
//                }
//            }
//        }
//        for (FieldData fieldData : annotationParameters) {
//            Field field = fieldData.field;
//            field.setAccessible(true);
//            String name = fieldData.column;
//            String value = resultSet.getString(name);
//            ResultSetMetaData metadata = resultSet.getMetaData();
//            metadata.getT
//            if (value == null) {
//                continue;
//            }
//            value = value.trim();
//            try {
//                Class<?> type = field.getType();
//                if (mapNumber.containsKey(type)) {
//                    value = value.replaceAll(",", ".");
//                }
//                if (type.isPrimitive()) {
//                    field.set(this, map.get(type).getConstructor(String.class).newInstance(value));
//                } else {
//                    field.set(this, type.getConstructor(String.class).newInstance(value));
//                }
//            } catch (Exception e) {
//                System.err.println("Поле " + name + " не удалось привязать к значению");
//            }
//        }
//    }
//
//    /**
//     * Функция получения списка параметров с аннотацией {@link ParameterAnnotation}
//     *
//     * @return возвращает {@link HashMap<String,Field>} со списком парамеров.
//     */
//    private HashMap<String, FieldData> getAnnotationParameters() throws ExceptionDuplicateAnnotation {
//        HashMap<String, FieldData> annotationParameters = new HashMap<>();
//        Class<? extends ResultSetWrapper<T>> clazz = (Class<? extends ResultSetWrapper<T>>) this.getClass();
//        for (Field field : clazz.getDeclaredFields()) {
//            if (field.isAnnotationPresent(ParameterAnnotation.class)) {
//                ParameterAnnotation parameterAnnotation = field.getAnnotation(ParameterAnnotation.class);
//                String column = parameterAnnotation.column();
//                if (annotationParameters.containsKey(column)) {
//                    throw new ExceptionDuplicateAnnotation(column, clazz.getName());
//                }
//                annotationParameters.put(column, new FieldData(column, field));
//            }
//        }
//        return annotationParameters;
//    }
//
//    private static class FieldData {
//        String column;
//        Field field;
//
//        public FieldData(String column, Field field) {
//            this.column = column;
//            this.field = field;
//        }
//    }
//
//}
