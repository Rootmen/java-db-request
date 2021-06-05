package com.rootmen.DatabaseController.Entities;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rootmen.DatabaseController.Entities.Parameter.Parameter;

import java.sql.Connection;
import java.util.ArrayList;

public class QuerySet {
    private String ID = null;
    private Connection connections = null;
    private ArrayList<Parameter> Queries = new ArrayList<Parameter>();

    static void generated(ObjectNode file, ObjectNode request, String querySetID) {

    }
}
