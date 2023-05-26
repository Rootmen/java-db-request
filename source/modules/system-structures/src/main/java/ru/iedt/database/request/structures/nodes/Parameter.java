package ru.iedt.database.request.structures.nodes;

import ru.iedt.database.request.structures.base.Node;

public class Parameter extends Node {
    String parameterValue;
    String parameterName;

    public Parameter(String parameterValue, String parameterName) {
        super("parameter");
        this.parameterValue = parameterValue;
        this.parameterName = parameterName;
    }
}
