package ru.iedt.database.request.structures.nodes.v3;

import java.util.ArrayList;

public class QuerySet {

    ArrayList<Queries> queries;

    ArrayList<Parameter> parameters = new ArrayList<>();

    String refid;

    public QuerySet(String refid) {
        this.queries = new ArrayList<>();
        this.refid = refid;
    }

    public void addQueries(Queries query) {
        queries.add(query);
    }

    public void setParameters(ArrayList<Parameter> parameters) {
        this.parameters = parameters;
    }

    public String getRefid() {
        return refid;
    }

    public ArrayList<Queries> getQueries() {
        return queries;
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return String.format("[id=%s, parameters=%s, queries=%s]", this.refid, this.parameters, this.queries);
    }
}
