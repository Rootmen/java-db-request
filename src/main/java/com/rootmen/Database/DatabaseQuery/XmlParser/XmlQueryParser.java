package com.rootmen.Database.DatabaseQuery.XmlParser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rootmen.Database.DatabaseQuery.JsonParser.MapperConfig;
import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.Parameter;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterInput;
import com.rootmen.Database.DatabaseQuery.Query.Binder.QueryWrapperClass;
import com.rootmen.Database.DatabaseQuery.Query.Binder.ResultSetWrapper;
import com.rootmen.Database.DatabaseQuery.Query.ConnectionsManager;
import com.rootmen.Database.DatabaseQuery.Query.Controller.QueryController;
import com.rootmen.Database.DatabaseQuery.XmlParser.Caching.Entity.QueryList;
import com.rootmen.Database.DatabaseQuery.XmlParser.Errors.ParserXMLErrors;
import com.rootmen.Database.DatabaseQuery.XmlParser.Errors.Types.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathFactory;

import javax.naming.NamingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.rootmen.Database.DatabaseQuery.XmlParser.Caching.Entity.QueryList.generateParameters;

public class XmlQueryParser {
    SAXBuilder builder = new SAXBuilder();
    XPathFactory xpath = XPathFactory.instance();

    public static XmlQueryParser getInstance() {
        return new XmlQueryParser();
    }

    public void getQuery(InputStream xmlQuery, PrintWriter output) throws SQLException, ParserXMLErrors, ClassNotFoundException, IOException, JDOMException, URISyntaxException {
        SAXBuilder builder = new SAXBuilder();
        XPathFactory xpath = XPathFactory.instance();
        Document xmlQueryDocument = builder.build(xmlQuery);
        List<Element> querySetNodes = xpath.compile("/QuerySet", Filters.element()).evaluate(xmlQueryDocument);
        if (querySetNodes.size() != 1) {
            throw new ExceptionXmlQueryInputParseError(xmlQuery.toString());
        }
        Element querySetNode = querySetNodes.get(0);
        String querySetName = querySetNode.getAttributeValue("refid");
        URL resource = XmlQueryParser.class.getResource("/" + querySetNode.getAttributeValue("directory"));
        String directory = Paths.get(Objects.requireNonNull(resource).toURI()).toString();
        if (querySetName == null) {
            throw new ExceptionXmlQueryInputParseError(xmlQuery.toString());
        }
        LinkedList<ParameterInput> parameterInput = new LinkedList<>();
        for (Element parameterInputElement : querySetNode.getChildren()) {
            if (Objects.equals(parameterInputElement.getName(), "TextParam")) {
                String id = parameterInputElement.getAttributeValue("ID"),
                        value = parameterInputElement.getValue();
                parameterInput.add(new ParameterInput(id, value));
            }
        }
        LinkedList<QueryList> queryList = this.getQueryList(querySetName, directory);
        for (QueryList query : queryList) {
            for (QueryList.ParameterCaching parameter : query.parameters) {
                if (parameter.isRequired()) {
                    Optional<ParameterInput> parameterInputOptional
                            = parameterInput.stream()
                            .filter(input -> input.name
                                    .equals(parameter.getID())).findFirst();
                    if (!parameterInputOptional.isPresent()) {
                        throw new ExceptionConfigNoRequired(querySetName, parameter.getID());
                    }
                }
            }
        }
        HashMap<String, ConnectionsManager> connectionsManagerHashMap = this.getConnections(directory);
        executeQuery(queryList, connectionsManagerHashMap, new ArrayList<>(parameterInput), output);
    }

    public JsonNode getQuery(String directory, String querySetName, ArrayList<ParameterInput> parameterInput) throws SQLException, ParserXMLErrors, ClassNotFoundException, IOException, JDOMException {
        //Проверка директории на правильность
        LinkedList<QueryList> queryList = this.getQueryList(querySetName, directory);
        HashMap<String, ConnectionsManager> connectionsManagerHashMap = this.getConnections(directory);
        return executeQuery(queryList, connectionsManagerHashMap, parameterInput);
    }

