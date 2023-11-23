package ru.iedt.database.request.parser.elements.v3;

/**
 * Интерфейс, предоставляющий константы для работы с атрибутами в различных элементах парсера запросов.
 */
public interface Attributes {

    /**
     * Интерфейс, определяющий константы для работы с атрибутами версии парсера.
     */
    public interface Definitions {
        public static final String PARSER_VERSION = "parser";
    }

    /**
     * Интерфейс, определяющий константы для работы с атрибутами шаблона.
     */
    public interface Template {
        public final String ID = "id";
    }

    /**
     * Интерфейс, определяющий константы для работы с атрибутами набора запросов.
     */
    public interface QuerySet {
        public static final String ID = "id";
    }

    /**
     * Интерфейс, определяющий константы для работы с атрибутами SQL-запроса.
     */
    public interface SQL {
        public static final String NAME = "name";
        public static final String WRAPPER_CLASS = "wrapper_class";
    }

    /**
     * Интерфейс, определяющий константы для работы с атрибутами параметра.
     */
    public interface Parameter {
        public static final String NAME = "name";
        public static final String TYPE = "type";
        public static final String DEFAULT = "default";
    }

    /**
     * Интерфейс, определяющий константы для работы с атрибутами условия.
     */
    public interface When {
        public static final String VALUE = "value";
    }
}
