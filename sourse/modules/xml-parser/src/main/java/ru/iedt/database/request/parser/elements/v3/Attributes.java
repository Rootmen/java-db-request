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

    public interface Connection {
        public static final String REFID = "refid";
        public static final String NAME = "name";
        public static final String DEFAULT = "default";
    }

}
