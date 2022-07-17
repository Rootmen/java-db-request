package com.rootmen.Database.DatabaseQuery.XmlParser.Errors.Types;

import com.rootmen.Database.DatabaseQuery.XmlParser.Errors.ParserXMLErrors;

import java.io.IOException;

public class ExceptionQueryHasDuplicate extends ParserXMLErrors {
    String directory;
    String file1;
    String file2;

    public ExceptionQueryHasDuplicate(String directory, String file1, String file2) {
        this.directory = directory;
        this.file1 = file1;
        this.file2 = file2;
    }


    @Override
    public String getMessage() {
        return "При поиске запроса в директория " + directory + " файлы " + file1 + " и " + file2 + " содержат одинаковые названия запросов";
    }
}
