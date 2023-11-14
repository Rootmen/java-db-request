package ru.iedt.database.request.structures.nodes.database;

import ru.iedt.database.request.structures.base.Node;

import java.util.ArrayList;
import java.util.HashMap;

public class QuerySet {

    ArrayList<Query> queries;
    HashMap<String,Connection> connections;

    String refid;
    public QuerySet(String refid) {
        this.queries = new ArrayList<>();
        this.refid = refid;
    }

    public void addQueries(Query query) {
        queries.add(query);
    }

    public void setConnections(HashMap<String,Connection> connection) {
        this.connections = connection;
    }

    public String getRefid() {
        return refid;
    }

    @Override
    public String toString() {
        return String.format("QuerySet { queries='%s', connection='%s' }",queries, connections) ;
    }
}
