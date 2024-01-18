package ru.iedt.database.request.parser.elements.v3.engine;

import ru.iedt.database.request.parser.elements.v3.Attributes;
import ru.iedt.database.request.parser.elements.v3.Nodes;
import ru.iedt.database.request.parser.elements.v3.ParserEngine;
import ru.iedt.database.request.structures.nodes.v3.edit.TemplateClass;
import ru.iedt.database.request.structures.nodes.v3.edit.TemplateEditable;
import ru.iedt.database.request.structures.nodes.v3.Template;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.HashMap;

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
            String elementName = ParserEngine.getElementName(reader, readCode);
            if (Nodes.TEMPLATE.equals(elementName) && readCode == XMLStreamConstants.START_ELEMENT) {
                processTemplate(reader, templateMap);
            } else if (Nodes.TEMPLATES.equals(elementName) && readCode == XMLStreamConstants.END_ELEMENT) {
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
        TemplateClass template = new TemplateClass(new StringBuilder(value), id);

        if (templateMap.containsKey(template.getId())) {
            throw new RuntimeException(String.format("Шаблон с ID '%s' уже существует в хранилище", template.getId()));
        }

        templateMap.put(template.getId(), template);
    }
}
