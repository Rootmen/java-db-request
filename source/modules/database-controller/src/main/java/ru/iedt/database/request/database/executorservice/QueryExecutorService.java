package ru.iedt.database.request.database.executorservice;

import ru.iedt.database.request.database.controller.query.execution.ExecutionInterface;
import ru.iedt.database.request.database.store.QueryStoreList;
import ru.iedt.database.request.structures.nodes.database.Definition;
import ru.iedt.database.request.structures.nodes.database.QuerySet;
import ru.iedt.database.request.structures.xml.parser.StaxStreamParser;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class QueryExecutorService {
    ExecutorService service;
    HashMap<String, Definition> queryStores = new HashMap<>();

    public QueryExecutorService() {
        int countThreadPool;
        try {
            countThreadPool = Integer.parseInt(System.getProperty("executor.service.count", "25"));
        } catch (Exception e) {
            countThreadPool = 25;
        }
        service = Executors.newFixedThreadPool(countThreadPool);
        System.out.println("Количество потоков для обработок запросов: " + countThreadPool);
        this.updateStore();
    }


    public void updateStore() {
        ArrayList<QueryStoreList.QueryStoreMetadata> queryStoresArray = QueryStoreList.getStoresMetadata();
        this.queryStores = new HashMap<>();
        for (QueryStoreList.QueryStoreMetadata metadata : queryStoresArray) {
            String storeName = metadata.getStoreName();
            Definition result = QueryExecutorService.getDefinition(metadata.getStorePatch());
            System.out.println(result);
            this.queryStores.put(storeName, result);
        }

    }

    public static Definition getDefinition(URI definitionLocation) {
        try {
            return StaxStreamParser.parseDefinitions(definitionLocation);
        } catch (Exception e) {
            return null;
        }
    }


    public static <T, F, D> Future<D> startQuery(InputStream xmlQuery, F input, Class<? extends ExecutionInterface<T, F, D>> executionInterfaceClass, Class<? extends ExecutionInterface.ExecutionInterfaceReturn<T, F, D>> executionInterfaceReturnClass) {


        // return service.submit(() -> System.out.println("Another thread was executed"));
        return null;
    }

    public static void shutdown() {
        service.shutdown();
    }
}
