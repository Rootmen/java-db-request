package ru.iedt.database.request.structures.nodes.v3.node;

import ru.iedt.database.request.structures.nodes.v3.Elements;

import java.util.HashMap;
import java.util.Map;

public class Definition  implements Elements.Definition {

    protected final Map<String, Elements.Template> template = new HashMap<>();
    protected final Map<String, Elements.QuerySet> querySet = new HashMap<>();

    public Map<String, Elements.Template> getTemplate() {
        return template;
    }

    public Map<String, Elements.QuerySet> getQuerySet() {
        return querySet;
    }

    public final void setQuerySetArrayList(Map<String, Elements.QuerySet> querySetMap) {
        this.querySet.clear();
        this.querySet.putAll(querySetMap);
    }

    public void setSqlArrayList(Map<String, Elements.Template> templateMap) {
        this.template.clear();
        this.template.putAll(templateMap);
    }

    @Override
    public String toString() {
        return  String.format("[template=%s, querySet=%s]", template, querySet);
    }



}
