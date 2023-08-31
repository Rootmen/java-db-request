package ru.iedt.database.request.demo;

import ru.iedt.database.request.database.store.QueryStoreList;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        ArrayList<QueryStoreList.QueryStoreMetadata> stores = QueryStoreList.getStoresMetadata();
        System.out.println(stores);
    }
}