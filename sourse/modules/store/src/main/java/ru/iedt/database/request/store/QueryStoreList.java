package ru.iedt.database.request.store;

import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<Class<?>> classArrayList = null;
        try {
            classArrayList =
            ClassPath
                .from(ClassLoader.getPlatformClassLoader())
                .getAllClasses()
                .stream()
                .filter(clazz -> {
                    System.out.println(clazz);
                    try {
                        if (clazz.load().getSuperclass() == null) return false;
                        return clazz.load().getSuperclass().getName().equals("ru.iedt.database.request.store.QueryStoreDefinition");
                    } catch (NoClassDefFoundError e) {
                        return false;
                    }
                })
                .map(ClassPath.ClassInfo::load)
                .filter(load -> !Arrays.stream(load.getAnnotations()).filter(annotation -> annotation.toString().equals("@ru.iedt.database.request.store.DefinitionStore()")).collect((Collectors.toSet())).isEmpty())
                .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("Найдено аннотаций: %-10s", classArrayList.size());
        ArrayList<QueryStoreDefinition> queryStores = new ArrayList<>();

        // Итерация по классам с аннотацией @DefinitionStore и создание их экземпляров в массив queryStores
        for (Class<?> classElement : classArrayList) {
            try {
                queryStores.add((QueryStoreDefinition) Class.forName(classElement.getName()).getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.printf("Найдено хранилищ: %-10s", queryStores.size());
        System.out.printf(queryStores.toString());
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
