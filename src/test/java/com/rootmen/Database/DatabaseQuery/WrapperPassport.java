package com.rootmen.Database.DatabaseQuery;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rootmen.Database.DatabaseQuery.Query.Binder.QueryWrapperClass;

public class WrapperPassport extends QueryWrapperClass {

    @Override
    public ObjectNode rowsLine(ObjectNode node) {
        return node;
    };
}
