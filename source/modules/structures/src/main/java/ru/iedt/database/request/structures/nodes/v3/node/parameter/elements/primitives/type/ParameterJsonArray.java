package ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.type;

import io.vertx.core.json.JsonArray;
import io.vertx.mutiny.sqlclient.Tuple;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterTypes;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.ParameterAbstract;

public class ParameterJsonArray extends ParameterAbstract<JsonArray> {

public ParameterJsonArray(JsonArray defaultValue, String parameterName) {
	super(defaultValue, parameterName, ParameterTypes.JSON);
}

public ParameterJsonArray(String defaultValue, String parameterName) {
	super(parseString(defaultValue), parameterName, ParameterTypes.JSON);
}

@Override
public void setValue(String value) {
	this.currentValue = parseString(value);
}

@Override
public void addToTuple(Tuple tuple) {
	tuple.addJsonArray(this.getValue());
}

static JsonArray parseString(String value) throws RuntimeException {
	if (value == null) return null;
	try {
	return new JsonArray(value);
	} catch (Exception e) {
	throw new RuntimeException(value + " to JsonObject " + e);
	}
}
}
