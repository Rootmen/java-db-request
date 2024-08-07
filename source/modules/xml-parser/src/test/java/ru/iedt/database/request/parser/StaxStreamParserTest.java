package ru.iedt.database.request.parser;

import io.quarkus.test.junit.QuarkusTest;
import java.net.URL;
import org.junit.jupiter.api.Test;
import ru.iedt.database.request.parser.elements.v3.ParserEngine;
import ru.iedt.database.request.structures.nodes.v3.Elements;

@QuarkusTest
public class StaxStreamParserTest {

    @Test
    public void testParser() throws Exception {
        URL file = StaxStreamParserTest.class.getResource("/example/test.xml");
        if (file == null) throw new RuntimeException("Nor found file test.xml");
        Elements.Definition definition = ParserEngine.parsingXml(file.openStream());
        System.out.println(definition);
        // Assertions.assertEquals(definition.toJson(), xmlFotmat,"Неправильно прочитано содержимое xml
        // файла");
    }
}
