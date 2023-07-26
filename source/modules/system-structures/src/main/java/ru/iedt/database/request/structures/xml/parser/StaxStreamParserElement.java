package ru.iedt.database.request.structures.xml.parser;

import ru.iedt.database.request.structures.nodes.database.Connection;
import ru.iedt.database.request.structures.nodes.database.Query;
import ru.iedt.database.request.structures.nodes.database.QuerySet;
import ru.iedt.database.request.structures.nodes.database.SQL;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class StaxStreamParserElement {

    public static QuerySet parseQuerySetNode(XMLStreamReader reader) throws XMLStreamException {
        QuerySet querySet = new QuerySet();
        while (reader.hasNext()) {
            int parserCode = reader.next();
            if (parserCode == XMLStreamConstants.START_ELEMENT && "CONNECTION".equals(reader.getLocalName())) {
                Connection connection = StaxStreamParserElement.parseConnectionNode(reader);
                querySet.setConnection(connection);
            }
            if (parserCode == XMLStreamConstants.START_ELEMENT && "SQL".equals(reader.getLocalName())) {
                Query query = StaxStreamParserElement.parseQueryNode(reader);
                querySet.addQueries(query);
            }
            if (parserCode == XMLStreamConstants.START_ELEMENT && "QUERY".equals(reader.getLocalName())) {
                Query query = StaxStreamParserElement.parseQueryNode(reader);
                querySet.addQueries(query);
            }
            if (parserCode == XMLStreamConstants.END_ELEMENT && "QuerySet".equals(reader.getLocalName())) {
                break;
            }
        }
        return querySet;
    }


    public static Connection parseConnectionNode(XMLStreamReader reader) throws XMLStreamException {
        Connection connection = new Connection(reader.getAttributeValue(null, "REFID"));
        while (reader.hasNext()) {
            int parserCode = reader.next();
            if (parserCode == XMLStreamConstants.END_ELEMENT && "CONNECTION".equals(reader.getLocalName())) {
                break;
            }
        }
        return connection;
    }

    public static Query parseQueryNode(XMLStreamReader reader) throws XMLStreamException {
        Query query = new Query();
        while (reader.hasNext()) {
            int parserCode = reader.next();
            if (parserCode == XMLStreamConstants.END_ELEMENT && "QUERY".equals(reader.getLocalName())) {
                break;
            }
        }
        return query;
    }

    public static SQL parseSqlNode(XMLStreamReader reader) throws XMLStreamException {
        SQL sql = new SQL();
        sql.setValue(new StringBuilder(reader.getElementText()));
        sql.setRefid(reader.getAttributeValue(null, "REFID"));
        while (reader.hasNext()) {
            int parserCode = reader.next();
            if (parserCode == XMLStreamConstants.END_ELEMENT && "SQL".equals(reader.getLocalName())) {
                break;
            }
        }
        return sql;
    }
}
