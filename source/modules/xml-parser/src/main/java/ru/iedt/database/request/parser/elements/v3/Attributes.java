package ru.iedt.database.request.parser.elements.v3;

/**
 * Интерфейс, предоставляющий константы для работы с атрибутами в различных элементах парсера
 * запросов.
 */
public interface Attributes {

  /** Интерфейс, определяющий константы для работы с атрибутами версии парсера. */
  interface Definitions {
    String PARSER_VERSION = "parser";
  }

  /** Интерфейс, определяющий константы для работы с атрибутами шаблона. */
  interface Template {
    String ID = "id";
  }

  /** Интерфейс, определяющий константы для работы с атрибутами набора запросов. */
  interface QuerySet {
    String ID = "id";
  }

  /** Интерфейс, определяющий константы для работы с атрибутами SQL-запроса. */
  interface SQL {
    String NAME = "name";
    String WRAPPER_CLASS = "wrapper_class";
  }

  /** Интерфейс, определяющий константы для работы с атрибутами параметра. */
  interface Parameter {
    String NAME = "name";
    String TYPE = "type";
    String DEFAULT = "default";
  }

  /** Интерфейс, определяющий константы для работы с атрибутами условия. */
  interface When {
    String VALUE = "value";
  }
}
