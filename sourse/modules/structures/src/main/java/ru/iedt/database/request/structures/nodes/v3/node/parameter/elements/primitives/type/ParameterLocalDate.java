package ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.type;

import io.vertx.mutiny.sqlclient.Tuple;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterTypes;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.ParameterObjects;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParameterLocalDate extends ParameterObjects<LocalDate> {

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
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(value, formatter);
        } catch (Exception e) {
            throw new RuntimeException(value + " to e " + e);
        }
    }
}