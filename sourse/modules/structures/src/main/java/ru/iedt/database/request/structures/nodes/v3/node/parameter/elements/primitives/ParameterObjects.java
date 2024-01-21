package ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.primitives;

import io.vertx.mutiny.sqlclient.Tuple;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.ParameterAbstract;

public abstract class ParameterObjects<T> extends ParameterAbstract<T> {

    protected T currentValue;

    public ParameterObjects(T defaultValue, String parameterName, String parameterType) {
        super(defaultValue, parameterName, parameterType);
    }

    @Override
    public T getValue() {
        return (this.currentValue == null) ? this.defaultValue : this.currentValue;
    }

    @Override
    public void setValue(T value) {
        this.currentValue = value;
    };

    @Override
    public abstract void addToTuple(Tuple tuple);
}