package ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.type;

import io.vertx.mutiny.sqlclient.Tuple;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterTypes;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.ParameterObjects;

public class ParameterLocalDateTime extends ParameterObjects<LocalDateTime> {

    public ParameterLocalDateTime(String defaultValue, String parameterName) {
        super(parseString(defaultValue), parameterName, ParameterTypes.TIMESTAMP);
    }

    public ParameterLocalDateTime(LocalDateTime defaultValue, String parameterName) {
        super(defaultValue, parameterName, ParameterTypes.TIMESTAMP);
    }

    @Override
    public void setValue(String value) {
        this.currentValue = parseString(value);
    }

    @Override
    public void addToTuple(Tuple tuple) {
        tuple.addLocalDateTime(this.getValue());
    }

    static LocalDateTime parseString(String value) throws RuntimeException {
        if (value == null) return null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return LocalDateTime.parse(value, formatter);
        } catch (Exception e) {
            throw new RuntimeException(value + " timestamp " + e);
        }
    }
}
