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
