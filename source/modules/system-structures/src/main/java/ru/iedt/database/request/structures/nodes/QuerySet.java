package ru.iedt.database.request.structures.nodes;

import ru.iedt.database.request.structures.base.Node;

import java.util.ArrayList;

public class QuerySet extends Node {
    ArrayList<Query> queries;

    public QuerySet(ArrayList<Query> queries) {
        super("queryset");
        this.queries = queries;
    }
}
