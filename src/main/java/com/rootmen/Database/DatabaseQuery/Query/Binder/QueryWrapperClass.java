package com.rootmen.Database.DatabaseQuery.Query.Binder;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rootmen.Database.DatabaseQuery.Parameter.Parameter;
import com.rootmen.Database.DatabaseQuery.Query.ConnectionsManager;

import java.io.PrintWriter;
import java.util.HashMap;

public abstract class QueryWrapperClass {

    public abstract void initialize(HashMap<String, Parameter<?>> parameters, HashMap<String, ConnectionsManager> connection);

    public ObjectNode rowsLine(ObjectNode node) {
        return node;
    }

    public ArrayNode resultWrapper() {
        return null;
    }

    public void additionalDates(PrintWriter printWriter) {
    }
}
