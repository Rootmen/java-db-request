package ru.iedt.database.request.parser.elements.v3;

import ru.iedt.database.request.parser.elements.v3.engine.ParserEngineDefinition;
import ru.iedt.database.request.parser.elements.v3.stax.StaxStreamProcessor;
import ru.iedt.database.request.structures.nodes.v3.Elements;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ParserEngine {

    public static Elements.Definition parsingXml(URI file) {
        if (file == null) throw new RuntimeException();
        Path paths = Paths.get(file);

        // Валидация
        // Exception parserError = StaxStreamValidator.staxStreamValidateSchema(paths);
        // if (parserError != null) throw parserError;

        try (StaxStreamProcessor processor = new StaxStreamProcessor(Files.newInputStream(paths))) {
            XMLStreamReader reader = processor.getReader();
            return ParserEngineDefinition.parseDefinitionNode(reader);
        } catch (XMLStreamException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isElement(int parseCode) {
        return !(XMLStreamConstants.START_ELEMENT == parseCode || XMLStreamConstants.END_ELEMENT == parseCode);
    }

    public static String getElementName(XMLStreamReader reader, int parseCode) {
        if (!(XMLStreamConstants.START_ELEMENT == parseCode || XMLStreamConstants.END_ELEMENT == parseCode)) {
            return null;
        }
        return reader.getLocalName();
    }
}
