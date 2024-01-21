package ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.type;

import io.vertx.mutiny.sqlclient.Tuple;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterTypes;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.ParameterObjects;

import java.math.BigDecimal;
import java.math.BigInteger;

import static ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterFactory.generateArray;

public class ParameterBigInteger extends ParameterObjects<BigInteger> {

    public ParameterBigInteger(String defaultValue, String parameterName) {
        super(parseString(defaultValue), parameterName, ParameterTypes.BIGINT);
    }

    public ParameterBigInteger(BigInteger defaultValue, String parameterName) {
        super(defaultValue, parameterName, ParameterTypes.BIGINT);
    }

    @Override
    public void setValue(String value) {
        this.currentValue = parseString(value);
    }

    static BigInteger parseString(String integer) throws RuntimeException {
        try {
            return new BigInteger(integer);
        } catch (Exception e) {
            throw new RuntimeException(integer + " to BigInteger ", e);
        }
    }

    @Override
    public void addToTuple(Tuple tuple) {
        tuple.addBigDecimal(new BigDecimal(this.getValue()));
    }
}