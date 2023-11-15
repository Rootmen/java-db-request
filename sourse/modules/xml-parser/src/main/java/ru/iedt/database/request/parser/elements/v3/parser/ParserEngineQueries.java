package ru.iedt.database.request.parser.elements.v3.parser;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import ru.iedt.database.request.parser.elements.v3.Attributes;
import ru.iedt.database.request.parser.elements.v3.Nodes;
import ru.iedt.database.request.parser.elements.v3.ParserEngine;
import ru.iedt.database.request.structures.nodes.v3.Queries;
import ru.iedt.database.request.structures.nodes.v3.SQL;

public class ParserEngineQueries {
    public static Queries parseQuerySetNode(XMLStreamReader reader) throws XMLStreamException {
        Queries queries = new Queries();

        while (reader.hasNext()) {
            int parserCode = reader.next();

            if (ParserEngine.isElement(parserCode)) {
                continue;
            }

            String localName = reader.getLocalName();

            if (parserCode == XMLStreamConstants.START_ELEMENT && Nodes.SQL.equals(localName)) {
                SQL sql = ParserEngineQueries.processSql(reader);
                queries.addSql(sql);
            } else if (parserCode == XMLStreamConstants.END_ELEMENT && Nodes.QUERY.equals(localName)) {
                break;
            }
        }

        return queries;
    }

    private static SQL processSql(XMLStreamReader reader) throws XMLStreamException {
        String name = reader.getAttributeValue(null, Attributes.SQL.NAME);
        String wrapper = reader.getAttributeValue(null, Attributes.SQL.WRAPPER_CLASS);
        String value = reader.getElementText();
        return new SQL(new StringBuilder(value), name, wrapper);
    }
}
