package ru.iedt.database.request.parser.elements.v3.parser;

import java.util.HashMap;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import ru.iedt.database.request.parser.elements.v3.Attributes;
import ru.iedt.database.request.parser.elements.v3.Nodes;
import ru.iedt.database.request.parser.elements.v3.ParserEngine;
import ru.iedt.database.request.structures.nodes.v3.Template;

/**
 * Класс, выполняющий разбор XML для извлечения информации о шаблонах.
 */
public class ParserEngineTemplate {

    /**
     * Выполняет разбор XML-контента для извлечения информации о шаблонах.
     *
     * @param reader XMLStreamReader для разбора XML-контента.
     * @return HashMap, содержащий извлеченные шаблоны.
     * @throws XMLStreamException в случае ошибок разбора XML.
     */
    public static HashMap<String, Template> parseTemplatesNode(XMLStreamReader reader) throws XMLStreamException {
        HashMap<String, Template> templateMap = new HashMap<>();

        while (reader.hasNext()) {
            int readCode = reader.next();
            if (ParserEngine.isElement(readCode)) continue;
            String elementName = reader.getLocalName();
            if (Nodes.TEMPLATE.equals(elementName)) {
                processTemplate(reader, templateMap);
            } else if (Nodes.TEMPLATES.equals(elementName)) {
                break;
            }
        }
        return templateMap;
    }

    /**
     * Обрабатывает отдельный элемент шаблона и добавляет его в карту шаблонов.
     *
     * @param reader      XMLStreamReader для обработки элемента шаблона.
     * @param templateMap Карта для хранения извлеченных шаблонов.
     * @throws XMLStreamException в случае ошибок разбора XML.
     */
    private static void processTemplate(XMLStreamReader reader, HashMap<String, Template> templateMap)
            throws XMLStreamException {
        String id = reader.getAttributeValue(null, Attributes.Template.ID);
        String value = reader.getElementText();
        Template template = new Template(new StringBuilder(value), id);

        if (templateMap.containsKey(template.getId())) {
            throw new RuntimeException(String.format("Шаблон с ID '%s' уже существует в хранилище", template.getId()));
        }

        templateMap.put(template.getId(), template);
    }
}
