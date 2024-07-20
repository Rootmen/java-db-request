package ru.iedt.database.request.parser.elements.v3.engine;

import java.util.HashMap;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import ru.iedt.database.request.parser.elements.v3.Nodes;
import ru.iedt.database.request.parser.elements.v3.ParserEngine;
import ru.iedt.database.request.structures.nodes.v3.Elements;
import ru.iedt.database.request.structures.nodes.v3.node.Definition;

public class ParserEngineDefinition {

  public static Elements.Definition parseDefinitionNode(XMLStreamReader reader)
      throws XMLStreamException {
    Definition definition = new Definition();
    HashMap<String, Elements.QuerySet> querySetMap = new HashMap<>();

    while (reader.hasNext()) {
      int readCode = reader.next();
      if (ParserEngine.isElement(readCode)) continue;
      String localName = reader.getLocalName();
      if (Nodes.QUERY_SET.equals(localName)) {
        Elements.QuerySet querySet = ParserEngineQuerySet.parseQuerySetNode(reader);
        String refid = querySet.getRefid();
        if (querySetMap.containsKey(refid)) {
          throw new RuntimeException(
              String.format("QuerySet refid '%s' в хранилище дублируется", refid));
        }
        querySetMap.put(refid, querySet);
      } else if (Nodes.TEMPLATES.equals(localName)) {
        HashMap<String, Elements.Template> templates =
            ParserEngineTemplate.parseTemplatesNode(reader);
        definition.setSqlArrayList(templates);
      }
    }
    definition.setQuerySetArrayList(querySetMap);
    return definition;
  }
}
