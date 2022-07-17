package com.rootmen.Database.DatabaseQuery.XmlParser.Errors.Types;

import com.rootmen.Database.DatabaseQuery.XmlParser.Errors.ParserXMLErrors;

public class ExceptionConfigParseError extends ParserXMLErrors {
    String directory;
    String config;

    public ExceptionConfigParseError(String config, String directory) {
        this.directory = directory;
        this.config = config;
    }

    @Override
    public String getMessage() {
        return "В фале " + this.directory + " CONNECTIONS.xml не удалось создать конфигурацию с " + this.config;
    }
}
