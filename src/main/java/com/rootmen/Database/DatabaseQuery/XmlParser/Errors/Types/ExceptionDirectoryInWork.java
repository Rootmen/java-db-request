package com.rootmen.Database.DatabaseQuery.XmlParser.Errors.Types;

import com.rootmen.Database.DatabaseQuery.XmlParser.Errors.ParserXMLErrors;

import java.io.IOException;

public class ExceptionDirectoryInWork extends ParserXMLErrors {
    String directory;
    Exception ioException;

    public ExceptionDirectoryInWork(String directory, Exception ioException) {
        this.directory = directory;
        this.ioException = ioException;
    }

    @Override
    public String getError() {
        return "Во время работы с директорией " + directory + " произошла ошибка \n" + ioException.getMessage();

    }
}
