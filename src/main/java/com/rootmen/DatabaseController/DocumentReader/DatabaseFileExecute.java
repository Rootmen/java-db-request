package com.rootmen.DatabaseController.DocumentReader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.rootmen.DatabaseController.Utils.DocumentParser.Parser.parseDocument;

public class DatabaseFileExecute extends ParserLogic {

    DatabaseFileExecute(String filename, String id) throws SQLException, IOException {
        initialization(filename, id);
    }

    static final String DB_URL = "jdbc:postgresql://distance-course.ru:5432/main";
    static final String USER = "authorization_app";
    static final String PASS = "ga4kHTswrjcqwWDi51QA";

    private void initialization(String filename, String id) throws IOException, SQLException {
        JsonNode json = parseDocument(filename);
        this.connection = DriverManager.getConnection(DB_URL, USER, PASS);
        this.refs = json.get("refs");
        this.root = json;
        parseQuerySet(json.get(id));
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SQLException {
        new DatabaseFileExecute("C:\\Users\\Shinderov\\Documents\\GitHub\\JSON-db-request\\src\\main\\resources\\query\\query.json5", "main");
    }
}
