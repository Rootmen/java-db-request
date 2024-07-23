package ru.iedt.database.request.parser.elements.v3.engine;

import java.util.Map;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import ru.iedt.database.request.parser.elements.v3.Attributes;
import ru.iedt.database.request.parser.elements.v3.Nodes;
import ru.iedt.database.request.parser.elements.v3.ParserEngine;
import ru.iedt.database.request.structures.nodes.v3.Elements;
import ru.iedt.database.request.structures.nodes.v3.node.QuerySet;

public class ParserEngineQuerySet {

    /**
     * Парсинг XML-узла с запросами для создания объекта QuerySet.
     *
     * @param reader XMLStreamReader для разбора XML-узла с запросами.
     * @return Объект QuerySet, содержащий извлеченные соединения и запросы.
     * @throws XMLStreamException в случае ошибок разбора XML.
     */
    public static Elements.QuerySet parseQuerySetNode(XMLStreamReader reader) throws XMLStreamException {
        QuerySet querySet = new QuerySet(reader.getAttributeValue(null, Attributes.QuerySet.ID));

        while (reader.hasNext()) {
            int parserCode = reader.next();

            if (ParserEngine.isElement(parserCode)) {
                continue;
            }

            String localName = reader.getLocalName();

            if (parserCode == XMLStreamConstants.START_ELEMENT && Nodes.PARAMETERS.equals(localName)) {
                Map<String, Elements.Parameter<?>> parameters = ParserEngineParameters.parseParametersNode(reader);
                querySet.setParameters(parameters);
            } else if (parserCode == XMLStreamConstants.START_ELEMENT && Nodes.QUERY.equals(localName)) {
                Elements.Queries query = ParserEngineQueries.parseQuerySetNode(reader);
                querySet.addQueries(query);
            } else if (parserCode == XMLStreamConstants.END_ELEMENT && Nodes.QUERY_SET.equals(localName)) {
                break;
            }
        }
        return querySet;
    }
}
