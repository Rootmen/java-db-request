package ru.iedt.database.request.controller.entity;

import io.vertx.mutiny.sqlclient.Row;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
    ArrayList<CreateParameter> parameters = new ArrayList<>();
    for (Annotation[] annotationList : annotations) {
      boolean isAnnotation = false;
      for (Annotation annotation : annotationList) {
        if (annotation.annotationType().equals(CreateParameter.class)) {
          isAnnotation = true;
          parameters.add((CreateParameter) annotation);
          break;
        }
      }
      if (!isAnnotation) {
        throw new RuntimeException(
            "Найден @CreateConstructor параметер которого не анотирован @CreateParameter \n"
                + "Название класса констурктра:"
                + constructor.getName());
      }
    }
    Object[] constructorParameters = new Object[parameters.size()];
    for (int g = 0; g < parameters.size(); g++) {
      if (RowTypes.primitiveMap.containsKey(parameterTypes[g])) {
        throw new RuntimeException("Примитивы использовть нельзя");
      }
      constructorParameters[g] =
          ReflectionUtil.getDataFromRow(row, parameterTypes[g], parameters.get(g), genericTypes[g]);
    }
    try {
      return constructor.newInstance(constructorParameters);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  static Object getDataFromRow(
      Row row, Class<?> clazz, CreateParameter createParameter, Type typeClass) {
    // TODO json конвертер при createParameter.is_json_mapping()
    if (clazz == ArrayList.class || clazz == LinkedList.class || clazz == List.class) {
      return getListArrayFromRow(row, clazz, createParameter, typeClass);
    }
    BiFunction<Row, String, Object> type = RowTypes.typeFunctionHashMap.get(clazz);
    if (type == null) {
      throw new RuntimeException("Для типа " + clazz.getName() + " не найден способ конвертации");
    }
    return type.apply(row, createParameter.column_name());
  }

  static Object getListArrayFromRow(
      Row row, Class<?> clazz, CreateParameter createParameter, Type typeGeneric) {
    if (typeGeneric instanceof ParameterizedType) {
      Class<?> arrayClazz =
          (Class<?>) ((ParameterizedType) typeGeneric).getActualTypeArguments()[0];
      Object[] array =
          RowTypes.typeArrayFunctionHashMap
              .get(arrayClazz)
              .apply(row, createParameter.column_name());
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
