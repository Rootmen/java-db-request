package ru.iedt.database.request.structures.nodes.database;

import ru.iedt.database.request.structures.base.Node;

import java.util.ArrayList;

public class QuerySet extends Node {
    ArrayList<Query> queries;

    public QuerySet(ArrayList<Query> queries) {
        super("QuerySet", "Definition");
        this.queries = queries;
    }
}
