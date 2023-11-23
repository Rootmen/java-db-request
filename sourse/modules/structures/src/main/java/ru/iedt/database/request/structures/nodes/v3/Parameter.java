package ru.iedt.database.request.structures.nodes.v3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.HashMap;

public class Parameter {
    private final String defaultValue;
    private final String parameterName;
    private final String parameterType;

    HashMap<String, String> whenMap = new HashMap<>();

    public Parameter(String defaultValue, String parameterName, String parameterType) {
        this.defaultValue = defaultValue;
        this.parameterName = parameterName;
        this.parameterType = parameterType;
    }

    public Parameter setWhenMap(HashMap<String, String> whenMap) {
        this.whenMap = whenMap;
        return this;
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            return String.format(
                    "{ \"parameterName\":\"%s\", \"parameterType\":\"%s\", \"defaultValue\":\"%s\", \"whenMap\":%s }",
                    parameterName,
                    parameterType,
                    defaultValue,
                    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(whenMap));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
