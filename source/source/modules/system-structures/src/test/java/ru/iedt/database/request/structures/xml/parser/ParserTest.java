package ru.iedt.database.request.structures.xml.parser;

import org.junit.jupiter.api.*;
import ru.iedt.database.request.structures.nodes.database.QuerySet;

import java.io.FileInputStream;
import java.util.ArrayList;

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
    void testCalcOne() {
        System.out.println("======TEST ONE EXECUTED=======");
        ArrayList<QuerySet> result =  (new StaxXmlParser()).parseDefinitions(this.getClass().getResourceAsStream("/test.xml"));

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
