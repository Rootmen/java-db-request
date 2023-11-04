package ru.iedt.database.request.exceptions;

/**
 * Класс BaseExceptions является пользовательским исключением,
 * производным от класса Exception. Он используется для представления
 * исключительных ситуаций в контексте базовых ошибок.
 */
public class BaseExceptions extends Exception {

    /**
     * Исходное исключение, которое вызвало данное пользовательское исключение.
     */
    Exception exceptionOrigin;

    /**
     * Сообщение об ошибке, связанное с этим пользовательским исключением.
     */
    String message;

    /**
     * Конструктор класса BaseExceptions.
     *
     * @param exceptionOrigin Исходное исключение, которое вызвало данное исключение (при его наличии).
     * @param message         Сообщение об ошибке, связанное с исключением.
     */
    public BaseExceptions(Exception exceptionOrigin, String message) {
        this.exceptionOrigin = exceptionOrigin;
        this.message = message;
    }

    /**
     * Возвращает исходное исключение, которое вызвало данное пользовательское исключение.
     *
     * @return Исходное исключение.
     */
    public Exception getExceptionOrigin() {
        return exceptionOrigin;
    }

    /**
     * Переопределение метода getMessage() из класса Exception.
     * Возвращает сообщение об ошибке, связанное с пользовательским исключением.
     *
     * @return Сообщение об ошибке.
     */
    @Override
    public String getMessage() {
        return this.message;
    }
}