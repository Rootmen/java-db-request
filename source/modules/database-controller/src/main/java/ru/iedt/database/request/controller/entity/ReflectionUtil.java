package ru.iedt.database.request.controller.entity;

import io.vertx.mutiny.sqlclient.Row;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Objects;
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
      return fromCreateConstructor(row, selectedConstructor);
    }
    try {
      return fromEmptyConstructor(row, clazz.getConstructor());
    } catch (NoSuchMethodException e) {
      throw new RuntimeException("Не найден @CreateConstructor или пустой конструктор");
    }
  }

  static <T> T fromCreateConstructor(Row row, Constructor<T> constructor) {
    Class<?>[] parameterTypes = constructor.getParameterTypes();
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
            "Не найден @CreateConstructor параметер которого не анотирован @CreateParameter \n"
                + "Название класса констурктра:"
                + constructor.getName());
      }
    }
    Objects[] constructorParameters = new Objects[parameters.size()];
    for (int g = 0; g < parameters.size(); g++) {
      constructorParameters[g] = getDataFroRow(row, parameterTypes[g], parameters.get(g));
    }
    try {
      return constructor.newInstance(constructorParameters);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  static Objects getDataFroRow(Row row, Class<?> clazz, CreateParameter createParameter) {
     /* if (clazz == Integer.class || clazz == int.class) {
          return row.get()
      }
      switch (clazz) {
          case Integer.class: {

          }
      }*/
    return (Objects) row.get(clazz, createParameter.column_name());
  }

  static <T> T fromEmptyConstructor(Row row, Constructor<T> constructor) {
    return null;
  }
}
