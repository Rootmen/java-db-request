package com.rootmen.Database.DatabaseQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;

public class Query {
    private Connection connection = null;
    private StringBuilder query = null;
    private HashMap<String, String> parameters = null;
    private ArrayList<String> parametersArray = null;


    public Query(String idConnections, StringBuilder query, HashMap<String, String> parameters) throws SQLException {
        this.connection = Connections.getConnection(idConnections);
        this.generateQuery(query, parameters);
    }


    public JsonNode runQuery() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query.toString());
        return null;
    }


    private void generateQuery(StringBuilder text, HashMap<String, String> parameters) {
        this.parameters = parameters;
        Matcher matcher = Pattern.compile("\\$.*?\\$").matcher(text);
        while (matcher.find()) {
            String token = text.substring(matcher.start(), matcher.end());
            if (parameters.containsKey(token)) {
                this.parametersArray.add(token);
            }
        }
        this.query = new StringBuilder(text.toString().replaceAll("\\$.*?\\$", "?"));
    }
}
