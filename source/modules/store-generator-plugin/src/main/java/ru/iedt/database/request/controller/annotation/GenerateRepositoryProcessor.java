package ru.iedt.database.request.controller.annotation;

import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import io.quarkus.deployment.CodeGenContext;
import io.quarkus.deployment.CodeGenProvider;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;


public class GenerateRepositoryProcessor implements CodeGenProvider {
    @Override
    public String providerId() {
        return "java-db-request-store-generator-plugin";
    }

    @Override
    public String inputDirectory() {
        return "java";
    }

    @Override
    public boolean trigger(CodeGenContext context) {
        System.out.println("Demo =======================================");
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();

        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
                .build();

        try {
            javaFile.writeTo(new File(context.inputDir().toString() + "/demo.java"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

  /*  @BuildStep
    public void handleEndpointParams(ResteasyReactiveResourceMethodEntriesBuildItem resourceMethodEntries, JaxRsResourceIndexBuildItem jaxRsIndex) {

        IndexView indexView = jaxRsIndex.getIndexView();

        Map<String, ClassInfo> jsonClasses = new HashMap<>();
        for (ResteasyReactiveResourceMethodEntriesBuildItem.Entry entry : resourceMethodEntries.getEntries()) {
            MethodInfo methodInfo = entry.getMethodInfo();
            ClassInfo returnClassInfo = indexView.getClassByName(methodInfo.returnType().name());
            if (returnClassInfo != null) {
                jsonClasses.put(returnClassInfo.name().toString(), returnClassInfo);
            }
        }

        if (!jsonClasses.isEmpty()) {
            // TODO: generate serializers for each returned type
        }
    }*/
}
