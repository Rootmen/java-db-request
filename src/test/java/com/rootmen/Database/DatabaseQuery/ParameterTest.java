package com.rootmen.Database.DatabaseQuery;

import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterExceptionErrorType;
import com.rootmen.Database.DatabaseQuery.Parameter.Parameter;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterFactory;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ObjectsElements.Type.ParameterInteger;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ObjectsElements.Type.ParameterString;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class ParameterTest {
    @Test
    public void testParameterFactoryInt() throws Exception {
        System.out.println("Testing FactoryInt");
        Parameter<?> parameter = ParameterFactory.getParameter("test", "test", "Int", "1");
        Assert.assertSame(parameter.getClass(), ParameterInteger.class);
        Assert.assertEquals(parameter.getValue(), 1);

        parameter = ParameterFactory.getParameter("test", "test", "Int", "2");
        Assert.assertSame(parameter.getClass(), ParameterInteger.class);
        Assert.assertEquals(parameter.getValue(),2);

        parameter = ParameterFactory.getParameter("test", "test", "Int", "3");
        Assert.assertSame(parameter.getClass(), ParameterInteger.class);
        Assert.assertEquals(parameter.getValue(), 3);
    }

    @Test
    public void testParameterFactoryString() throws Exception {
        System.out.println("Testing FactoryString");
        Parameter<?> parameter = ParameterFactory.getParameter("test", "test", "String", "1");
        Assert.assertSame(parameter.getClass(), ParameterString.class);
        Assert.assertEquals(parameter.getValue(), "1");

        parameter = ParameterFactory.getParameter("test", "test", "String", "2");
        Assert.assertSame(parameter.getClass(), ParameterString.class);
        Assert.assertEquals(parameter.getValue(), "2");

        parameter = ParameterFactory.getParameter("test", "test", "String", "3");
        Assert.assertSame(parameter.getClass(), ParameterString.class);
        Assert.assertEquals(parameter.getValue(), "3");
    }

    @Test(expected = ParameterExceptionErrorType.class)
    public void whenExceptionThrown_thenExpectationSatisfied() throws ParameterExceptionErrorType {
        Parameter<?> parameter = null;
        try {
            parameter = ParameterFactory.getParameter("test", "test", "Int", "фии");
            Assert.assertSame(parameter.getClass(), ParameterString.class);
            Assert.assertEquals(parameter.getValue(), "3");
        } catch (ParameterException e) {
            System.out.println(e.getClass());
            throw (ParameterExceptionErrorType) e;
        }
    }

}
