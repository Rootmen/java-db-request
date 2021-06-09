package com.rootmen.DatabaseController.Entities.Parameter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;

public class ParameterWhen {
    HashMap<String, String> dictionary = new HashMap<>();

    public ParameterWhen(ObjectNode node) {
        ObjectMapper mapper = new ObjectMapper();
        this.dictionary = mapper.convertValue(node, new TypeReference<HashMap<String, String>>() {
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
