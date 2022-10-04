package com.rootmen.Database.DatabaseQuery;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rootmen.Database.DatabaseQuery.Parameter.Parameter;
import com.rootmen.Database.DatabaseQuery.Query.Binder.QueryWrapperClass;
import com.rootmen.Database.DatabaseQuery.Query.ConnectionsManager;

import java.util.HashMap;

public class WrapperPassport extends QueryWrapperClass {

    @Override
    public void initialize(HashMap<String, Parameter<?>> parameters, HashMap<String, ConnectionsManager> connection) {

    }

    @Override
    public ObjectNode rowsLine(ObjectNode node) {
        return node;
    };
}
