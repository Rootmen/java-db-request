package ru.iedt.database.request.controller.entity;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.vertx.mutiny.sqlclient.Row;

@RegisterForReflection
public class BaseEntity {

  static <T> T from(Row row, Class<T> clazz) {
    return ReflectionUtil.from(row, clazz);
  }
}
