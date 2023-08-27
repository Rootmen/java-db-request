package ru.iedt.database.request.database.store;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class QueryStoreList {
    static public ArrayList<QueryStore> getStoresArray() {
        Reflections reflections = new Reflections("ru");
        ArrayList<Class<?>> classes = new ArrayList<>(reflections.getTypesAnnotatedWith(DefinitionStore.class));
        ArrayList<QueryStore> queryStores = new ArrayList<>();
        for (Class<?> clazz : classes) {
            try {
                QueryStoreDefinition queryStoreDefinition = (QueryStoreDefinition) clazz.getDeclaredConstructor().newInstance();
                queryStores.add(new QueryStore(queryStoreDefinition.getStorePath(), queryStoreDefinition.getStoreName()));
            } catch (InstantiationException | IllegalAccessException | URISyntaxException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return queryStores;
    }

    /**
     * Вложенный статический класс QueryStore представляет собой структуру,
     * которая хранит информацию о хранилище запросов.
     */
    static public class QueryStore {
        /**
         * Путь к хранилищу запросов.
         */
        URI storePatch;

        /**
         * Имя хранилища запросов.
         */
        String storeName;

        public QueryStore(URI storePatch, String storeName) {
            this.storeName = storeName;
            this.storePatch = storePatch;
        }

        @Override
        public String toString() {
            return "QueryStore{" +
                    "storePatch=" + storePatch +
                    ", storeName='" + storeName + '\'' +
                    '}';
        }
    }
}
