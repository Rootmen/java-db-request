package ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.type;

import io.vertx.mutiny.sqlclient.Tuple;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterTypes;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.ParameterAbstract;

public class ParameterLocalDateTime extends ParameterAbstract<LocalDateTime> {
private static final DateTimeFormatter formatter =
	DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

public ParameterLocalDateTime(String defaultValue, String parameterName) {
	super(parseString(defaultValue), parameterName, ParameterTypes.TIMESTAMP);
}

public ParameterLocalDateTime(LocalDateTime defaultValue, String parameterName) {
	super(defaultValue, parameterName, ParameterTypes.TIMESTAMP);
}

@Override
public void setValue(String value) {
	this.currentValue = parseString(value);
}

@Override
public void addToTuple(Tuple tuple) {
	tuple.addLocalDateTime(this.getValue());
}

static LocalDateTime parseString(String value) throws RuntimeException {
	if (value == null) return null;
	try {
	return LocalDateTime.parse(value, formatter);
	} catch (Exception e) {
	throw new RuntimeException(value + " timestamp " + e);
	}
}

public static DateTimeFormatter getFormatter() {
	return formatter;
}
}
