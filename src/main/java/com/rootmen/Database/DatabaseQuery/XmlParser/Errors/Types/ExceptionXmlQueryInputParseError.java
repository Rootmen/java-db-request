package com.rootmen.Database.DatabaseQuery.XmlParser.Errors.Types;

import com.rootmen.Database.DatabaseQuery.XmlParser.Errors.ParserXMLErrors;

public class ExceptionXmlQueryInputParseError extends ParserXMLErrors {
    String config;

    public ExceptionXmlQueryInputParseError(String config) {
        this.config = config;
    }

    @Override
    public String getMessage() {
        return "Не удалось прочитать конфигурацию " + this.config;
    }
}
