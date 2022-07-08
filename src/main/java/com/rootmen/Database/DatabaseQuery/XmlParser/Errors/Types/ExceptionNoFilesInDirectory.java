package com.rootmen.Database.DatabaseQuery.XmlParser.Errors.Types;

import com.rootmen.Database.DatabaseQuery.XmlParser.Errors.ParserXMLErrors;

import java.io.IOException;

public class ExceptionNoFilesInDirectory extends ParserXMLErrors {
    String directory;
    IOException ioException;

    public ExceptionNoFilesInDirectory(String directory) {
        this.directory = directory;
    }

    public ExceptionNoFilesInDirectory(String directory, IOException ioException) {
        this.directory = directory;
        this.ioException = ioException;
    }

    @Override
    public String getError() {
        if (this.ioException == null) {
            return "Директория " + directory + " не содержит файлов XMLQuery";
        } else {
            return "В директории " + directory + " не удалось проверить наличие файлов XMLQuery из-за \n" + ioException.getMessage();
        }
    }
}
