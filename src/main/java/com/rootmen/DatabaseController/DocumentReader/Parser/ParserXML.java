package com.rootmen.DatabaseController.DocumentReader.Parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.rootmen.DatabaseController.DocumentReader.Parser.Utils.Parser;
import com.rootmen.DatabaseController.Entities.Parameter.ParameterClasses.Parameter;
import com.rootmen.DatabaseController.Entities.QuerySet;

import javax.management.Query;
import java.io.IOException;

public class ParserXML extends Parser {
    ParserXML(String patch) throws IOException {
        super(patch);
    }


    public static void main(String[] args) throws IOException {
        String patch = "C:\\Users\\Rootmen\\Documents\\GitHub\\JSON-db-request\\src\\main\\resources\\query\\query.xml";
        new ParserXML(patch);
    }

    @Override
    public void parseFile(String patch) {

    }

    @Override
    public Parameter parseParameter(JsonNode parameter) {
        return null;
    }

    @Override
    public Query parseQuery(JsonNode query) {
        return null;
    }

    @Override
    public QuerySet parseQuerySet(JsonNode querySet) {
        return null;
    }
}
