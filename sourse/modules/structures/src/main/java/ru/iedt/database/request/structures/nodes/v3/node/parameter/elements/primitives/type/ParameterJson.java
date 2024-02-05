package ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives.type;

import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.sqlclient.Tuple;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterTypes;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.ParameterAbstract;

public class ParameterJson extends ParameterAbstract<JsonObject> {

    public ParameterJson(JsonObject defaultValue, String parameterName) {
        super(defaultValue, parameterName, ParameterTypes.JSON);
    }
    public ParameterJson(String defaultValue, String parameterName) {
        super(parseString(defaultValue), parameterName, ParameterTypes.JSON);
    }

    @Override
    public void setValue(String value) {
        this.currentValue = parseString(value);
    }

    @Override
    public void addToTuple(Tuple tuple) {
        tuple.addJsonObject(this.getValue());
    }

    static JsonObject parseString(String value) throws RuntimeException {
        if (value == null) return null;
        try {
            return  new JsonObject(value);
        } catch (Exception e) {
            throw new RuntimeException(value + " to JsonObject " + e);
        }
    }
}
