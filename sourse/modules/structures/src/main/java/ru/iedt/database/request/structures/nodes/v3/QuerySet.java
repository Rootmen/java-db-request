package ru.iedt.database.request.structures.nodes.v3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuerySet {

    protected final List<Queries> queries = new ArrayList<>();

    protected final Map<String, Parameter> parameters = new HashMap<>();

    protected final String refid;

    public QuerySet(String refid) {
        this.refid = refid;
    }

    public String getRefid() {
        return refid;
    }

    public List<Queries> getQueries() {
        return queries;
    }

    public Map<String, Parameter> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return String.format("[id=%s, parameters=%s, queries=%s]", this.refid, this.parameters, this.queries);
    }
}
