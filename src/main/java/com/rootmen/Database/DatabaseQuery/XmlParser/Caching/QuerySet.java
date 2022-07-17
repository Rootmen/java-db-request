package com.rootmen.Database.DatabaseQuery.XmlParser.Caching;

import com.rootmen.Database.DatabaseQuery.Query.ConnectionsManager;
import com.rootmen.Database.DatabaseQuery.XmlParser.Caching.Entity.QueryList;

import java.util.HashMap;
import java.util.LinkedList;

public class QuerySet {
    public static HashMap<String, LinkedList<QueryList>> cachedQuery = new HashMap<>();
    public static HashMap<String, HashMap<String, ConnectionsManager>> cachedConnection = new HashMap<>();
}
