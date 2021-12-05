package com.rootmen.Database.DatabaseQuery;

import com.rootmen.Database.DatabaseQuery.Parameter.Parameter;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterFactory;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterTypes.ParameterInteger;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterTypes.ParameterString;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;

public class QueryTest {

    private Query usersService;

    @BeforeClass
    public static void globalSetUp() {
        System.out.println("Testing class Query");
    }

    @Test
    public void testParameterFactoryInt() throws Exception {
        System.out.println("Testing FactoryInt");
        Parameter parameter = ParameterFactory.getParameter("test", "test", "Int", "1");
        Assert.assertSame(parameter.getClass(), ParameterInteger.class);
        Assert.assertThat(parameter.getValue(), is("1"));

        parameter = ParameterFactory.getParameter("test", "test", "Int", "2");
        Assert.assertSame(parameter.getClass(), ParameterInteger.class);
        Assert.assertThat(parameter.getValue(), is("2"));

        parameter = ParameterFactory.getParameter("test", "test", "Int", "3");
        Assert.assertSame(parameter.getClass(), ParameterInteger.class);
        Assert.assertThat(parameter.getValue(), is("3"));
    }

    @Test
    public void testParameterFactoryString() throws Exception {
        System.out.println("Testing FactoryString");
        Parameter parameter = ParameterFactory.getParameter("test", "test", "String", "1");
        Assert.assertSame(parameter.getClass(), ParameterString.class);
        Assert.assertThat(parameter.getValue(), is("1"));

        parameter = ParameterFactory.getParameter("test", "test", "Int", "2");
        Assert.assertSame(parameter.getClass(), ParameterString.class);
        Assert.assertThat(parameter.getValue(), is("2"));

        parameter = ParameterFactory.getParameter("test", "test", "Int", "3");
        Assert.assertSame(parameter.getClass(), ParameterString.class);
        Assert.assertThat(parameter.getValue(), is("3"));
    }

    @Test
    public void testGenerateQuery() throws Exception {
        Parameter INT_VALUE = ParameterFactory.getParameter("INT_VALUE", "INT_VALUE", "Int", "1");
        Parameter INT_VALUE2 = ParameterFactory.getParameter("INT_VALUE2", "INT_VALUE2", "Int", "2");
        Parameter STRING_VALUE1 = ParameterFactory.getParameter("STRING_VALUE1", "STRING_VALUE1", "String", "s1");
        Parameter STRING_VALUE2 = ParameterFactory.getParameter("STRING_VALUE2", "STRING_VALUE2", "String", "s2");
        HashMap<String, Parameter> parameters = new HashMap<>();
        Assert.assertThat(INT_VALUE.getValue(), is("1"));
        Assert.assertThat(INT_VALUE2.getValue(), is("2"));
        Assert.assertThat(STRING_VALUE1.getValue(), is("s1"));
        Assert.assertThat(STRING_VALUE2.getValue(), is("s2"));
        parameters.put("INT_VALUE", INT_VALUE);
        parameters.put("INT_VALUE2", INT_VALUE2);
        parameters.put("STRING_VALUE1", STRING_VALUE1);
        parameters.put("STRING_VALUE2", STRING_VALUE2);
        //new Query("main", new StringBuilder("SELECT $INT_VALUE$ as aws, $STRING_VALUE1$ as aws2, $STRING_VALUE2$ as aws3, $STRING_VALUE1$ as aws4, $INT_VALUE$ as aws5,$INT_VALUE2$ as aws6;"), parameters);
    }

    @AfterClass
    public static void tearDown() {
        System.out.println("Tests finished");
    }

}
