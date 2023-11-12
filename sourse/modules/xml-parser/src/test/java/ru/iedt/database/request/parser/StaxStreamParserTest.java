package ru.iedt.database.request.parser;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URL;


@QuarkusTest
public class StaxStreamParserTest {

    @Test
    public void testPing() throws Exception {
        URL file = StaxStreamParserTest.class.getResource("/TEST_STORE/TEST.xml");
        if (file == null) throw new RuntimeException("Nor found file TEST.xml");
        System.out.println(StaxStreamParser.parseDefinitions(file.toURI()));
    }
}