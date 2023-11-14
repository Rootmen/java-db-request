package ru.iedt.database.request.parser.elements.v3.parser;

import ru.iedt.database.request.parser.StaxStreamParserElement;
import ru.iedt.database.request.parser.elements.v3.Attributes;
import ru.iedt.database.request.parser.elements.v3.Nodes;
import ru.iedt.database.request.structures.nodes.database.Connection;
import ru.iedt.database.request.structures.nodes.database.Query;
import ru.iedt.database.request.structures.nodes.database.QuerySet;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.HashMap;


public class ParserEngineQuerySet {

    /**
     * Парсинг XML-узла с запросами для создания объекта QuerySet.
     *
     * @param reader XMLStreamReader для разбора XML-узла с запросами.
     * @return Объект QuerySet, содержащий извлеченные соединения и запросы.
     * @throws XMLStreamException в случае ошибок разбора XML.
     */

    public static QuerySet parseQuerySetNode(XMLStreamReader reader) throws XMLStreamException {
        QuerySet querySet = new QuerySet(reader.getAttributeValue(null,  Attributes.QuerySet.ID));

        while (reader.hasNext()) {
            int parserCode = reader.next();

            if (!(parserCode == XMLStreamConstants.START_ELEMENT || parserCode == XMLStreamConstants.END_ELEMENT)) {
                continue;
            }

            String localName = reader.getLocalName().toUpperCase();

            if (parserCode == XMLStreamConstants.START_ELEMENT && Nodes.CONNECTIONS.equals(localName)) {
                HashMap<String, Connection> connections = ParserEngineConnections.parseConnectionsNode(reader);
                querySet.setConnections(connections);
            } else if (parserCode == XMLStreamConstants.START_ELEMENT && Nodes.QUERY.equals(localName)) {
                Query query = StaxStreamParserElement.parseQueryNode(reader);
                querySet.addQueries(query);
            } else if (parserCode == XMLStreamConstants.END_ELEMENT && Nodes.QUERY_SET.equals(localName)) {
                break;
            }
        }
        return querySet;
    }
}
