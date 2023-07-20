package ru.iedt.database.request.structures.xml.parser;

import ru.iedt.database.request.structures.nodes.database.QuerySet;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;

public class StaxXmlParser {


    public ArrayList<QuerySet> parseDefinitions(InputStream io) {
        try (StaxStreamProcessor processor = new StaxStreamProcessor(io)) {
            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT &&  "TextParam".equals(reader.getLocalName())) {
                    System.out.println(reader.getAttributeValue(null, "ID"));
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
