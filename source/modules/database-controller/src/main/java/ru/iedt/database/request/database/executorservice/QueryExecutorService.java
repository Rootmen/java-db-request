package ru.iedt.database.request.database.executorservice;

import ru.iedt.database.request.database.controller.query.execution.ExecutionInterface;
import ru.iedt.database.request.database.store.QueryStoreList;
import ru.iedt.database.request.structures.nodes.database.QuerySet;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class QueryExecutorService {
    // ArrayList<QueryStoreList.QueryStoreMetadata> f = QueryStoreList.getStoresArray();
    static ExecutorService service;

    static {
        //TODO нормальные Property
        int countThreadPool;
        try {
            countThreadPool = Integer.parseInt(System.getProperty("executor.service.count", "25"));
        } catch (Exception e) {
            countThreadPool = 25;
        }
        service = Executors.newFixedThreadPool(countThreadPool);
        System.out.println("Количество потоков для обработок запросов: " + countThreadPool);
    }

    static HashMap<String, HashMap<String, QuerySet>> queryStores;

    public void updateStore() {
        ArrayList<QueryStoreList.QueryStoreMetadata> queryStoresArray = QueryStoreList.getStoresMetadata();
        queryStores = new HashMap<>();
        for (QueryStoreList.QueryStoreMetadata metadata : queryStoresArray) {
            String storeName = metadata.getStoreName();
        }

    }


    public <T, F, D> Future<D> startQuery(InputStream xmlQuery, F input, Class<? extends ExecutionInterface<T, F, D>> executionInterfaceClass, Class<? extends ExecutionInterface.ExecutionInterfaceReturn<T, F, D>> executionInterfaceReturnClass) {




        // return service.submit(() -> System.out.println("Another thread was executed"));
        return null;
    }

    public void shutdown() {
        service.shutdown();
    }
}
