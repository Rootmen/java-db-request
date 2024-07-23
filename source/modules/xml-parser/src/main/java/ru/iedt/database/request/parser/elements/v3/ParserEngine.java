package ru.iedt.database.request.parser.elements.v3;

import java.io.InputStream;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import ru.iedt.database.request.parser.elements.v3.engine.ParserEngineDefinition;
import ru.iedt.database.request.parser.elements.v3.stax.StaxStreamProcessor;
import ru.iedt.database.request.structures.nodes.v3.Elements;

public class ParserEngine {

    public static Elements.Definition parsingXml(InputStream file) {
        if (file == null) throw new RuntimeException();

        // Валидация
        // Exception parserError = StaxStreamValidator.staxStreamValidateSchema(paths);
        // if (parserError != null) throw parserError;

        try (StaxStreamProcessor processor = new StaxStreamProcessor(file)) {
            XMLStreamReader reader = processor.getReader();
            return ParserEngineDefinition.parseDefinitionNode(reader);
        } catch (XMLStreamException e) {
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
