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

    public void setWhenMap(HashMap<String, String> whenMap) {
        this.whenMap = whenMap;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getParameterType() {
        return parameterType;
    }

    public HashMap<String, String> getWhenMap() {
        return whenMap;
    }

    @Override
    public String toString() {
        return String.format(
                "[parameterName=%s, parameterType=%s, defaultValue=%s, whenMap=%s]",
                parameterName, parameterType, defaultValue, whenMap);
    }
}
