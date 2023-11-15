package ru.iedt.database.request.parser.elements.v3;

public interface Attributes {

    public interface Definitions {
        public static final String PARSER_VERSION = "parser";
    }

    public interface Template {
        public final String ID = "id";
    }

    public interface QuerySet {
        public static final String ID = "id";
    }

    public interface SQL {
        public static final String NAME = "name";
        public static final String WRAPPER_CLASS = "wrapper_class";
    }

    public interface Connection {
        public static final String REFID = "refid";
        public static final String NAME = "name";
        public static final String DEFAULT = "default";
    }

    public interface Parameter {
        public static final String NAME = "name";
        public static final String TYPE = "type";
        public static final String DEFAULT = "default";
    }

    public interface When {
        public static final String VALUE = "value";
    }

}
