package ru.iedt.database.request.structures.nodes.database;

import ru.iedt.database.request.structures.base.Node;

public class Parameter extends Node {
    String parameterValue;
    String parameterName;
    String parameterType;

    public Parameter(String parameterValue, String parameterName, String parameterType) {
        super("Parameter", "Query");
        this.parameterValue = parameterValue;
        this.parameterName = parameterName;
        this.parameterType = parameterType;
    }
}
