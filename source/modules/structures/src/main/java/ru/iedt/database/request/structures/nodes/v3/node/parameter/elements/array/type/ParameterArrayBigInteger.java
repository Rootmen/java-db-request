package ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.array.type;

import static ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterFactory.generateArray;

import io.vertx.mutiny.sqlclient.Tuple;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterTypes;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.ParameterAbstract;

public class ParameterArrayBigInteger extends ParameterAbstract<ArrayList<BigInteger>> {
    public ParameterArrayBigInteger(ArrayList<BigInteger> defaultValue, String parameterName) {
        super(defaultValue, parameterName, ParameterTypes.BIGINT_ARRAY);
    }

    @Override
    public void setValue(String value) {
        this.currentValue = generateArray(value, BigInteger.class);
    }

    @Override
    public void addToTuple(Tuple tuple) {
        ArrayList<BigInteger> arrayList = this.getValue();
        if (arrayList == null) {
            tuple.addArrayOfBigDecimal(null);
            return;
        }
        ArrayList<BigDecimal> insert = new ArrayList<>();
        for (BigInteger bigInteger : arrayList) {
            insert.add(new BigDecimal(bigInteger));
        }
        tuple.addArrayOfBigDecimal(insert.toArray(BigDecimal[]::new));
    }
}
