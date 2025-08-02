package ru.iedt.database.request.controller.parameter;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import ru.iedt.database.request.controller.entity.RowTypes;

public class SQLParameter<T> {

    private static final Logger LOG = LoggerFactory.getLogger(SQLParameter.class);

    private final T value;
    private final String name;

    public SQLParameter(String name, T value) {
        if (!(RowTypes.typeFunctionHashMap.containsKey(value.getClass())
                || RowTypes.typeArrayFunctionHashMap.containsKey(value.getClass()))) {
            RuntimeException exception = new RuntimeException(
                    String.format("SQLParameter %s not supported class %s", name, value.getClass()));
            LOG.error(exception);
            throw exception;
        }
        this.value = value;
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
