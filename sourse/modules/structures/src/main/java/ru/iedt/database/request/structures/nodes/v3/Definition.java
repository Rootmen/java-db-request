package ru.iedt.database.request.structures.nodes.v3;

import java.util.HashMap;
import java.util.Map;

public class Definition {

    protected final Map<String, Template> template = new HashMap<>();
    protected final Map<String, QuerySet> querySet = new HashMap<>();

    public Map<String, Template> getTemplate() {
        return template;
    }

    public Map<String, QuerySet> getQuerySet() {
        return querySet;
    }

    @Override
    public String toString() {
        return  String.format("[template=%s, querySet=%s]", template, querySet);
    }

}
