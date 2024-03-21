package ru.iedt.database.request.store;

import java.net.URI;
import java.util.ArrayList;
import org.reflections.Reflections;

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
    public static ArrayList<QueryStoreDefinition> getStoresMetadata() {
        // Инициализация рефлексии и списка для хранения классов и метаданных
        Reflections reflections = new Reflections("ru");
        ArrayList<Class<?>> classArrayList = new ArrayList<>(reflections.getTypesAnnotatedWith(DefinitionStore.class));

        System.out.printf("Найдено аннотаций: %-10s", classArrayList.size());
        ArrayList<QueryStoreDefinition> queryStores = new ArrayList<>();

        // Итерация по классам с аннотацией @DefinitionStore и создание их экземпляров в массив queryStores
        for (Class<?> classElement : classArrayList) {
            try {
                queryStores.add((QueryStoreDefinition) classElement.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        // Возвращение списка с метаданными классов
        System.out.printf("Найдено хранилищ: %-10s\n", queryStores.size());
        for (QueryStoreDefinition queryStore : queryStores) {
            System.out.println(queryStore);
        }
        // Возвращение списка с метаданными классов
        return queryStores;
    }

    /**
     * Вложенный статический класс QueryStore представляет собой структуру,
     * которая хранит информацию о хранилище запросов.
     */
    public static class QueryStoreMetadata {

        /**
         * Путь к хранилищу запросов.
         */
        private final URI storePatch;

        /**
         * Имя хранилища запросов.
         */
        private final String storeName;

        public QueryStoreMetadata(URI storePatch, String storeName) {
            this.storeName = storeName;
            this.storePatch = storePatch;
        }

        @Override
        public String toString() {
            return "QueryStore{" + "storePatch=" + storePatch + ", storeName='" + storeName + '\'' + '}';
        }

        public URI getStorePatch() {
            return storePatch;
        }

        public String getStoreName() {
            return storeName;
        }
    }
}
