package ru.iedt.database.request.structures.nodes.database;

import ru.iedt.database.request.structures.base.Node;

import java.util.ArrayList;

public class Query extends Node {
    ArrayList<Parameter> parameters;
    ArrayList<String> text;

    public Query(ArrayList<Parameter> parameters, ArrayList<String> text) {
        super("Query", "QuerySet");
        this.parameters = parameters;
        this.text = text;
    }
}
