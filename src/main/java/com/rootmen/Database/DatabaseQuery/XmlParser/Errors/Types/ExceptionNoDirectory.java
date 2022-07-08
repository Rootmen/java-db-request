package com.rootmen.Database.DatabaseQuery.XmlParser.Errors.Types;

import com.rootmen.Database.DatabaseQuery.XmlParser.Errors.ParserXMLErrors;

public class ExceptionNoDirectory extends ParserXMLErrors {
    String directory;

    public ExceptionNoDirectory(String directory) {
        this.directory = directory;
    }

    @Override
    public String getError() {
        return "Директория " + directory + " с запросами не найдена";
    }
}
