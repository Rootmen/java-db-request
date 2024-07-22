package ru.iedt.database.request.exceptions.store.types;

import ru.iedt.database.request.exceptions.store.base.BaseStoreFormat;

public class BaseStoreFormatException extends BaseStoreFormat {
/**
* Конструктор класса BaseExceptions.
*
* @param exceptionOrigin Исходное исключение, которое вызвало данное исключение (при его
*     наличии).
* @param message Сообщение об ошибке, связанное с исключением.
*/
public BaseStoreFormatException(Exception exceptionOrigin, String message) {
	super(exceptionOrigin, message);
}
}
