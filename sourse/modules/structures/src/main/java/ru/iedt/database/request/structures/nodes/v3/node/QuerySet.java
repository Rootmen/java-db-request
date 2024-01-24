package ru.iedt.database.request.structures.nodes.v3.node;

import ru.iedt.database.request.structures.nodes.v3.Elements;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.ParameterAbstract;

import java.util.*;
import java.util.stream.Collectors;

import static ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterFactory.getParameter;

public class QuerySet implements Elements.QuerySet {
    protected final List<Elements.Queries> queries = new ArrayList<>();

    protected final Map<String, Elements.Parameter<?>> parameters = new HashMap<>();

    protected final String refid;

    public QuerySet(String refid) {
        this.refid = refid;
    }

    public void addQueries(Elements.Queries query) {
        queries.add(query);
    }

    public void setParameters(Map<String, Elements.Parameter<?>> parameters) {
        this.parameters.clear();
        this.parameters.putAll(parameters);
    }

    public String getRefid() {
        return refid;
    }

    public List<Elements.Queries> getQueries() {
        return new ArrayList<>(queries);
    }

    public Map<String, Elements.Parameter<?>> getParameters() {
        return parameters.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                    Elements.Parameter<?> current = e.getValue();
                    ParameterAbstract<?> parameterAbstract = (ParameterAbstract<?>) getParameter(current.getParameterName(), current.getParameterType(), (current.getValue() == null)? null: current.getValue().toString());
                    parameterAbstract.setWhenMap(new HashMap<>(current.getWhenMap()));
                    return  parameterAbstract;
                }));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", QuerySet.class.getSimpleName() + "[", "]")
                .add("queries=" + queries)
                .add("parameters=" + parameters)
                .add("refid='" + refid + "'")
                .toString();
    }
}
