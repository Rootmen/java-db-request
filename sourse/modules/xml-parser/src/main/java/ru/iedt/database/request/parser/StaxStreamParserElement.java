package ru.iedt.database.request.parser;

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
            } else if (parserCode == XMLStreamConstants.START_ELEMENT && "QUERY".equals(reader.getLocalName())) {
                Query query = StaxStreamParserElement.parseQueryNode(reader);
                querySet.addQueries(query);
            } else if (parserCode == XMLStreamConstants.END_ELEMENT && "QuerySet".equals(reader.getLocalName())) {
                break;   
            }
        }
        return querySet;
    }


    public static Connection parseConnectionNode(XMLStreamReader reader) throws XMLStreamException {
        Connection connection = new Connection(reader.getAttributeValue(null, "REFID"), reader.getAttributeValue(null, "name"));
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
            if (parserCode == XMLStreamConstants.START_ELEMENT && "SQL".equals(reader.getLocalName())) {
                SQL sql = StaxStreamParserElement.parseSqlNode(reader);
                query.addSql(sql);
            } else if (parserCode == XMLStreamConstants.END_ELEMENT && "QUERY".equals(reader.getLocalName())) {
                break;
            }
        }
        return query;
    }

    public static SQL parseSqlNode(XMLStreamReader reader) throws XMLStreamException {
        SQL sql = new SQL();
        sql.setRefId(reader.getAttributeValue(null, "REFID"));
        sql.setValue(reader.getElementText());
        while (reader.hasNext()) {
            int parserCode = reader.next();
            if (parserCode == XMLStreamConstants.END_ELEMENT && "SQL".equals(reader.getLocalName())) {
                break;
            }
        }
        return sql;
    }
}
