package com.rootmen.DatabaseController.DocumentReader.Parser.Utils;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.rootmen.DatabaseController.Entities.Parameter.ParameterClasses.Parameter;
import com.rootmen.DatabaseController.Entities.QuerySet;

import javax.management.Query;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

public abstract class Parser {
    protected JsonNode root;
    protected Connection connection;
    protected boolean isParsingComplete = false;
    protected HashMap<String, Parameter> parameters = new HashMap<>();

    protected Parser(String patch) throws IOException {
        this.parseFile(patch);
    }

    public abstract void parseFile(String patch) throws IOException;

    public String runQuerySet(String id) {
        JsonNode querySet = root.get("QuerySet");
        //printObject(root, 0);
        /*for (int g = 0; g < 0; g++) {
            ObjectNode element = querySet.get(g);
            if ()
        }*/
        return "sad";
    }

    public static void printObject(JsonNode object, int root) {
        root++;
        for (int g = 0; g < object.size(); g++) {
            JsonNode element = object.get(g);
            if (element != null) {
                System.out.println(element.getNodeType());
            }
            if (root < 3) {
                printObject(object, root);
            }
        }
    }

    public Parameter parseParameter(JsonNode parameter) {
        return null;
    }

    public Query parseQuery(JsonNode query) {
        return null;
    }

    public QuerySet parseQuerySet(JsonNode querySet) {
        return null;
    }

    public static void main(String[] args) throws IOException {
        String patch = "C:\\Users\\Rootmen\\Documents\\GitHub\\JSON-db-request\\src\\main\\resources\\query\\query.xml";
        Parser p = new Parser(patch) {
            @Override
            public void parseFile(String patch) throws IOException {
                XmlMapper xmlMapper = new XmlMapper();
                xmlMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
                xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
                LinkedHashMap<?, ?> rootd = xmlMapper.readValue(new File(patch), LinkedHashMap.class);
                //this.root = xmlMapper.readTree(new File(patch));
            }
        };
        p.runQuerySet("ASOUV_ERROR");
    }
}
