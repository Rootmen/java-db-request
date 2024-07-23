package ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.type;

import io.vertx.mutiny.sqlclient.Tuple;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterTypes;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.ParameterAbstract;

public class ParameterBoolean extends ParameterAbstract<Boolean> {

    public ParameterBoolean(String defaultValue, String parameterName) {
        super(parseString(defaultValue), parameterName, ParameterTypes.BOOLEAN);
    }

    public ParameterBoolean(Boolean defaultValue, String parameterName) {
        super(defaultValue, parameterName, ParameterTypes.BOOLEAN);
    }

    @Override
    public void setValue(String value) {
        this.currentValue = parseString(value);
    }

    static Boolean parseString(String input) throws RuntimeException {
        if (input == null) return false;
        try {
            return Boolean.parseBoolean(input);
        } catch (Exception e) {
            throw new RuntimeException(input + " to Boolean ", e);
        }
    }

    @Override
    public void addToTuple(Tuple tuple) {
        tuple.addBoolean(this.getValue());
    }
}
