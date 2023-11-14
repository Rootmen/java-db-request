package ru.iedt.database.request.structures.nodes.database;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
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
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.getSerializerProvider().setNullKeySerializer(new KeySerializer());
            Object jsonObject = mapper.readValue(
                    String.format("{ \"template\": %s,  \"querySet\": %s}",
                                    mapper.writeValueAsString(template),
                                    mapper.writeValueAsString(querySet))
                            .replace("\\n", "")
                            .replaceAll("\\s+", " "),
                    Object.class);
            return mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(jsonObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    static class KeySerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object nullKey, JsonGenerator jsonGenerator, SerializerProvider unused)
                throws IOException, JsonProcessingException {
            jsonGenerator.writeFieldName("");
        }
    }

}
