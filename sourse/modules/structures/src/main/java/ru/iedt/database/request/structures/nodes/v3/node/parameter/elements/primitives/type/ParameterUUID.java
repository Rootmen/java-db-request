package ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.type;

import io.vertx.mutiny.sqlclient.Tuple;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterTypes;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.ParameterObjects;

import java.math.BigDecimal;
import java.util.UUID;

public class ParameterUUID extends ParameterObjects<UUID> {
    public ParameterUUID(String defaultValue, String parameterName) {
        super(parseString(defaultValue), parameterName, ParameterTypes.UUID);
    }

    @Override
    public void setValue(String value) {
        this.currentValue = UUID.fromString(value);
    }

    @Override
    public void addToTuple(Tuple tuple) {
        tuple.addUUID(this.getValue());
    }

    static UUID parseString(String value) throws RuntimeException {
        if (value == null) return null;
        try {
            return UUID.fromString(value);
        } catch (Exception e) {
            throw new RuntimeException(value + " to UUID " + e);
        }
    }
}
