package ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.type;

import io.vertx.mutiny.sqlclient.Tuple;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterTypes;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.ParameterObjects;

import java.math.BigDecimal;

public class ParameterNumeric extends ParameterObjects<BigDecimal> {
    public ParameterNumeric(String defaultValue, String parameterName) {
        super(parseString(defaultValue), parameterName, ParameterTypes.NUMERIC);
    }

    public ParameterNumeric(BigDecimal defaultValue, String parameterName) {
        super(defaultValue, parameterName, ParameterTypes.NUMERIC);
    }

    @Override
    public void setValue(String value) {
        this.currentValue = parseString(value);
    }

    @Override
    public void addToTuple(Tuple tuple) {
        tuple.addBigDecimal(this.getValue());
    }

    static BigDecimal parseString(String value) throws RuntimeException {
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            throw new RuntimeException(value + " to BigDecimal " + e);
        }
    }
}
