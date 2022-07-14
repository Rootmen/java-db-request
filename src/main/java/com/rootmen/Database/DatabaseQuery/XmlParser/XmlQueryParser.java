package com.rootmen.Database.DatabaseQuery.XmlParser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rootmen.Database.DatabaseQuery.ConnectionsManager;
import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.Parameter;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterFactory;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterInput;
import com.rootmen.Database.DatabaseQuery.Query.QueryController;
import com.rootmen.Database.DatabaseQuery.XmlParser.Errors.ParserXMLErrors;
import com.rootmen.Database.DatabaseQuery.XmlParser.Errors.Types.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XmlQueryParser {
    SAXBuilder builder = new SAXBuilder();
    XPathFactory xpath = XPathFactory.instance();

    public JsonNode getQuery(InputStream querySetFile, String querySetName, ArrayList<ParameterInput> parameterInput, ConnectionsManager connectionsManager) throws SQLException {
        ArrayList<QueryList> queryList = getQuerySet(querySetFile, querySetName, parameterInput);
        return executeQuery(queryList, connectionsManager);
    }

    public JsonNode getQuery(String directory, String querySetName, ArrayList<ParameterInput> parameterInput) throws SQLException, ParserXMLErrors {
        //Проверка директории на правильность
        ParserXMLErrors errors = this.checkDirectory(directory);
        if (errors != null) {
            throw errors;
        }
        //Получение фала с XMLQuery запросом
        String filename = this.findFile(directory, querySetName);
        System.out.println(filename);
        return null;
        /*ArrayList<QueryList> queryList = getQuerySet(querySetFile, querySetName, parameterInput);
        return executeQuery(queryList, connectionsManager);*/
    }

    private ArrayList<QueryList> getQuerySet(InputStream querySetFile, String querySetName, ArrayList<ParameterInput> parameterInput) {
        try {
            Document document = builder.build(querySetFile);
            List<Element> querySetNodes = xpath.compile("/Definitions/QuerySet[@ID='" + querySetName + "']", Filters.element()).evaluate(document);
            if (querySetNodes.size() != 1) {
                throw new Exception("Ошибка документа");
            }
            List<Element> queryNodes = xpath.compile("QUERY", Filters.element()).evaluate(querySetNodes);
            ArrayList<QueryList> queryList = new ArrayList<>();
            HashMap<String, ParameterInput> parameterInputHasMap = new HashMap<>();
            for (ParameterInput parameter : parameterInput) {
                parameterInputHasMap.put(parameter.name, parameter);
            }
            for (Element queryNode : queryNodes) {
                queryList.add(parseQuery(queryNode, parameterInputHasMap));
            }
            return queryList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private QueryList parseQuery(Element queryNodes, HashMap<String, ParameterInput> parameterInput) throws ParameterException {
        HashMap<String, Parameter<?>> parameters = new HashMap<>();
        List<Element> parameterNodes = xpath.compile("TextParam", Filters.element()).evaluate(queryNodes);
        for (Element parameterNode : parameterNodes) {
            String name = "$" + parameterNode.getAttributeValue("name") + "$";
            String ID = parameterNode.getAttributeValue("ID");
            String type = parameterNode.getAttributeValue("type");
            List<Element> whenNodes = xpath.compile("when", Filters.element()).evaluate(parameterNode);
            HashMap<String, String> when = new HashMap<>();
            for (Element whenNode : whenNodes) {
                when.put(whenNode.getAttributeValue("value"), whenNode.getValue());
            }
            Element otherwise = xpath.compile("otherwise", Filters.element()).evaluateFirst(parameterNode);
            if (otherwise != null) {
                when.put(null, otherwise.getValue());
            }
            String value = "";
            if (parameterInput.containsKey(ID)) {
                value = parameterInput.get(ID).value;
            } else {
                Element defaultNode = xpath.compile("default", Filters.element()).evaluateFirst(parameterNode);
                if (defaultNode != null) {
                    value = defaultNode.getValue();
                }
            }
            Parameter<?> parameter = ParameterFactory.getParameter(ID, name, type, value);
            parameter.setWhen(when);
            parameters.put(name, parameter);
        }
        List<Element> sqlNodes = xpath.compile("SQL", Filters.element()).evaluate(queryNodes);
        LinkedList<QueryList.SQL> sqlList = new LinkedList<>();
        for (Element sql : sqlNodes) {
            sqlList.add(new QueryList.SQL(sql.getValue(), sql.getAttributeValue("name", "rows")));
        }
        return new QueryList(sqlList, parameters, queryNodes.getAttributeValue("name"));
    }

    static private JsonNode executeQuery(ArrayList<QueryList> queryList, ConnectionsManager connectionsManager) throws SQLException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode mainNode = mapper.createObjectNode();
        if (queryList.size() == 1 && queryList.get(0).name == null) {
            return executeSQL(queryList.get(0).queryList, queryList.get(0).parameters, connectionsManager);
        }

        for (QueryList query : queryList) {
            if (query.name == null) {
                mainNode.set("rows", executeSQL(query.queryList, query.parameters, connectionsManager));
            } else {
                mainNode.set(query.name, executeSQL(query.queryList, query.parameters, connectionsManager));
            }
        }
        return mainNode;
    }

    static private JsonNode executeSQL(List<QueryList.SQL> sqlLists, HashMap<String, Parameter<?>> parameters, ConnectionsManager connectionsManager) throws SQLException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode mainNode = mapper.createObjectNode();
        Connection connection = connectionsManager.getConnection();
        for (QueryList.SQL sql : sqlLists) {
            QueryController queryController = new QueryController(new StringBuilder(sql.query), parameters, connection, true);
            mainNode.set(sql.name, queryController.getResult());
        }
        connection.close();
        return mainNode;
    }


    /**
     * Функция проверки правильности пути к директории хранения запросов
     * в формате XMLQuery, проверяет существования каталога, файла CONNECTIONS.xml
     * и прочих условий.
     *
     * @param directory проверяемая директория.
     * @return возвращает ошибку {@link ParserXMLErrors} если что-то не так или null если все хорошо.
     */
    private ParserXMLErrors checkDirectory(String directory) {
        //Проверка того что директория существует
        if (Files.notExists(Paths.get(directory)) || !Files.isDirectory(Paths.get(directory))) {
            return new ExceptionNoDirectory(directory);
        }
        //Проверка наличия файла CONNECTIONS.xml
        if (Files.notExists(Paths.get(directory + "/CONNECTIONS.xml"))) {
            return new ExceptionNoConnectionConfig(directory);
        }
        //Проверка количества файлов
        try (Stream<Path> files = Files.list(Paths.get(directory))) {
            long count = files.count();
            if (count < 1) {
                return new ExceptionNoFilesInDirectory(directory);
            }
        } catch (IOException ioException) {
            return new ExceptionNoFilesInDirectory(directory, ioException);
        }
        return null;
    }

    /**
     * Функция поиска фала хранящего запрос
     * в случае наличия запроса в двух файлах вызовет ошибку {@link ParserXMLErrors}
     *
     * @param directory директория XMLQuery.
     * @return возмущает ошибку {@link ParserXMLErrors} если что-то не так или null если все хорошо.
     */
    private String findFile(String directory, String queryName) throws ParserXMLErrors {
        //Создание стрима для получения имён фалов
        try (Stream<Path> filePathStream = Files.walk(Paths.get(directory))) {
            String fileName = null;
            //Поиск файла с запросом
            for (Path path : filePathStream.collect(Collectors.toList())) {
                if (Files.isDirectory(path)) {
                    continue;
                }
                String currentFile = path.toString();
                Document document = builder.build(currentFile);
                List<Element> querySetNodes = xpath.compile("/Definitions/QuerySet[@ID='" + queryName + "']", Filters.element()).evaluate(document);
                if (querySetNodes.size() > 0) {
                    //Если есть дублирование, то выкидывается ошибка
                    if (fileName != null) {
                        throw new ExceptionQueryHasDuplicate(directory, fileName, currentFile);
                    }
                    fileName = currentFile;
                }
            }
            return fileName;
        } catch (IOException | JDOMException e) {
            throw new ExceptionDirectoryInWork(directory, e);
        }
    }

    public static void main(String[] args) throws URISyntaxException, SQLException, ParserXMLErrors {
        ArrayList<ParameterInput> parameters = new ArrayList<>();
        parameters.add(new ParameterInput("INTNUM", "NULL"));
        XmlQueryParser xmlQueryParser = new XmlQueryParser();
        System.out.println(Objects.requireNonNull(Paths.get(Objects.requireNonNull(XmlQueryParser.class.getClassLoader().getResource("")).toURI())).toString() + "\\query\\Query");
        try {
            xmlQueryParser.getQuery(Objects.requireNonNull(Paths.get(Objects.requireNonNull(XmlQueryParser.class.getClassLoader().getResource("")).toURI())).toString() + "\\query\\Query", "TEST4", parameters);
        } catch (ParserXMLErrors E) {
            System.out.println(E.getError());
        }
        //JsonNode objectNode = xmlQueryParser.getQuery(QueryTest.class.getClassLoader().getResource().getResourceAsStream("query/Query/QuerySet.xml"), "TEST", parameters, connectionsManager);

    }
}
