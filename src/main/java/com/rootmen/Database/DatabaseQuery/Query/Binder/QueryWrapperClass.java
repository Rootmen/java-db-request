package com.rootmen.Database.DatabaseQuery.Query.Binder;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rootmen.Database.DatabaseQuery.Parameter.Parameter;

import java.util.HashMap;

public abstract class QueryWrapperClass {

    public void initialize(HashMap<String, Parameter<?>> parameters) {

    };

    public ObjectNode rowsLine(ObjectNode node) {
        return node;
    };
}
