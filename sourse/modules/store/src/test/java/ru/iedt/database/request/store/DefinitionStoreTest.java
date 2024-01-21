package ru.iedt.database.request.store;

import io.quarkus.test.junit.QuarkusTest;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class DefinitionStoreTest {

    @Test
    public void testQueryStoreList() throws Exception {
        System.out.println("Поиск хранилищ");
        ArrayList<QueryStoreDefinition> queryStoreDefinitions = QueryStoreList.getStoresMetadata();
        System.out.printf("Найдено %s хранилищ из 3\n", queryStoreDefinitions.size());
        Assertions.assertEquals(queryStoreDefinitions.size(), 3, "Неправильно определено количество хранилищ");
        System.out.println("Проверка корректности работы поиска файла");
        for (QueryStoreDefinition qStoreDefinition : queryStoreDefinitions) {
            String fileName = qStoreDefinition.getStorePath().getPath();
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String fileContent = reader.readLine();
            System.out.printf(
                    "Хранилище: %-10s - путь к нему: %-40s - содержимое: %s\n",
                    qStoreDefinition.getStoreName(), fileName, fileContent);
            Assertions.assertEquals(
                    fileContent,
                    qStoreDefinition.getStoreName(),
                    "Неправильно прочитано название хранилища\\содержимое фала");
        }
    }
}
