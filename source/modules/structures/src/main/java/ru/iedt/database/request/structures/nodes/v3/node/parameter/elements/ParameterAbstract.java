package ru.iedt.database.request.structures.nodes.v3.node.parameter.elements;

import io.vertx.mutiny.sqlclient.Tuple;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import ru.iedt.database.request.structures.nodes.v3.Elements;

public abstract class ParameterAbstract<T> implements Elements.Parameter<T> {

    protected T currentValue;
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
    public T getValue() {
        return (this.currentValue == null) ? this.defaultValue : this.currentValue;
    }

    @Override
    public void setValue(T value) {
        this.currentValue = value;
    }
    ;

    @Override
    public abstract void addToTuple(Tuple tuple);

    @Override
    public String toString() {
        return new StringJoiner(", ", ParameterAbstract.class.getSimpleName() + "[", "]")
                .add("currentValue=" + currentValue)
                .add("defaultValue=" + defaultValue)
                .add("parameterName='" + parameterName + "'")
                .add("parameterType='" + parameterType + "'")
                .add("whenMap=" + whenMap)
                .toString();
    }
}
