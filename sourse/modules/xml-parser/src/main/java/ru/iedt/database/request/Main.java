package ru.iedt.database.request;

import java.net.URISyntaxException;
import java.util.Objects;

import static ru.iedt.database.request.parser.StaxStreamParser.parseDefinitions;

public class Main {
    public static void main(String[] args) throws URISyntaxException {
        try {
            parseDefinitions(Objects.requireNonNull(Main.class.getResource("./TEST_STORE/TEST.xml")).toURI());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}