package com.rootmen.Database.DatabaseQuery.Query.Binder;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rootmen.Database.DatabaseQuery.Parameter.Parameter;
import com.rootmen.Database.DatabaseQuery.Query.ConnectionsManager;

import java.util.HashMap;

public abstract class QueryWrapperClass {

    public abstract void initialize(HashMap<String, Parameter<?>> parameters, HashMap<String, ConnectionsManager> connection);

    public abstract ObjectNode rowsLine(ObjectNode node);
}
