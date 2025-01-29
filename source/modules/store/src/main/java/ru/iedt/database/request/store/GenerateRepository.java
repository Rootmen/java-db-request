package ru.iedt.database.request.store;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация `@DefinitionStore` используется для пометки классов, для которых необходимо автоматически
 * сгенерировать описание репозитрия с запросами на основе заивисмости store-generator-plugin.
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(value = RetentionPolicy.RUNTIME)
@RegisterForReflection
public @interface GenerateRepository {
    String repositoryPackage();
}
