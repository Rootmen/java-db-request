package ru.iedt.database.request.structures.nodes.v3.node.parameter.elements;

import io.vertx.mutiny.sqlclient.Tuple;
import ru.iedt.database.request.structures.nodes.v3.Elements;

import java.util.HashMap;
import java.util.Map;

public abstract class ParameterAbstract<T> implements Elements.Parameter<T> {

    protected final T defaultValue;
    protected final String parameterName;
    protected final String parameterType;
    protected final HashMap<String, String> whenMap = new HashMap<>();

    public ParameterAbstract(T defaultValue, String parameterName, String parameterType) {
        this.defaultValue = defaultValue;
        this.parameterName = parameterName;
        this.parameterType = parameterType;
    }

    public void setWhenMap(Map<String, String> whenMap) {
        this.whenMap.clear();
        this.whenMap.putAll(whenMap);
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getParameterType() {
        return parameterType;
    }

    public Map<String, String> getWhenMap() {
        return new HashMap<>(whenMap);
    }

    @Override
    public String toString() {
        return String.format(
                "[parameterName=%s, parameterType=%s, defaultValue=%s, whenMap=%s]",
                parameterName, parameterType, defaultValue, whenMap);
    }

    @Override
    public abstract T getValue();

    @Override
    public abstract void setValue(T value);

    @Override
    public abstract void addToTuple(Tuple tuple);
}
