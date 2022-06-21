package com.rootmen.Database.DatabaseQuery.XmlParser;

import com.rootmen.Database.DatabaseQuery.Parameter.Parameter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class QueryList {
    List<SQL> queryList;
    HashMap<String, Parameter<?>> parameters;
    String name;

    QueryList(LinkedList<SQL> _queryList, HashMap<String, Parameter<?>> _parameters, String _queryName) {
        this.queryList = _queryList;
        this.parameters = _parameters;
        this.name = _queryName;
    }

    static class SQL {
        String query;
        String name;

        SQL(String _query, String _name) {
            this.query = _query;
            this.name = _name;
        }
    }
}
