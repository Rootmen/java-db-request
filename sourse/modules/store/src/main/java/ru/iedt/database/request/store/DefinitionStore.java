package ru.iedt.database.request.store;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.lang.annotation.*;

/**
 * Аннотация `@DefinitionStore` используется для пометки классов, которые представляют хранилища definition.
 * Эта аннотация позволяет обнаруживать классы, содержащие определения, и получать метаданные о них.
 * Классы, отмеченные этой аннотацией, могут быть использованы в рефлексивных операциях для получения информации
 * о хранилищах определений.
 */
@RegisterForReflection
@Target(ElementType.TYPE)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface DefinitionStore {
}
