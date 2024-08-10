package ru.iedt.database.controller.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Deprecated(since = "Переходите на @TaskSynchronous")
public @interface Task {
    String value();
}
