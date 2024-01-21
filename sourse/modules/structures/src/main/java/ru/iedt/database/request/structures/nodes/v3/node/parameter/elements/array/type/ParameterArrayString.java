package ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.array.type;

import io.vertx.mutiny.sqlclient.Tuple;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterTypes;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.array.ParameterArray;

import java.math.BigInteger;
import java.util.ArrayList;

import static ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterFactory.generateArray;

public class ParameterArrayString extends ParameterArray<ArrayList<String>> {
    public ParameterArrayString(ArrayList<String> defaultValue, String parameterName) {
        super(defaultValue, parameterName, ParameterTypes.STRING_ARRAY);
    }

    @Override
    public void setValue(String value) {
        this.currentValue = generateArray(value, "");
    }

    @Override
    public void addToTuple(Tuple tuple) {
        ArrayList<String> arrayList = this.getValue();
        if (arrayList == null) {
            tuple.addArrayOfString(null);
            return;
        }
        tuple.addArrayOfString(arrayList.toArray(String[]::new));
    }
}
