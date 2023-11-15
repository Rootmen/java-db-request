package ru.iedt.database.request.structures.nodes.v3;

import com.fasterxml.jackson.databind.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Definition {

    private HashMap<String, Template> template = new HashMap<>();
    private HashMap<String, QuerySet> querySet = new HashMap<>();

    public void setQuerySetArrayList(HashMap<String, QuerySet> querySetMap) {
        this.querySet = querySetMap;
    }

    public void setSqlArrayList(HashMap<String, Template> templateMap) {
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
            /* return  String.format("{ \"template\": %s,  \"querySet\": %s}",
                    templateString,
                    querySetString)
            .replace("\\n", "")
            .replaceAll("\\s+", " ");*/
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
