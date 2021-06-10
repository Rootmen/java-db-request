package com.rootmen.DatabaseController.Utils.DocumentParser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;

public class Parser {

    public static ObjectMapper generatorObjectMapper() {
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        objectMapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        objectMapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
        objectMapper.enable(JsonParser.Feature.ALLOW_MISSING_VALUES);
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
        return objectMapper;
    }

    public static JsonNode parseDocument(String filename) throws IOException {
        ObjectMapper objectMapper = generatorObjectMapper();
        return objectMapper.readTree(Paths.get(filename).toFile());
    }

    public static <T> T generatedPOJO(JsonNode parameter, Class<T> type) {
        ObjectMapper mapper = generatorObjectMapper();
        return mapper.convertValue(parameter, type);
    }

    public static <T> T convertValue(Object parameter, TypeReference<?> toValueTypeRef) {
        ObjectMapper mapper = generatorObjectMapper();
        return mapper.convertValue(parameter, toValueTypeRef);

    }
}
