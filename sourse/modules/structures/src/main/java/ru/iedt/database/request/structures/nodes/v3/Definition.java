package ru.iedt.database.request.structures.nodes.v3;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Definition {

    private Map<String, ? extends Template> template = new HashMap<>();
    private Map<String, ? extends QuerySet> querySet = new HashMap<>();

    public final void setQuerySetArrayList(Map<String, ? extends QuerySet> querySetMap) {
        this.querySet = querySetMap;
    }

    public void setSqlArrayList(Map<String, ? extends Template> templateMap) {
        this.template = templateMap;
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            StringBuilder templateString = new StringBuilder("{ ");
            StringBuilder querySetString = new StringBuilder("[ ");
            ArrayList<Template> collectionTemplate = new ArrayList<>(this.template.values());
            for (int g = 0; g < collectionTemplate.size(); g++) {
                Template template = collectionTemplate.get(g);
                templateString.append(template);
                if (g != collectionTemplate.size() - 1) {
                    templateString.append(",");
                }
            }
            templateString.append(" }");
            ArrayList<QuerySet> collectionQuerySet = new ArrayList<>(this.querySet.values());
            for (int g = 0; g < collectionQuerySet.size(); g++) {
                QuerySet querySet = collectionQuerySet.get(g);
                querySetString.append(querySet);
                if (g != collectionQuerySet.size() - 1) {
                    querySetString.append(",");
                }
            }
            querySetString.append(" ]");

            Object jsonObject = mapper.readValue(
                    String.format("{ \"template\": %s,  \"querySet\": %s}", templateString, querySetString)
                            .replace("\\n", "")
                            .replaceAll("\\s+", " "),
                    Object.class);

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
