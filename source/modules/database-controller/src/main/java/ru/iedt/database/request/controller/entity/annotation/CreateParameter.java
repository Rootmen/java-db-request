package ru.iedt.database.request.controller.entity.annotation;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ru.iedt.database.request.structures.nodes.v3.Elements;

@Target(ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@RegisterForReflection
public @interface CreateParameter {
  String column_name();
}
