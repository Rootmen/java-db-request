package ru.iedt.database.request.store;

import org.reflections.Reflections;

import java.net.URI;
import java.util.ArrayList;

public class QueryStoreList {

    /**
     * Метод для получения метаданных из классов, отмеченных аннотацией @DefinitionStore.
     * <p>
     * Этот метод использует рефлексию для поиска классов и создания их экземпляров, отмеченных аннотацией @DefinitionStore,
     * для извлечения и хранения метаданных.
     *
     * @return Список QueryStoreDefinition, содержащий метаданные классов, отмеченных аннотацией @DefinitionStore.
     * @throws RuntimeException В случае возникновения ошибок при создании экземпляров классов с метаданными.
     */
    static public ArrayList<QueryStoreDefinition> getStoresMetadata() {
        // Инициализация рефлексии и списка для хранения классов и метаданных
        Reflections reflections = new Reflections("ru");
        ArrayList<Class<?>> classArrayList = new ArrayList<>(reflections.getTypesAnnotatedWith(DefinitionStore.class));
        ArrayList<QueryStoreDefinition> queryStores = new ArrayList<>();

        // Итерация по классам с аннотацией @DefinitionStore и создание их экземпляров в массив queryStores
        for (Class<?> classElement : classArrayList) {
            try {
                queryStores.add((QueryStoreDefinition) classElement.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        // Возвращение списка с метаданными классов5
        return queryStores;
    }

    public static void main(String[] args) {
        ArrayList<QueryStoreDefinition> queryStoreDefinitions = getStoresMetadata();
        System.out.println(queryStoreDefinitions);
    }

  /*  static public class QueryStore {
        String storeName;
        HashMap<String, ConnectionsManager> connections = new HashMap<>();
        HashMap<String, QuerySet> querySet = new HashMap<>();

        public QueryStore(URI storePatch, String storeName) {
            this.storeName = storeName;


        }


*//*        private static ParserXMLErrors checkDirectory(String directory) {
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
    }*//*
    }*/

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

        public URI getStorePatch() {
            return storePatch;
        }

        public String getStoreName() {
            return storeName;
        }
    }
}
