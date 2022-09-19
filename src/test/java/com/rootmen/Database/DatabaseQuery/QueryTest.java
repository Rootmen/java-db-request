package com.rootmen.Database.DatabaseQuery;

import com.fasterxml.jackson.databind.JsonNode;
import com.rootmen.BaseTest;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterInput;
import com.rootmen.Database.DatabaseQuery.Query.ConnectionsManager;
import com.rootmen.Database.DatabaseQuery.XmlParser.XmlQueryParser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
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
        /*try (PrintWriter output = new PrintWriter(System.out)) {
            InputStream xmlQuery = new ByteArrayInputStream("<QuerySet refid=\"GET_TECH_FORM\" directory=\"EIM\"><TextParam ID=\"VAR_ID\">1</TextParam><TextParam ID=\"YEARS\">2021</TextParam></QuerySet>".getBytes());
            XmlQueryParser.getInstance().getQuery(xmlQuery, output);
        }*/
    }
}
