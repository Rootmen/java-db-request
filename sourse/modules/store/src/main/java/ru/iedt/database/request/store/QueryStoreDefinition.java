package ru.iedt.database.request.store;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Абстрактный класс, представляющий базовое определение
 * для хранилища запросов.
 */
public abstract class QueryStoreDefinition {

    /**
     * Абстрактный метод, который должен быть реализован в подклассах,
     * чтобы получить путь к месту хранения запросов.
     *
     * @return URI пути к хранилищу запросов.
     * @throws URISyntaxException если произошла ошибка в URI.
     */
    abstract public URI getStorePath() throws URISyntaxException;

    /**
     * Абстрактный метод, который должен быть реализован в подклассах,
     * чтобы задать имя хранилища запросов.
     *
     * @return Имя хранилища запросов.
     */
    abstract public String getStoreName();
}
