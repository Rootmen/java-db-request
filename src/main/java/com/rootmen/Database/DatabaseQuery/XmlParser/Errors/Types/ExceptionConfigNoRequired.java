package com.rootmen.Database.DatabaseQuery.XmlParser.Errors.Types;

import com.rootmen.Database.DatabaseQuery.XmlParser.Errors.ParserXMLErrors;

public class ExceptionConfigNoRequired extends ParserXMLErrors {
    String querySet;
    String parameter;

    public ExceptionConfigNoRequired(String querySet, String parameter) {
        this.querySet = querySet;
        this.parameter = parameter;
    }

    @Override
    public String getMessage() {
        return "В запросе " + this.querySet + " не смотрят обязательный параметр " + this.parameter;
    }
}
