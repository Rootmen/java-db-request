package com.rootmen.Database.DatabaseQuery;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rootmen.BaseTest;
import com.rootmen.Database.DatabaseQuery.Parameter.Parameter;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterFactory;
import com.rootmen.Database.DatabaseQuery.Query.Query;
import com.rootmen.Database.DatabaseQuery.Query.QueryController;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Это класс тестирования компонента для запросов
 *
 * @author Shinderov Roman
 * @version 1.0
 * @since 2021-12-16
 **/

public class QueryTest2 extends BaseTest {

    String url = "jdbc:postgresql://176.99.11.235:5432/main";
    String user = "authorization_app";
    String pass = "ga4kHTswrjcqwWDi51QA";

    @Test
    public void testGenerateOneQuery() throws Exception {
        ConnectionsManager connectionsManager = new ConnectionsManager(url, user, pass, true);
        printText("Базовый запрос на возращение одного ResultSet", "blue");
        Parameter<?> INT_VALUE = ParameterFactory.getParameter("INT_VALUE", "INT_VALUE", "Int", "1");
        Parameter<?> STRING_VALUE = ParameterFactory.getParameter("STRING_VALUE1", "STRING_VALUE1", "String", "s1");
        HashMap<String, Parameter<?>> parameters = new HashMap<>();
        parameters.put("$INT_VALUE$", INT_VALUE);
        parameters.put("$STRING_VALUE$", STRING_VALUE);
        Query query = new Query(new StringBuilder("SELECT $INT_VALUE$ as INT_VALUE, $STRING_VALUE$ as STRING_VALUE"), parameters, connectionsManager);
        printText("Проверка генерации текста запроса", "cyan");
        printText("(" + query.getQuery() + ") = (" + "SELECT ? as INT_VALUE, ? as STRING_VALUE)", "cyan");
        Assert.assertEquals(query.getQuery().toString(), "SELECT ? as INT_VALUE, ? as STRING_VALUE");
        printText("Успех! Создание текста запроса прошло успешно!", "green");
        printText("Проверка результата запроса", "cyan");
        String result = query.runQuery().toString();
        printText("(" + result + ") = (" + "[{\"int_value\":1,\"string_value\":\"s1\"}])", "cyan");
        Assert.assertEquals(result, "[{\"int_value\":1,\"string_value\":\"s1\"}]");
        printText("Успех! Выполнение запроса прошло успешно!", "green");
        printText("Проверка базового запроса на возращение одного ResultSet прошла успешно!", "green");
        printText("Проверка одного пустого запроса в под запросах", "cyan");
        query = new Query(new StringBuilder("CREATE TEMPORARY TABLE IF NOT EXISTS TEST AS (SELECT 1); SELECT $INT_VALUE$ as INT_VALUE, $STRING_VALUE$ as STRING_VALUE"), parameters, connectionsManager);
        printText("(" + query.getQuery() + ") = (" + "CREATE TEMPORARY TABLE IF NOT EXISTS TEST AS (SELECT 1); SELECT ? as INT_VALUE, ? as STRING_VALUE)", "cyan");
        Assert.assertEquals(query.getQuery().toString(), "CREATE TEMPORARY TABLE IF NOT EXISTS TEST AS (SELECT 1); SELECT ? as INT_VALUE, ? as STRING_VALUE");
        result = query.runQuery().toString();
        printText("(" + result + ") = (" + "[{\"int_value\":1,\"string_value\":\"s1\"}])", "cyan");
        Assert.assertEquals(result, "[{\"int_value\":1,\"string_value\":\"s1\"}]");
        printText("Успех! Выполнение запроса прошло успешно!", "green");
    }


    @Test
    public void testGenerateQuery2() throws Exception {
        ConnectionsManager connectionsManager = new ConnectionsManager(url, user, pass, true);
        Parameter<?> INT_VALUE = ParameterFactory.getParameter("INT_VALUE", "INT_VALUE", "Int", "1");
        Parameter<?> INT_VALUE2 = ParameterFactory.getParameter("INT_VALUE2", "INT_VALUE2", "Int", "2");
        Parameter<?> STRING_VALUE1 = ParameterFactory.getParameter("STRING_VALUE1", "STRING_VALUE1", "String", "s1");
        Parameter<?> STRING_VALUE2 = ParameterFactory.getParameter("STRING_VALUE2", "STRING_VALUE2", "String", "s2");
        HashMap<String, Parameter<?>> parameters = new HashMap<>();
        Assert.assertEquals(INT_VALUE.getValue(), 1);
        Assert.assertEquals(INT_VALUE2.getValue(), 2);
        Assert.assertEquals(STRING_VALUE1.getValue(), "s1");
        Assert.assertEquals(STRING_VALUE2.getValue(), "s2");
        parameters.put("$INT_VALUE$", INT_VALUE);
        parameters.put("$INT_VALUE2$", INT_VALUE2);
        parameters.put("$STRING_VALUE1$", STRING_VALUE1);
        parameters.put("$STRING_VALUE2$", STRING_VALUE2);
        QueryController query = new QueryController(new StringBuilder("(SELECT $INT_VALUE$ as INT_VALUE, $INT_VALUE2$ as INT_VALUE2, $INT_VALUE$ as INT_VALUE3, $INT_VALUE2$ as INT_VALUE4, $STRING_VALUE1$ as STRING_VALUE1, $STRING_VALUE2$  as STRING_VALUE1 union all SELECT $INT_VALUE$ as INT_VALUE, $INT_VALUE2$ as INT_VALUE2, $INT_VALUE$ as INT_VALUE3, $INT_VALUE2$ as INT_VALUE4, $STRING_VALUE1$ as STRING_VALUE1, $STRING_VALUE2$  as STRING_VALUE1); SELECT $INT_VALUE$ as INT_VALUE, $INT_VALUE2$ as INT_VALUE2, $INT_VALUE$ as INT_VALUE3, $INT_VALUE2$ as INT_VALUE4, $STRING_VALUE1$ as STRING_VALUE1, $STRING_VALUE2$  as STRING_VALUE1;"), parameters, connectionsManager);
        Assert.assertEquals(query.getResult().toString(), "{\"0\":[{\"int_value\":1,\"int_value2\":2,\"int_value3\":1,\"int_value4\":2,\"string_value1\":\"s1\"},{\"int_value\":1,\"int_value2\":2,\"int_value3\":1,\"int_value4\":2,\"string_value1\":\"s1\"}],\"1\":[{\"int_value\":1,\"int_value2\":2,\"int_value3\":1,\"int_value4\":2,\"string_value1\":\"s1\"}]}");
        query = new QueryController(new StringBuilder("SELECT * FROM generate_series(2,100);;"), parameters, connectionsManager);
        while (true) {
            ObjectNode node = query.getNextLine();
            if (node == null) {
                break;
            }
            System.out.println(node.toString());
        }
    }

    @Test
    public void testArray() throws Exception {
        ConnectionsManager connectionsManager = new ConnectionsManager(url, user, pass, true);
        ArrayList<Integer> array = new ArrayList<>();
        array.add(1);
        array.add(2);
        array.add(3);
        Parameter<?> STRING_VALUE = ParameterFactory.getArrayParameter("STRING_VALUE", "STRING_VALUE", "int", array);
        HashMap<String, Parameter<?>> parameters = new HashMap<>();
        parameters.put("$STRING_VALUE$", STRING_VALUE);
        Query query = new Query(new StringBuilder("SELECT $STRING_VALUE$ as STRING_VALUE;"), parameters, connectionsManager);
        Assert.assertEquals(query.runQuery().toString(), "[{\"string_value\":[1,2,3]}]");
    }


}
