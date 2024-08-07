package ru.iedt.database.request.controller.entity.annotation;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.CONSTRUCTOR)
@Retention(value = RetentionPolicy.RUNTIME)
@RegisterForReflection
public @interface CreateConstructor {}
