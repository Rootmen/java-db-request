package ru.iedt.database.request.structures.nodes.v3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

    public QuerySet setParameters(ArrayList<Parameter> parameters) {
        this.parameters = parameters;
        return this;
    }

    public String getRefid() {
        return refid;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return String.format(" { \"queries\":%s, \"parameters\":%s }", queries, parameters);
    }
}
