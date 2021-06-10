package com.rootmen.DatabaseController.Entities.Parameter.ParameterClasses;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;

public class ParameterPOJO implements Serializable {
    public String ref;
    public String id;
    public String raw;
    public String name;
    public String type;
    public String query;
    public String nodeType;
    public JsonNode when;


    public boolean isValid() {
        return (raw != null || query != null) && id != null && name != null && type != null && nodeType.contentEquals("parameter");
    }

    public ParameterWhen getParameterWhen() {
        if (when == null) {
            return new ParameterWhen();
        }
        return new ParameterWhen(when);
    }
}