    public <T> ArrayList<T> getQuery(String directory, String querySetName, ArrayList<ParameterInput> parameterInput, Class<? extends ResultSetWrapper<T>> resultSetWrapper) throws SQLException, ParserXMLErrors, ClassNotFoundException, IOException, JDOMException {
        //Проверка директории на правильность
        LinkedList<QueryList> queryList = this.getQueryList(querySetName, directory);
        HashMap<String, ConnectionsManager> connectionsManagerHashMap = this.getConnections(directory);
        return executeQuery(queryList, connectionsManagerHashMap, parameterInput, resultSetWrapper);
    }


    private LinkedList<QueryList> getQuerySet(String querySetName, Document document) {
        try {
            List<Element> querySetNodes = xpath.compile("/Definitions/QuerySet[@ID='" + querySetName + "']", Filters.element()).evaluate(document);
            if (querySetNodes.size() != 1) {
                throw new Exception("Ошибка документа");
            }
            LinkedList<QueryList> queryList = new LinkedList<>();
            List<Element> queryNodes = querySetNodes.get(0).getChildren();
            String connection = null;
            for (Element queryNode : queryNodes) {
                if (Objects.equals(queryNode.getName(), "CONNECTION")) {
                    connection = queryNode.getAttributeValue("REFID");
                } else if (Objects.equals(queryNode.getName(), "QUERY")) {
                    queryList.add(parseQuery(queryNode, connection));
                }
            }
            return queryList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private QueryList parseQuery(Element queryNodes, String connection) throws ParameterException {
        List<Element> elementList = queryNodes.getChildren();
        LinkedList<QueryList.ParameterCaching> parameters = new LinkedList<>();
        LinkedList<QueryList.SQL> sqlList = new LinkedList<>();
        for (Element element : elementList) {
            if (Objects.equals(element.getName(), "TextParam")) {
                String name = "$" + element.getAttributeValue("name") + "$";
                String ID = element.getAttributeValue("ID");
                String type = element.getAttributeValue("type");
                String required = element.getAttributeValue("required");
                List<Element> whenNodes = xpath.compile("when", Filters.element()).evaluate(element);
                HashMap<String, String> when = new HashMap<>();
                for (Element whenNode : whenNodes) {
                    when.put(whenNode.getAttributeValue("value"), whenNode.getValue());
                }
                Element otherwise = xpath.compile("otherwise", Filters.element()).evaluateFirst(element);
                if (otherwise != null) {
                    when.put(null, otherwise.getValue());
                }
                String value = "";
                Element defaultNode = xpath.compile("default", Filters.element()).evaluateFirst(element);
                if (defaultNode != null) {
                    value = defaultNode.getValue();
                }
                parameters.add(new QueryList.ParameterCaching(ID, name, type, when, value, Objects.equals(required, "true")));
            } else if (Objects.equals(element.getName(), "SQL")) {
                sqlList.add(new QueryList.SQL(element.getValue(), element.getAttributeValue("name", "rows"), connection, element.getAttributeValue("wrapperClass", "")));
            } else if (Objects.equals(element.getName(), "CONNECTION")) {
                connection = element.getAttributeValue("REFID");
            }
        }
        return new QueryList(sqlList, parameters, queryNodes.getAttributeValue("name"));
    }


    static private JsonNode executeQuery(LinkedList<QueryList> queryList, HashMap<String, ConnectionsManager> connectionsManager, ArrayList<ParameterInput> parameters) throws SQLException, ExceptionNoConnectionID, ClassNotFoundException {
        ObjectMapper mapper = MapperConfig.getMapper();
        ObjectNode mainNode = mapper.createObjectNode();
        if (queryList.size() == 1 && queryList.get(0).name == null) {
            HashMap<String, Parameter<?>> parametersHasMap = generateParameters(parameters, queryList.get(0).parameters);
            return executeSQL(queryList.get(0).queryList, parametersHasMap, connectionsManager);
        }

        for (QueryList query : queryList) {
            HashMap<String, Parameter<?>> parametersHasMap = generateParameters(parameters, query.parameters);
            if (query.name == null) {
                mainNode.set("rows", executeSQL(query.queryList, parametersHasMap, connectionsManager));
            } else {
                mainNode.set(query.name, executeSQL(query.queryList, parametersHasMap, connectionsManager));
            }
        }
        return mainNode;
    }

    static private JsonNode executeSQL(List<QueryList.SQL> sqlLists, HashMap<String, Parameter<?>> parameters, HashMap<String, ConnectionsManager> connectionsManager) throws SQLException, ExceptionNoConnectionID, ClassNotFoundException {
        HashMap<String, Connection> connectionHashMap = new HashMap<>();
        try {
            ObjectMapper mapper = MapperConfig.getMapper();
            ObjectNode mainNode = mapper.createObjectNode();
            for (QueryList.SQL sql : sqlLists) {
                QueryController queryController = getQueryController(parameters, connectionsManager, connectionHashMap, sql);
                mainNode.set(sql.name, queryController.getResult());
            }
            return mainNode;
        } finally {
            for (Connection connection : connectionHashMap.values()) {
                connection.close();
            }
        }
    }


    static private <T> ArrayList<T> executeQuery(LinkedList<QueryList> queryList, HashMap<String, ConnectionsManager> connectionsManager, ArrayList<ParameterInput> parameters, Class<? extends ResultSetWrapper<T>> resultSetWrapper) throws SQLException, ExceptionNoConnectionID, ClassNotFoundException {
        if (queryList.size() == 1 && queryList.get(0).name == null) {
            HashMap<String, Parameter<?>> parametersHasMap = generateParameters(parameters, queryList.get(0).parameters);
            return executeSQL(queryList.get(0).queryList, parametersHasMap, connectionsManager, resultSetWrapper);
        }

        ArrayList<T> resultSetWrapperArrayList = new ArrayList<>();
        for (QueryList query : queryList) {
            HashMap<String, Parameter<?>> parametersHasMap = generateParameters(parameters, queryList.get(0).parameters);
            resultSetWrapperArrayList.addAll(executeSQL(query.queryList, parametersHasMap, connectionsManager, resultSetWrapper));
        }
        return resultSetWrapperArrayList;
    }

    static private <T> ArrayList<T> executeSQL(List<QueryList.SQL> sqlLists, HashMap<String, Parameter<?>> parameters, HashMap<String, ConnectionsManager> connectionsManager, Class<? extends ResultSetWrapper<T>> resultSetWrapper) throws SQLException, ExceptionNoConnectionID, ClassNotFoundException {
        HashMap<String, Connection> connectionHashMap = new HashMap<>();
        try {
            ArrayList<T> resultSetWrapperArrayList = new ArrayList<>();
            for (QueryList.SQL sql : sqlLists) {
                QueryController queryController = getQueryController(parameters, connectionsManager, connectionHashMap, sql);
                resultSetWrapperArrayList = queryController.getResult(resultSetWrapper);
            }
            return resultSetWrapperArrayList;
        } finally {
            for (Connection connection : connectionHashMap.values()) {
                connection.close();
            }
        }
    }


    static private void executeQuery(LinkedList<QueryList> queryList, HashMap<String, ConnectionsManager> connectionsManager, ArrayList<ParameterInput> parameters, PrintWriter output) throws SQLException, ExceptionNoConnectionID, ClassNotFoundException, JsonProcessingException {
        if (queryList.size() == 1 && queryList.get(0).name == null) {
            output.write("{ \"rows\": [");
            HashMap<String, Parameter<?>> parametersHasMap = generateParameters(parameters, queryList.get(0).parameters);
            executeSQL(queryList.get(0).queryList, parametersHasMap, connectionsManager, output);
            output.write("]}");
            return;
        }

        for (QueryList query : queryList) {
            HashMap<String, Parameter<?>> parametersHasMap = generateParameters(parameters, query.parameters);
            if (query.name == null) {
                output.write("{ \"rows\": [");
            } else {
                output.write("{ \"" + query.name + "\": [");
            }
            executeSQL(query.queryList, parametersHasMap, connectionsManager, output);
            output.write("]}");
        }
    }

    static private void executeSQL(List<QueryList.SQL> sqlLists, HashMap<String, Parameter<?>> parameters, HashMap<String, ConnectionsManager> connectionsManager, PrintWriter output) throws SQLException, ExceptionNoConnectionID, ClassNotFoundException, JsonProcessingException {
        HashMap<String, Connection> connectionHashMap = new HashMap<>();
        try {
            boolean first = true;
            for (QueryList.SQL sql : sqlLists) {
                QueryController queryController = getQueryController(parameters, connectionsManager, connectionHashMap, sql);
                ObjectNode jsonNode = queryController.getNextLine();
                ObjectMapper objectMapper = MapperConfig.getMapper();
                QueryWrapperClass instance = null;
                if (!Objects.equals(sql.wrapperClass, "")) {
                    try {
                        Class<?> wrapperClass = Class.forName(sql.wrapperClass);
                        instance = (QueryWrapperClass) wrapperClass.getDeclaredConstructor().newInstance();
                        instance.initialize(parameters);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
                if (jsonNode == null) {
                    continue;
                }
                if (!first) {
                    output.write(",");
                }
                first = false;
                while (true) {
                    if (instance != null) {
                        jsonNode = instance.rowsLine(jsonNode);
                    }
                    output.write(objectMapper.writeValueAsString(jsonNode));
                    jsonNode = queryController.getNextLine();
                    if (jsonNode == null) {
                        break;
                    }
                    output.write(",");
                }
            }
        } finally {
            for (Connection connection : connectionHashMap.values()) {
                connection.close();
            }
        }
    }

    private static QueryController getQueryController(HashMap<String, Parameter<?>> parameters, HashMap<String, ConnectionsManager> connectionsManager, HashMap<String, Connection> connectionHashMap, QueryList.SQL sql) throws ExceptionNoConnectionID, SQLException, ClassNotFoundException {
        String connectionId = sql.connection;
        if (!connectionHashMap.containsKey(connectionId)) {
            ConnectionsManager connectionManager = connectionsManager.get(sql.connection);
            if (connectionManager == null) {
                throw new ExceptionNoConnectionID(sql.connection);
            }
            Connection connection = connectionManager.getConnection();
            connectionHashMap.put(connectionId, connection);
        }
        return new QueryController(new StringBuilder(sql.query), parameters, connectionHashMap.get(connectionId), true);
    }

    private LinkedList<QueryList> getQueryList(String querySetName, String directory) throws ParserXMLErrors, IOException, JDOMException {
        Document document = this.getFileDocument(directory, querySetName);
        return getQuerySet(querySetName, document);
       /* if (QuerySet.cachedQuery.containsKey(querySetName)) {
            return QuerySet.cachedQuery.get(querySetName);
        } else {
            Document document = this.getFileDocument(directory, querySetName);
            LinkedList<QueryList> queryList = getQuerySet(querySetName, document);
            QuerySet.cachedQuery.put(querySetName, queryList);
            return queryList;
        }*/
    }

    /**
     * Функция получения {@link HashMap} со списком подключений к БД
     * из файла CONNECTIONS.xml
     *
     * @param directory проверяемая директория расположения запроса и фала CONNECTIONS.xml
     * @return возвращает {@link HashMap<String, ConnectionsManager>} со списком подключений.
     */
    private HashMap<String, ConnectionsManager> getConnections(String directory) throws IOException, JDOMException, ParserXMLErrors {
        Document connectionsFile = builder.build(new FileInputStream(directory + "/CONNECTIONS.xml"));
        List<Element> driverList = xpath.compile("/Definitions/DRIVER", Filters.element()).evaluate(connectionsFile);
        HashMap<String, String> driverHashMap = new HashMap<>();
        for (Element drive : driverList) {
            String driverId = drive.getAttributeValue("ID");
            List<Element> driverClassName = xpath.compile("ClassName", Filters.element()).evaluate(drive);
            if (driverClassName.size() > 0) {
                driverHashMap.put(driverId, driverClassName.get(0).getValue());
            }
        }
        List<Element> connectionList = xpath.compile("/Definitions/CONNECTION", Filters.element()).evaluate(connectionsFile);
        HashMap<String, ConnectionsManager> connectionsManagerHashMap = new HashMap<>();
        for (Element connection : connectionList) {
            String connectionId = connection.getAttributeValue("ID");
            List<Element> connectionParameter = connection.getChildren();
            String url = null, password = null, username = null, className = null, jndi = null;
            for (Element parameter : connectionParameter) {
                switch (parameter.getName().toLowerCase()) {
                    case "url":
                        url = parameter.getValue();
                        break;
                    case "user":
                        username = parameter.getValue();
                        break;
                    case "pwd":
                        password = parameter.getValue();
                        break;
                    case "driver":
                        className = parameter.getAttributeValue("REFID");
                        break;
                    case "jndi":
                        jndi = parameter.getValue();
                        break;
                }
            }
            className = driverHashMap.get(className);
            if (password != null && username != null && jndi != null) {
                try {
                    ConnectionsManager connectionsManager = new ConnectionsManager(jndi, username, password);
                    if (className != null) {
                        connectionsManager.setClassName(className);
                    }
                    connectionsManagerHashMap.put(connectionId, connectionsManager);
                } catch (NamingException e) {
                    throw new ExceptionConfigParseError(directory, connectionId + e.getMessage());
                }
            } else if (jndi != null) {
                try {
                    ConnectionsManager connectionsManager = new ConnectionsManager(jndi, username, password);
                    if (className != null) {
                        connectionsManager.setClassName(className);
                    }
                    connectionsManagerHashMap.put(connectionId, connectionsManager);
                } catch (NamingException e) {
                    throw new ExceptionConfigParseError(directory, connectionId + e.getMessage());
                }
            } else if (url != null && password != null && username != null && className != null) {
                connectionsManagerHashMap.put(connectionId, new ConnectionsManager(url, username, password, className));
            } else {
                throw new ExceptionConfigParseError(directory, connectionId);
            }
        }
        return connectionsManagerHashMap;
    }

    /**
     * Функция получения потока для фала хранящего запрос
     *
     * @param directory директория с файлами.
     * @return возвращает ошибку {@link ParserXMLErrors} если что-то не так или null если все хорошо.
     */
    private Document getFileDocument(String directory, String querySetName) throws ParserXMLErrors {
        //Проверка директории на правильность
        ParserXMLErrors errors = this.checkDirectory(directory);
        if (errors != null) {
            throw errors;
        }
        //Получение фала с XMLQuery запросом
        String filename = this.findFile(directory, querySetName);
        try (FileInputStream fileInputStream = new FileInputStream(filename)) {
            return builder.build(fileInputStream);
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }
        return null;
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

    public static void main(String[] args) throws URISyntaxException, SQLException, ParserXMLErrors, ClassNotFoundException {
        /*ArrayList<ParameterInput> parameters = new ArrayList<>();
        parameters.add(new ParameterInput("PASSPORT_ID", "720"));
        XmlQueryParser xmlQueryParser = new XmlQueryParser();
        try {
            String directory = Objects.requireNonNull(Paths.get(Objects.requireNonNull(XmlQueryParser.class.getClassLoader().getResource("")).toURI())) + "\\query\\PASSPORT";
            //ArrayList<Fragment> jsonNode = xmlQueryParser.getQuery(directory, "GET_PASSPORT_ID_NSI_FRAGMENTS", parameters, Fragment.class);
            //System.out.println(jsonNode);
        } catch (ParserXMLErrors| IOException | JDOMException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }*/
        //JsonNode objectNode = xmlQueryParser.getQuery(QueryTest.class.getClassLoader().getResource().getResourceAsStream("query/Query/QuerySet.xml"), "TEST", parameters, connectionsManager);

    }
}
