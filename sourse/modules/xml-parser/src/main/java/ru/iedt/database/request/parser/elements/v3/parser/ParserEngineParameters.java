package ru.iedt.database.request.parser.elements.v3.parser;

import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import ru.iedt.database.request.parser.elements.v3.Attributes;
import ru.iedt.database.request.parser.elements.v3.Nodes;
import ru.iedt.database.request.parser.elements.v3.ParserEngine;
import ru.iedt.database.request.structures.nodes.v3.Parameter;

public class ParserEngineParameters {

    public static ArrayList<Parameter> parseParametersNode(XMLStreamReader reader) throws XMLStreamException {
        ArrayList<Parameter> parameters = new ArrayList<>();
        while (reader.hasNext()) {
            int parserCode = reader.next();

            if (ParserEngine.isElement(parserCode)) {
                continue;
            }

            String localName = reader.getLocalName();

            if (parserCode == XMLStreamConstants.START_ELEMENT && Nodes.PARAMETER.equals(localName)) {
                Parameter parameter = processParameter(reader);
                parameters.add(parameter);
            } else if (parserCode == XMLStreamConstants.END_ELEMENT && Nodes.PARAMETERS.equals(localName)) {
                break;
            }
        }
        return parameters;
    }

    private static Parameter processParameter(XMLStreamReader reader) throws XMLStreamException {
        String parameterName = reader.getAttributeValue(null, Attributes.Parameter.NAME);
        String parameterType = reader.getAttributeValue(null, Attributes.Parameter.TYPE);
        String defaultValue = reader.getAttributeValue(null, Attributes.Parameter.DEFAULT);

        Parameter parameter = new Parameter(defaultValue, parameterName, parameterType);
        HashMap<String, String> whenMap = new HashMap<>();
        while (reader.hasNext()) {
            int parserCode = reader.next();

            if (ParserEngine.isElement(parserCode)) {
                continue;
            }

            String localName = reader.getLocalName();

            if (parserCode == XMLStreamConstants.START_ELEMENT && Nodes.WHEN.equals(localName)) {
                String whenValue = reader.getAttributeValue(null, Attributes.When.VALUE);
                String value = reader.getElementText().trim();
                if (whenMap.containsKey(whenValue)) {
                    throw new RuntimeException(String.format("When значения дублируются с value='%s'", whenValue));
                }
                whenMap.put(whenValue, value);
            }
            if (parserCode == XMLStreamConstants.START_ELEMENT && Nodes.OTHERWISE.equals(localName)) {
                String whenValue = "default";
                String value = reader.getElementText().trim();
                if (whenMap.containsKey(whenValue)) {
                    throw new RuntimeException(String.format("When значения дублируются с value='%s'", whenValue));
                }
                whenMap.put(whenValue, value);
            } else if (parserCode == XMLStreamConstants.END_ELEMENT && Nodes.PARAMETER.equals(localName)) {
                break;
            }
        }
        parameter.setWhenMap(whenMap);
        return parameter;
    }
}
