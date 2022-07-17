package com.rootmen.Database.DatabaseQuery.XmlParser.Caching.Entity;

import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.Parameter;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterFactory;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class QueryList {
    public List<SQL> queryList;
    public LinkedList<ParameterCaching> parameters;
    public String name;

    public QueryList(LinkedList<SQL> queryList, LinkedList<ParameterCaching> parameters, String queryName) {
        this.queryList = queryList;
        this.parameters = parameters;
        this.name = queryName;
    }

    static public class SQL {
        public String connection;
        public String query;
        public String name;

        public SQL(String query, String name, String connection) {
            this.query = query;
            this.name = name;
            this.connection = connection;
        }
    }

    static public class ParameterCaching {
        String ID;
        String name;
        String type;
        String defaults;
        HashMap<String, String> when;

        public ParameterCaching(String ID, String name, String type, HashMap<String, String> when, String defaults) {
            this.ID = ID;
            this.name = name;
            this.type = type;
            this.when = when;
            this.defaults = defaults;
        }
    }

    static public HashMap<String, Parameter<?>> generateParameters(ArrayList<ParameterInput> parameterInputs, LinkedList<ParameterCaching> parameters) throws ParameterException {
        HashMap<String, String> parameterInputHashMap = new HashMap<>();
        for (ParameterInput parameterInput : parameterInputs) {
            parameterInputHashMap.put(parameterInput.name, parameterInput.value);
        }
        HashMap<String, Parameter<?>> parameterHashMap = new HashMap<>();
        for (ParameterCaching parameterCaching : parameters) {
            String values = (parameterInputHashMap.containsKey(parameterCaching.ID)) ? parameterInputHashMap.get(parameterCaching.ID) : parameterCaching.defaults;
            Parameter<?> parameter = ParameterFactory.getParameter(parameterCaching.ID, parameterCaching.name, parameterCaching.type, values);
            parameter.setWhen(parameterCaching.when);
            parameterHashMap.put(parameterCaching.name, parameter);
        }

        return parameterHashMap;

    }

}
