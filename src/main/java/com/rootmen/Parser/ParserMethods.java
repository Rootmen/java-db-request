package com.rootmen.Parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class ParserMethods {

    public static ObjectMapper generatorObjectMapper() {
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        objectMapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        objectMapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
        return objectMapper;
    }

    public static JsonNode parseDocumentXML(String filename) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(new File(filename), JsonNode.class);
    }


    public static JsonNode parseDocumentJSON(String filename) throws IOException {
        ObjectMapper objectMapper = generatorObjectMapper();
        return objectMapper.readTree(Paths.get(filename).toFile());
    }

    public static <T> T generatedPOJO(JsonNode parameter, Class<T> type) {
        ObjectMapper mapper = generatorObjectMapper();
        return mapper.convertValue(parameter, type);
    }

    public static <T> T convertValue(Object parameter, TypeReference<?> toValueTypeRef) {
        ObjectMapper mapper = generatorObjectMapper();
        return (T) mapper.convertValue(parameter,  toValueTypeRef);
    }

    public static <T> T convertValue(Object parameter, JavaType toValueType) {
        ObjectMapper mapper = generatorObjectMapper();
        return mapper.convertValue(parameter,  toValueType);
    }
}