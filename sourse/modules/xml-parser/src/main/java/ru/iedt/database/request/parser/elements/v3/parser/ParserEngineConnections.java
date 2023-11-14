package ru.iedt.database.request.parser.elements.v3.parser;

import ru.iedt.database.request.parser.elements.v3.Attributes;
import ru.iedt.database.request.parser.elements.v3.Nodes;
import ru.iedt.database.request.structures.nodes.database.Connection;
import ru.iedt.database.request.structures.nodes.database.Template;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.HashMap;

public class ParserEngineConnections {
    public static HashMap<String, Connection> parseConnectionsNode(XMLStreamReader reader) throws XMLStreamException {
        HashMap<String, Connection> templateMap = new HashMap<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String elementName = reader.getLocalName();

                if (Nodes.CONNECTION.equals(elementName)) {
                    processConnections(reader, templateMap);
                } else if (Nodes.CONNECTIONS.equals(elementName)) {
                    break;
                }
            }
        }

        return templateMap;
    }

    private static void processConnections(XMLStreamReader reader, HashMap<String, Connection> templateMap) throws XMLStreamException {
        String refid = reader.getAttributeValue(null, Attributes.Connection.REFID);
        String name = reader.getAttributeValue(null, Attributes.Connection.NAME);
        boolean defaultConnection = reader.getAttributeValue(null, Attributes.Connection.DEFAULT).equals("true");
        Connection connection = new Connection(refid, name);
        if (templateMap.containsKey(connection.getRef())) {
            throw new RuntimeException(String.format("Подключение с ID '%s' уже существует", connection.getRef()));
        }
        templateMap.put(connection.getRef(), connection);
        if (defaultConnection) {
            if (templateMap.containsKey("default")) {
                throw new RuntimeException(String.format("Подключение по умолчанию уже существует - имя дублирующего подключения %s", connection.getName()));
            }
            templateMap.put("default", connection);
        }

    }

}
