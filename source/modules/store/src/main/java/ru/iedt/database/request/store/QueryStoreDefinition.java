package ru.iedt.database.request.store;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.net.URISyntaxException;

/**
 * Абстрактный класс `QueryStoreDefinition` представляет базовую структуру для хранения метаданных о
 * хранилищах запросов. Подклассы этого класса должны реализовать методы для получения информации о
 * пути и имени хранилища запросов.
 */
@RegisterForReflection
public abstract class QueryStoreDefinition {

    /**
     * Абстрактный метод, который должен быть реализован в подклассах, чтобы получить путь к месту
     * хранения запросов.
     *
     * @return URI пути к хранилищу запросов.
     * @throws URISyntaxException если произошла ошибка в URI.
     */
    public abstract String getResourcePatch();

    /**
     * Абстрактный метод, который должен быть реализован в подклассах, чтобы задать имя хранилища
     * запросов.
     *
     * @return Имя хранилища запросов.
     */
    public abstract String getStoreName();

    /**
     * Переопределенный метод `toString`, возвращающий строковое представление объекта
     * `QueryStoreDefinition`.
     *
     * @return Строковое представление объекта, включая путь и имя хранилища запросов.
     * @throws RuntimeException если произошла ошибка при получении пути хранилища запросов.
     */
    public abstract Class<?> getResourceClass();

    @Override
    public String toString() {
        return String.format(
                "QueryStore{ getResourcePatch='%s', storeName='%s', resourceClass='%s' }",
                this.getResourcePatch(), this.getStoreName(), this.getResourceClass());
    }
}
