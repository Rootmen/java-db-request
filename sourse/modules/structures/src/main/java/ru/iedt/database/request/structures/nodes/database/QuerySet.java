package ru.iedt.database.request.structures.nodes.database;

import ru.iedt.database.request.structures.base.Node;

import java.util.ArrayList;

public class QuerySet extends Node {

    ArrayList<Query> queries;
    Connection connection;
    public QuerySet() {
        super("QuerySet");
        this.queries = new ArrayList<>();
    }

    public QuerySet(ArrayList<Query> queries) {
        super("QuerySet");
        this.queries = queries;
    }

    public void addQueries(Query query) {
        queries.add(query);
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public String toString() {
        return "QuerySet {" +
                "\n\tqueries=" + queries +
                ",\n\tconnection=" + connection +
                "\n}";
    }
}
