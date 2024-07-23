package ru.iedt.database.request.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.mutiny.sqlclient.Row;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.BiFunction;
import ru.iedt.database.request.controller.entity.annotation.CreateConstructor;
import ru.iedt.database.request.controller.entity.annotation.CreateParameter;

public class ReflectionUtil {

    @SuppressWarnings("unchecked")
    static <T> T from(Row row, Class<T> clazz) {
        Constructor<T>[] constructors = (Constructor<T>[]) clazz.getConstructors();
        Constructor<T> selectedConstructor = null;
        for (Constructor<T> constructor : constructors) {
            if (constructor.isAnnotationPresent(CreateConstructor.class)) {
                selectedConstructor = constructor;
                break;
            }
        }
        if (selectedConstructor != null) {
            return ReflectionUtil.fromCreateConstructor(row, selectedConstructor);
        }
        try {
            return ReflectionUtil.fromEmptyConstructor(row, clazz.getConstructor());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Не найден @CreateConstructor или пустой конструктор");
        }
    }

    static <T> T fromCreateConstructor(Row row, Constructor<T> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Type[] genericTypes = constructor.getGenericParameterTypes();
        Annotation[][] annotations = constructor.getParameterAnnotations();
        ArrayList<CreateParameter> parameters = new ArrayList<>(annotations.length);
        ArrayList<JsonProperty> parametersJson = new ArrayList<>(annotations.length);
        int index = 0;
        for (Annotation[] annotationList : annotations) {
            boolean isAnnotation = false;
            parameters.add(null);
            parametersJson.add(null);
            for (Annotation annotation : annotationList) {
                if (annotation.annotationType().equals(CreateParameter.class)) {
                    isAnnotation = true;
                    parameters.set(index, (CreateParameter) annotation);
                }
                if (annotation.annotationType().equals(JsonProperty.class)) {
                    isAnnotation = true;
                    parametersJson.set(index, (JsonProperty) annotation);
                }
            }
            if (!isAnnotation) {
                throw new RuntimeException(
                        "Найден @CreateConstructor параметер которого не анотирован @CreateParameter \n"
                                + "Название класса констурктра:"
                                + constructor.getName());
            }
            index++;
        }
        Object[] constructorParameters = new Object[parameters.size()];
        for (int g = 0; g < parameters.size(); g++) {
            if (RowTypes.primitiveMap.containsKey(parameterTypes[g])) {
                throw new RuntimeException("Примитивы использовть нельзя");
            }
            constructorParameters[g] = ReflectionUtil.getDataFromRow(
                    row, parameterTypes[g], parameters.get(g), parametersJson.get(g), genericTypes[g]);
        }
        try {
            return constructor.newInstance(constructorParameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    static Object getDataFromRow(
            Row row, Class<?> clazz, CreateParameter createParameter, JsonProperty jsonParameter, Type typeClass) {
        String name = (createParameter != null) ? createParameter.value() : jsonParameter.value();
        // TODO json конвертер при createParameter.is_json_mapping()
        if (createParameter != null && createParameter.is_json_mapping()) {
            return getDataFromJson(row, clazz, name, typeClass);
        }

        if (clazz == ArrayList.class || clazz == LinkedList.class || clazz == List.class) {
            return getListArrayFromRow(row, clazz, name, typeClass);
        }
        BiFunction<Row, String, Object> type = RowTypes.typeFunctionHashMap.get(clazz);
        if (type == null) {
            throw new RuntimeException("Для типа " + clazz.getName() + " не найден способ конвертации");
        }
        return type.apply(row, name);
    }

    static ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    static Object getDataFromJson(Row row, Class<?> clazz, String createParameter, Type typeGeneric) {
        try {
            if (clazz == ArrayList.class || clazz == LinkedList.class || clazz == List.class) {
                Class<?> arrayClazz = (Class<?>) ((ParameterizedType) typeGeneric).getActualTypeArguments()[0];
                JavaType type = mapper.getTypeFactory()
                        .constructCollectionType((Class<? extends Collection>) clazz, arrayClazz);

                return mapper.readValue(row.getJson(createParameter).toString(), type);
            }
            return mapper.readValue(row.getJson(createParameter).toString(), clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    static Object getListArrayFromRow(Row row, Class<?> clazz, String createParameter, Type typeGeneric) {
        if (typeGeneric instanceof ParameterizedType) {
            Class<?> arrayClazz = (Class<?>) ((ParameterizedType) typeGeneric).getActualTypeArguments()[0];
            Object[] array = RowTypes.typeArrayFunctionHashMap.get(arrayClazz).apply(row, createParameter);
            List<Object> arrayList = Arrays.stream(array).toList();
            if (clazz == ArrayList.class) {
                return new ArrayList<>(arrayList);
            }
            if (clazz == List.class) {
                return (List<?>) (new ArrayList<>(arrayList));
            }
            if (clazz == LinkedList.class) {
                return new LinkedList<>(arrayList);
            }
            throw new RuntimeException("Непонятный тип массива из List, ArrayList или LinkedList");
        }
        throw new RuntimeException("ParameterizedType не найден для List");
    }

    static <T> T fromEmptyConstructor(Row row, Constructor<T> constructor) {
        return null;
    }
}
