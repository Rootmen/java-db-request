package ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.type;

import io.vertx.mutiny.sqlclient.Tuple;
import java.math.BigInteger;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterTypes;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.ParameterAbstract;

public class ParameterLong extends ParameterAbstract<Long> {
public ParameterLong(String defaultValue, String parameterName) {
	super(parseString(defaultValue), parameterName, ParameterTypes.LONG);
}

public ParameterLong(Long defaultValue, String parameterName) {
	super(defaultValue, parameterName, ParameterTypes.LONG);
}

@Override
public void setValue(String value) {
	this.currentValue = parseString(value);
}

@Override
public void addToTuple(Tuple tuple) {
	tuple.addLong(this.getValue());
}

static Long parseString(String integer) throws RuntimeException {
	if (integer == null) return null;
	try {
	return new BigInteger(integer).longValue();
	} catch (Exception e) {
	throw new RuntimeException(integer + " BigInteger " + e);
	}
}
}
