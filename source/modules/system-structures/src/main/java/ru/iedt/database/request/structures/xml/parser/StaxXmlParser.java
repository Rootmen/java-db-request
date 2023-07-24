package ru.iedt.database.request.structures.xml.parser;

import org.xml.sax.SAXException;
import ru.iedt.database.request.structures.nodes.database.QuerySet;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class StaxXmlParser {


    public ArrayList<QuerySet> parseDefinitions(URL file) {
        if (xmlValidateSchema(file)) {

        }

        try (StaxStreamProcessor processor = new StaxStreamProcessor(Files.newInputStream(Paths.get(patch)))) {

            ArrayList<QuerySet> result = new ArrayList<>();
            XMLStreamReader reader = processor.getReader();

            while (reader.hasNext()) {
                if (reader.next() == XMLStreamConstants.START_ELEMENT && "QuerySet".equals(reader.getLocalName())) {
                    System.out.println(reader.getAttributeValue(null, "ID"));
                }
            }
        } catch (XMLStreamException | IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    boolean xmlValidateSchema(URL file) {
        String patch = file.getFile();
        try (FileInputStream fileInputStream = new FileInputStream(patch)) {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(Objects.requireNonNull(this.getClass().getResource("/Definitions.xsd")).toURI().getPath()));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(fileInputStream));
            return true;
        } catch (IOException | URISyntaxException | SAXException e) {
            e.printStackTrace();
            return false;
        }
    }
}
