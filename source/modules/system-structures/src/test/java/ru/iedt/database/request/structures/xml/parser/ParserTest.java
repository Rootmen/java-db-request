package ru.iedt.database.request.structures.xml.parser;

import org.junit.jupiter.api.*;

import java.io.FileInputStream;

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
        (new StaxXmlParser()).parseDefinitions(this.getClass().getResourceAsStream("/test.xml"));
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
