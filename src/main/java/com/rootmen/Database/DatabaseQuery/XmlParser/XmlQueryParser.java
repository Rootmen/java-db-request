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
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class XmlQueryParser {
    SAXBuilder builder = new SAXBuilder();
    XPathFactory xpath = XPathFactory.instance();

    public JsonNode getQuery(InputStream querySetFile, String querySetName, ArrayList<ParameterInput> parameterInput, ConnectionsManager connectionsManager) throws SQLException {
        ArrayList<QueryList> queryList = getQuerySet(querySetFile, querySetName, parameterInput);
        return executeQuery(queryList, connectionsManager);
    }

    private ArrayList<QueryList> getQuerySet(InputStream querySetFile, String querySetName, ArrayList<ParameterInput> parameterInput) {
        try {
            Document document = builder.build(querySetFile);
            List<Element> querySetNodes = xpath.compile("/Definitions/QuerySet[@ID='" + querySetName + "']", Filters.element()).evaluate(document);
            if (querySetNodes.size() != 1) {
                throw new Exception("Ошибка доумента");
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
}
