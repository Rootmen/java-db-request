package ru.iedt.database.request.database.store.exceptions;

import ru.iedt.database.request.BaseExceptions;

public class BaseStoreFormatException extends BaseExceptions {
    /**
     * Конструктор класса BaseExceptions.
     *
     * @param exceptionOrigin Исходное исключение, которое вызвало данное исключение (при его наличии).
     * @param message         Сообщение об ошибке, связанное с исключением.
     */
    public BaseStoreFormatException(Exception exceptionOrigin, String message) {
        super(exceptionOrigin, message);
    }
}
