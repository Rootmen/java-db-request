package com.rootmen.DatabaseController.Parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class DatabaseFileExecute {
    SAXParser parser;
    XMLHandler handler;

    DatabaseFileExecute(File file) throws ParserConfigurationException, SAXException, IOException {
        initialization(file.getAbsolutePath());
    }

    DatabaseFileExecute(String filename) throws ParserConfigurationException, SAXException, IOException {
        initialization(filename);
    }

    private void initialization(String filename) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        this.parser = factory.newSAXParser();
        if (handler == null) {
            this.handler = new XMLHandler();
        }
        parser.parse(new File(filename), this.handler);
    }

    private static class XMLHandler extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            System.out.println(qName);
            if (qName.equalsIgnoreCase("QuerySet")) {

            }
        }
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        new DatabaseFileExecute("C:\\Users\\Shinderov\\Documents\\GitHub\\JSON-db-request\\src\\main\\resources\\query\\query.xml");
    }
}
