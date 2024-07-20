package ru.iedt.database.request.exceptions.store.base;

import ru.iedt.database.request.exceptions.BaseExceptions;

public class BaseStoreFormat extends BaseExceptions {
  /**
   * Конструктор класса BaseExceptions.
   *
   * @param exceptionOrigin Исходное исключение, которое вызвало данное исключение (при его
   *     наличии).
   * @param message Сообщение об ошибке, связанное с исключением.
   */
  public BaseStoreFormat(Exception exceptionOrigin, String message) {
    super(exceptionOrigin, message);
  }
}
