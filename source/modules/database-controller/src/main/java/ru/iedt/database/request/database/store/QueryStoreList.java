package ru.iedt.database.request.database.store;

import org.reflections.Reflections;
import ru.iedt.database.request.database.controller.query.connections.ConnectionsManager;
import ru.iedt.database.request.structures.nodes.database.QuerySet;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class QueryStoreList {
    static public ArrayList<QueryStoreMetadata> getStoresMetadata() {
        Reflections reflections = new Reflections("ru");
        ArrayList<Class<?>> classes = new ArrayList<>(reflections.getTypesAnnotatedWith(DefinitionStore.class));
        ArrayList<QueryStoreMetadata> queryStores = new ArrayList<>();
        for (Class<?> clazz : classes) {
            try {
                QueryStoreDefinition queryStoreDefinition = (QueryStoreDefinition) clazz.getDeclaredConstructor().newInstance();
                queryStores.add(new QueryStoreMetadata(queryStoreDefinition.getStorePath(), queryStoreDefinition.getStoreName()));
            } catch (InstantiationException | IllegalAccessException | URISyntaxException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return queryStores;
    }


    static public class QueryStore {
        String storeName;
        HashMap<String, ConnectionsManager> connections = new HashMap<>();
        HashMap<String, QuerySet> querySet = new HashMap<>();
        public QueryStore(URI storePatch, String storeName) {
            this.storeName = storeName;


        }


        private static ParserXMLErrors checkDirectory(String directory) {
            //Проверка того что директория существует
            if (Files.notExists(Paths.get(directory)) || !Files.isDirectory(Paths.get(directory))) {
                return new ExceptionNoDirectory(directory);
            }
            //Проверка наличия файла CONNECTIONS.xml
            if (Files.notExists(Paths.get(directory + "/CONNECTIONS.xml"))) {
                return new ExceptionNoConnectionConfig(directory);
            }
            //Проверка количества файлов
            try (Stream<Path> files = Files.list(Paths.get(directory))) {
                long count = files.count();
                if (count < 1) {
                    return new ExceptionNoFilesInDirectory(directory);
                }
            } catch (IOException ioException) {
                return new ExceptionNoFilesInDirectory(directory, ioException);
            }
            return null;
        }
    }

    /**
     * Вложенный статический класс QueryStore представляет собой структуру,
     * которая хранит информацию о хранилище запросов.
     */
    static public class QueryStoreMetadata {
        /**
         * Путь к хранилищу запросов.
         */
        URI storePatch;

        /**
         * Имя хранилища запросов.
         */
        String storeName;

        public QueryStoreMetadata(URI storePatch, String storeName) {
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
