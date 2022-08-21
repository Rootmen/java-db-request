package com.rootmen.Database.DatabaseQuery;

import com.fasterxml.jackson.databind.JsonNode;
import com.rootmen.BaseTest;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterInput;
import com.rootmen.Database.DatabaseQuery.Query.ConnectionsManager;
import com.rootmen.Database.DatabaseQuery.XmlParser.XmlQueryParser;
import org.junit.Test;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;


/**
 * Это класс тестирования компонента для запросов
 *
 * @author Shinderov Roman
 * @version 1.0
 * @since 2021-12-16
 **/

public class QueryTest extends BaseTest {

    String url = "jdbc:postgresql://176.99.11.235:5432/postgres";
    String user = "authorization_app";
    String pass = "ga4kHTswrjcqwWDi51QA";

    @Test
    public void testXmlQuery2() throws Exception {
       /* ArrayList<ParameterInput> parameters = new ArrayList<>();
        parameters.add(new ParameterInput("INTNUM", "1"));
        ConnectionsManager connectionsManager = new ConnectionsManager(url, user, pass, "org.postgresql.Driver");
        XmlQueryParser xmlQueryParser = new XmlQueryParser();
        URL resource = QueryTest.class.getResource("/query/Query");
        String directory = Paths.get(Objects.requireNonNull(resource).toURI()).toString();
        JsonNode jsonNode = xmlQueryParser.getQuery(directory, "TEST", parameters);*/
        //JsonNode objectNode = xmlQueryParser.getQuery(QueryTest.class.getClassLoader().getResource().getResourceAsStream("query/Query/QuerySet.xml"), "TEST", parameters, connectionsManager);
        //System.out.println(objectNode);
    }
}
