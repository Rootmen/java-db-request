package ru.iedt.database.request.database.executorservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class QueryExecutorService {

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
        System.out.println("Количество потоков для обработок запросов : " + countThreadPool);
    };
    public Future<?> startQuery(String store, String queryName) {
        return service.submit(() -> System.out.println("Another thread was executed"));
    }
}
