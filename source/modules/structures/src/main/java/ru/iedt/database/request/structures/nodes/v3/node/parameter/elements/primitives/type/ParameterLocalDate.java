package ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.type;

import io.vertx.mutiny.sqlclient.Tuple;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterTypes;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.ParameterAbstract;

public class ParameterLocalDate extends ParameterAbstract<LocalDate> {
private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

public ParameterLocalDate(String defaultValue, String parameterName) {
	super(parseString(defaultValue), parameterName, ParameterTypes.DATE);
}

public ParameterLocalDate(LocalDate defaultValue, String parameterName) {
	super(defaultValue, parameterName, ParameterTypes.DATE);
}

@Override
public void setValue(String value) {
	this.currentValue = parseString(value);
}

@Override
public void addToTuple(Tuple tuple) {
	tuple.addLocalDate(this.getValue());
}

static LocalDate parseString(String value) throws RuntimeException {
	if (value == null) return null;
	try {
	return LocalDate.parse(value, formatter);
	} catch (Exception e) {
	throw new RuntimeException(value + " to e " + e);
	}
}

public static DateTimeFormatter getFormatter() {
	return formatter;
}
}
