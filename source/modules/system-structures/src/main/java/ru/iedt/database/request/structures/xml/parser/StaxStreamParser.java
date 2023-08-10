package ru.iedt.database.request.structures.xml.parser;

import ru.iedt.database.request.structures.nodes.database.Definition;
import ru.iedt.database.request.structures.nodes.database.QuerySet;
import ru.iedt.database.request.structures.nodes.database.SQL;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class StaxStreamParser {


    public Definition parseDefinitions(URI file) throws Exception {
        //TODO действия при ошибки парсера
        if (file == null) throw new RuntimeException();
        Path paths = Paths.get(file);
        Exception parserError = StaxStreamValidator.staxStreamValidateSchema(paths);
        if (parserError != null) throw parserError;

        try (StaxStreamProcessor processor = new StaxStreamProcessor(Files.newInputStream(paths))) {
            ArrayList<QuerySet> querySetsArrayList = new ArrayList<>();
            ArrayList<SQL> sqlArrayList = new ArrayList<>();
            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) {
                int readCode = reader.next();
                if (readCode == XMLStreamConstants.START_ELEMENT && "QuerySet".equals(reader.getLocalName())) {
                    querySetsArrayList.add(StaxStreamParserElement.parseQuerySetNode(reader));
                } else if (readCode == XMLStreamConstants.START_ELEMENT && "SQL".equals(reader.getLocalName())) {
                    sqlArrayList.add(StaxStreamParserElement.parseSqlNode(reader));
                }
            }
            Definition definition = new Definition();
            definition.setQuerySetArrayList(querySetsArrayList);
            definition.setSqlArrayList(sqlArrayList);
            return definition;
        } catch (XMLStreamException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
