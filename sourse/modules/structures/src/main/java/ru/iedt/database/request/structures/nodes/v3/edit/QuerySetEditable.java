package ru.iedt.database.request.structures.nodes.v3.edit;

import ru.iedt.database.request.structures.nodes.v3.Parameter;
import ru.iedt.database.request.structures.nodes.v3.Queries;
import ru.iedt.database.request.structures.nodes.v3.QuerySet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuerySetEditable  extends QuerySet {

    ArrayList<Queries> queries;

    Map<String, Parameter>  parameters = new HashMap<>();

    String refid;

    public QuerySetEditable (String refid) {
        super(refid);
    }

    public void addQueries(Queries query) {
        queries.add(query);
    }

    public void setParameters(Map<String, ? extends Parameter> parameters) {
        this.parameters = (Map<String, Parameter>) parameters;
    }

    public String getRefid() {
        return refid;
    }

    public ArrayList<Queries> getQueries() {
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
