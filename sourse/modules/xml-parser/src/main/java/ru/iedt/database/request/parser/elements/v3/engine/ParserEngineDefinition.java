package ru.iedt.database.request.parser.elements.v3.engine;

import ru.iedt.database.request.parser.elements.v3.Nodes;
import ru.iedt.database.request.parser.elements.v3.ParserEngine;
import ru.iedt.database.request.structures.nodes.v3.edit.DefinitionEditable;
import ru.iedt.database.request.structures.nodes.v3.edit.QuerySetEditable;
import ru.iedt.database.request.structures.nodes.v3.edit.TemplateEditable;
import ru.iedt.database.request.structures.nodes.v3.QuerySet;
import ru.iedt.database.request.structures.nodes.v3.Template;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.HashMap;

public class ParserEngineDefinition {

    public static DefinitionEditable parseDefinitionNode(XMLStreamReader reader) throws XMLStreamException {
        DefinitionEditable definition = new DefinitionEditable();
        HashMap<String, QuerySet> querySetMap = new HashMap<>();

        while (reader.hasNext()) {
            int readCode = reader.next();
            if (ParserEngine.isElement(readCode)) continue;
            String localName = reader.getLocalName();
            if (Nodes.QUERY_SET.equals(localName)) {
                QuerySetEditable querySet = ParserEngineQuerySet.parseQuerySetNode(reader);
                String refid = querySet.getRefid();
                if (querySetMap.containsKey(refid)) {
                    throw new RuntimeException(String.format("QuerySet refid '%s' в хранилище дублируется", refid));
                }
                querySetMap.put(refid, querySet);
            } else if (Nodes.TEMPLATES.equals(localName)) {
                HashMap<String, Template> templates = ParserEngineTemplate.parseTemplatesNode(reader);
                definition.setSqlArrayList(templates);
            }
        }
        definition.setQuerySetArrayList(querySetMap);
        return definition;
    }
}
