package ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.type;

import io.vertx.mutiny.sqlclient.Tuple;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterTypes;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.ParameterObjects;

public class ParameterInteger extends ParameterObjects<Integer> {

    public ParameterInteger(String defaultValue, String parameterName) {
        super(parseString(defaultValue), parameterName, ParameterTypes.INTEGER);
    }

    public ParameterInteger(Integer defaultValue, String parameterName) {
        super(defaultValue, parameterName, ParameterTypes.INTEGER);
    }

    @Override
    public void addToTuple(Tuple tuple) {
        tuple.addInteger(this.getValue());
    }

    @Override
    public void setValue(String value) {
        this.currentValue = parseString(value);
    }

    static Integer parseString(String integer) throws RuntimeException {
        if (integer == null) return null;
        try {
            return Integer.parseInt(integer);
        } catch (Exception e) {
            throw new RuntimeException(integer + " to Integer " + e);
        }
    }
}
