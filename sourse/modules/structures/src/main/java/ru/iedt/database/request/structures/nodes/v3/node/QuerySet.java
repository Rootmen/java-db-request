package ru.iedt.database.request.structures.nodes.v3.node;

import ru.iedt.database.request.structures.nodes.v3.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuerySet implements Elements.QuerySet {
    protected final List<Elements.Queries> queries = new ArrayList<>();

    protected final Map<String, Elements.Parameter> parameters = new HashMap<>();

    protected final String refid;

    public QuerySet(String refid) {
        this.refid = refid;
    }

    public void addQueries(Elements.Queries query) {
        queries.add(query);
    }

    public void setParameters(Map<String, Elements.Parameter> parameters) {
        this.parameters.clear();
        this.parameters.putAll(parameters);
    }

    public String getRefid() {
        return refid;
    }

    public List<Elements.Queries> getQueries() {
        return queries;
    }

    public Map<String, Elements.Parameter> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return String.format("[id=%s, parameters=%s, queries=%s]", this.refid, this.parameters, this.queries);
    }
}
