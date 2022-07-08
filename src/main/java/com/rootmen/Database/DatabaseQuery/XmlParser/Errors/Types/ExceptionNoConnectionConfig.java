package com.rootmen.Database.DatabaseQuery.XmlParser.Errors.Types;

import com.rootmen.Database.DatabaseQuery.XmlParser.Errors.ParserXMLErrors;

public class ExceptionNoConnectionConfig extends ParserXMLErrors {
    String directory;

    public ExceptionNoConnectionConfig(String directory) {
        this.directory = directory;
    }

    @Override
    public String getError() {
        return "Не найден файл CONNECTIONS.xml с конфигурацией параметров подключения в директории: "+ directory;
    }
}
