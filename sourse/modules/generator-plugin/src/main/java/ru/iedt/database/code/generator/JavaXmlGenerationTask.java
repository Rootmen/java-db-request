package ru.iedt.database.code.generator;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import ru.iedt.database.request.store.QueryStoreDefinition;

import javax.tools.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JavaXmlGenerationTask extends DefaultTask {
    File outputFile = new File(
            getProject().getLayout().getBuildDirectory().get().getAsFile().getPath(), "myfile.txt");

    @TaskAction
    public void generationJava() {
        try {
            List<String> javaFilesPatch = new ArrayList<>();
            List<String> finalJavaFiles = javaFilesPatch;
            getProject().fileTree("./src/main/java").visit(fileVisitDetails -> {
                if (!fileVisitDetails.isDirectory()) {
                    String path = fileVisitDetails.getPath();
                    if (path.endsWith(".java")) {
                        finalJavaFiles.add(path);
                    }
                }
            });

            javaFilesPatch = finalJavaFiles.stream()
                    .filter(javaFile -> {
                        File file = getProject()
                                .getLayout()
                                .getProjectDirectory()
                                .dir("./src/main/java/" + javaFile)
                                .getAsFile();
                        if (file.exists()) {
                            try {
                                boolean checkExtends = Files.lines(Paths.get(file.getPath()), StandardCharsets.UTF_8)
                                        .anyMatch("extends QueryStoreDefinition"::contains);
                                boolean checkAnnotation = Files.lines(Paths.get(file.getPath()), StandardCharsets.UTF_8)
                                        .anyMatch("@DefinitionStore"::contains);
                                return checkAnnotation && checkExtends;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return false;
                    })
                    .toList();

            System.out.println("Найдено репозиториев : " + javaFilesPatch.size());
            javaFilesPatch.forEach(System.out::println);

            List<File> files = javaFilesPatch.stream().map(s -> getProject()
                    .getLayout()
                    .getProjectDirectory()
                    .dir("./src/main/java/" + s)
                    .getAsFile()).toList();

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            QueryStoreDefinition query2StoreDefinition = new QueryStoreDefinition() {
                @Override
                public String getResourcePatch() {
                    return "";
                }

                @Override
                public String getStoreName() {
                    return "";
                }

                @Override
                public Class<?> getResourceClass() {
                    return null;
                }
            };
            try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
                // получаем список всех файлов описывающих исходники
                Iterable<? extends JavaFileObject> javaFiles = fileManager.getJavaFileObjectsFromFiles(files);

                DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
                // заводим задачу на компиляцию
                JavaCompiler.CompilationTask task =
                        compiler.getTask(null, fileManager, diagnostics, null, null, javaFiles);
                // выполняем задачу
                task.call();
                // выводим ошибки, возникшие в процессе компиляции
                for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
                    System.out.format(diagnostic.getMessage(Locale.getDefault()));
                    System.out.format("Error on line %d in %s%n", diagnostic.getLineNumber(), diagnostic.getSource());
                }
            }

            ClassLoader classLoader = System.class.getClassLoader();
            // получаем путь до нашей папки со сгенерированным кодом
            URLClassLoader urlClassLoader =
                    new URLClassLoader(new URL[] {Paths.get(".").toUri().toURL()}, classLoader);

            for (String classPatch : javaFilesPatch) {
                classPatch = classPatch.replaceAll("//", ".");
                try {
                    Class<? extends QueryStoreDefinition> queryStoreDefinition =
                            (Class<? extends QueryStoreDefinition>) urlClassLoader.loadClass(classPatch);
                    System.out.println(queryStoreDefinition.getConstructor().newInstance().getResourcePatch());
                } catch (Exception ignored) {

                }
            }

            System.out.println("You are ricroll");
            outputFile.getParentFile().mkdirs();
            outputFile.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true));
            writer.write("HELLO FROM MY PLUGIN");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}