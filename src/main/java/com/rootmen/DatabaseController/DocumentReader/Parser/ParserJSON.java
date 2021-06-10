package com.rootmen.DatabaseController.DocumentReader.Parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.rootmen.DatabaseController.Entities.Parameter.ParameterClasses.Parameter;
import com.rootmen.DatabaseController.Entities.Parameter.ParameterFactory;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

abstract public class ParserJSON {
    public JsonNode root;
    public JsonNode refs;
    public Connection connection;
    HashMap<String, Parameter> parametersGlobal = new HashMap<>();

    protected void parseQuerySet(JsonNode querySet) {
        boolean hasRefs = refs != null;
        if (querySet == null) {
            throw new RuntimeException("QuerySet is not find");
        }
        if (connection == null) {
            throw new RuntimeException("Connection to database is null");
        }
        this.parseArray(querySet.get("parameters"));
        for (Map.Entry<String, Parameter> entry : parametersGlobal.entrySet()) {
            System.out.println(entry.getValue().getValue());
        }
    }


    private void parseArray(JsonNode parameters) {
        for (int g = 0; g < parameters.size(); g++) {
            if (parameters.get(g).get("ref") == null) {
                this.nextElementRun(parameters.get(g));
            } else if (parameters.get(g).get("ref") != null) {
                JsonNode refsObject = this.refs.get(String.valueOf(parameters.get(g).get("ref")));
                this.nextElementRun(refsObject);
            }
        }
    }

    private void nextElementRun(JsonNode element) {
        String nodeType = String.valueOf(element.get("nodeType")).toLowerCase(Locale.ROOT);
        switch (nodeType) {
            case "parameter": {
                this.nextElementParameter(element);
            }
            case "query": {

            }
            default:
        }
    }

    private void nextElementParameter(JsonNode element) {
        String nodeType = String.valueOf(element.get("nodeType")).toLowerCase(Locale.ROOT);
        if (!nodeType.equals("parameter")) {
            throw new RuntimeException("Parsing element type is not parameter");
        } else if (element.get("ref") != null) {
            element = this.refs.get(String.valueOf(element.get("ref")));
        }
        Parameter newParameter = ParameterFactory.getParameter(element, connection, parametersGlobal);
        parametersGlobal.put(newParameter.getID(), newParameter);
    }

    private void nextElementQuery(JsonNode element) {
        for (int g = 0; g < element.size(); g++) {
            if (element.get(g).get("ref") == null) {
                this.nextElementRun(element.get(g));
            } else if (element.get(g).get("ref") != null) {
                this.nextElementRun(this.refs.get(String.valueOf(element.get("ref"))));
            }
        }
    }

}
