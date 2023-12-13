package ru.iedt.database.request.structures.nodes.v3;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class Definition {

    private Map<String, ? extends Template> template = new HashMap<>();
    private Map<String, ? extends QuerySet> querySet = new HashMap<>();

    public final void setQuerySetArrayList(Map<String, ? extends QuerySet> querySetMap) {
        this.querySet = querySetMap;
    }

    public void setSqlArrayList(Map<String, ? extends Template> templateMap) {
        this.template = templateMap;
    }

    public Map<String, ? extends Template> getTemplate() {
        return template;
    }

    public Map<String, ? extends QuerySet> getQuerySet() {
        return querySet;
    }

    @Override
    public String toString() {
        return  String.format("[template=%s, querySet=%s]", template, querySet);
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
