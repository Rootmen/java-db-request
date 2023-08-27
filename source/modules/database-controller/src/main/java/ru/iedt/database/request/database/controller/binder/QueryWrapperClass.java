package ru.iedt.database.request.database.controller.binder;

import com.fasterxml.jackson.databind.node.ArrayNode;
import jdk.nashorn.internal.ir.ObjectNode;
import ru.iedt.database.request.database.controller.parameter.Parameter;
import ru.iedt.database.request.database.controller.query.ConnectionsManager;

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
