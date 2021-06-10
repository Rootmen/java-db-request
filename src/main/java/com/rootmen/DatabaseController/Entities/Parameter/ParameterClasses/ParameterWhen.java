package com.rootmen.DatabaseController.Entities.Parameter.ParameterClasses;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;

import static com.rootmen.DatabaseController.DocumentReader.Parser.Utils.ParserMethods.convertValue;


public class ParameterWhen {
    HashMap<String, String> dictionary = new HashMap<>();

    public ParameterWhen(JsonNode node) {
        this.dictionary = convertValue(node, new TypeReference<HashMap<String, String>>() {
        });
    }

    public ParameterWhen(HashMap<String, String> node) {
        this.dictionary = node;
    }

    public ParameterWhen() {
    }

    public String getNewValue(String currentValue) {
        return dictionary.get(currentValue);
    }
}
