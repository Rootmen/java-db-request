package com.rootmen.Database.DatabaseQuery.XmlParser.Errors.Types;

import com.rootmen.Database.DatabaseQuery.XmlParser.Errors.ParserXMLErrors;

public class ExceptionNoConnectionID extends ParserXMLErrors {
    String id;

    public ExceptionNoConnectionID(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "В файле CONNECTIONS.xml с конфигурацией параметров подключения в директории не найдено подключение для " + id;
    }
}
