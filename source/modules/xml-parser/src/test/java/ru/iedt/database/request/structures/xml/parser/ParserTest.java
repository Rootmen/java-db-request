package ru.iedt.database.request.structures.xml.parser;

import org.junit.jupiter.api.*;
import ru.iedt.database.request.structures.nodes.database.Definition;
import ru.iedt.database.request.structures.nodes.database.QuerySet;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ParserTest {

    @BeforeAll
    static void setup() {
        System.out.println("@BeforeAll executed");
    }

    @BeforeEach
    void setupThis() {
        System.out.println("@BeforeEach executed");
    }

    @Test
    void testCalcOne() throws Exception {
        System.out.println("======TEST ONE EXECUTED=======");
        Date date = new Date();
        Definition result = (new StaxStreamParser()).parseDefinitions(this.getClass().getResource("/test.xml").toURI());
        System.out.println(TimeUnit.MILLISECONDS.convert(new Date().getTime() - date.getTime(), TimeUnit.MILLISECONDS) + " ms");
        System.out.println(result);
        //Assertions.assertEquals( 4 , Calculator.add(2, 2));
    }


    @AfterEach
    void tearThis() {
        System.out.println("@AfterEach executed");
    }

    @AfterAll
    static void tear() {
        System.out.println("@AfterAll executed");
    }
}
