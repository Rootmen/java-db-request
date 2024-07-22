package ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.type;

import io.vertx.mutiny.sqlclient.Tuple;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterTypes;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.ParameterAbstract;

public class ParameterString extends ParameterAbstract<String> {
public ParameterString(String defaultValue, String parameterName) {
	super(defaultValue, parameterName, ParameterTypes.STRING);
}

@Override
public void addToTuple(Tuple tuple) {
	tuple.addString(this.getValue());
}
}
