package ru.iedt.database.request.parser;

public class StaxStreamParser {

    /* public static Definition parseDefinitions(URI file) throws Exception {
        //TODO действия при ошибки парсера
        if (file == null) throw new RuntimeException();
        Path paths = Paths.get(file);
        // Exception parserError = StaxStreamValidator.staxStreamValidateSchema(paths);
        // if (parserError != null) throw parserError;

        try (StaxStreamProcessor processor = new StaxStreamProcessor(Files.newInputStream(paths))) {
            Definition definition = new Definition();
            HashMap<String, QuerySet> querySetsMap = new HashMap<>();
            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) {
                int readCode = reader.next();
                if (readCode != XMLStreamConstants.START_ELEMENT) continue;
                String localName = reader.getLocalName();
                if ("queryset".equals(localName)) {
                    QuerySet querySet = ParserQuerySet.parseQuerySetNode(reader);
                    if (querySetsMap.containsKey(querySet.getRefid()))
                        throw new RuntimeException(String.format("QuerySet refid '%s' в хранилище дублируется", querySet.getRefid()));
                    querySetsMap.put(querySet.getRefid(), querySet);
                } else if ("templates".equals(localName)) {
                    HashMap<String, Template> templates = ParserTemplate.parseTemplatesNode(reader);
                    definition.setSqlArrayList(templates);
                }
            }
            definition.setQuerySetArrayList(querySetsMap);
            return definition;
        } catch (XMLStreamException | IOException e) {
            throw new RuntimeException(e);
        }
    }*/
}
