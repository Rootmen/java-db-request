package ru.iedt.database.controller.annotation;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import ru.iedt.database.controller.TaskDescription;
import ru.iedt.database.messaging.WebsocketMessage;

@SupportedAnnotationTypes("ru.iedt.database.controller.annotation.TaskSynchronous")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class TaskSynchronousProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        boolean isProcessFalse = false;
        List<? extends Element> elements = roundEnv.getElementsAnnotatedWith(TaskSynchronous.class).stream()
                .toList();
        if (elements.isEmpty()) return false;
        Elements elementUtils = processingEnv.getElementUtils();
        Types types = processingEnv.getTypeUtils();
        TypeMirror taskDescriptionType = elementUtils
                .getTypeElement(TaskDescription.class.getCanonicalName())
                .asType();
        TypeMirror websocketMessageType = elementUtils
                .getTypeElement(WebsocketMessage.class.getCanonicalName())
                .asType();
        TypeMirror uniType =
                elementUtils.getTypeElement(Uni.class.getCanonicalName()).asType();
        TypeMirror tupleType =
                elementUtils.getTypeElement(Tuple2.class.getCanonicalName()).asType();
        TypeMirror voidType =
                elementUtils.getTypeElement(Void.class.getCanonicalName()).asType();
        TypeMirror integerType =
                elementUtils.getTypeElement(Integer.class.getCanonicalName()).asType();
        TypeMirror multiType =
                elementUtils.getTypeElement(Multi.class.getCanonicalName()).asType();

        for (Element element : elements) {
            if (element.getKind() == ElementKind.METHOD) {
                ExecutableElement methodElement = (ExecutableElement) element;
                List<? extends VariableElement> parameters = methodElement.getParameters();
                for (int g = 0; g < parameters.size(); g++) {
                    TypeMirror declaredType = parameters.get(g).asType();
                    if (g == 0) {
                        if (!declaredType.equals(taskDescriptionType)) {
                            isProcessFalse = true;
                            printError(
                                    "Firsts argument in " + element.getSimpleName() + " is not TaskDescription",
                                    element);
                        }
                    }
                    if (g == 1) {
                        if (!declaredType.equals(websocketMessageType)) {
                            isProcessFalse = true;
                            printError(
                                    "Second argument in " + element.getSimpleName() + " is not WebsocketMessage",
                                    element);
                        }
                    }
                }
                if (parameters.size() != 2) {
                    isProcessFalse = true;
                    printError(
                            "In " + element.getSimpleName()
                                    + " not a 2 parameter method need (TaskDescription task, WebsocketMessage websocketMessage)",
                            element);
                }

                TypeMirror returnType = methodElement.getReturnType();

                if (!types.isAssignable(types.erasure(returnType), uniType)) {
                    isProcessFalse = true;
                    printError("return type in " + element.getSimpleName() + " is not Uni", element);
                }

                DeclaredType declaredReturnType = (DeclaredType) returnType;
                List<? extends TypeMirror> genericReturnTypeArray = declaredReturnType.getTypeArguments();
                if (types.isAssignable(types.erasure(genericReturnTypeArray.getFirst()), uniType)) {
                    isProcessFalse = true;
                    printError(
                            "generic return type in " + element.getSimpleName()
                                    + " is not single Uni<?> (Uni in Uni - Uni<Uni<?>>)",
                            element);
                    continue;
                }
                if (types.isAssignable(types.erasure(genericReturnTypeArray.getFirst()), multiType)) {
                    isProcessFalse = true;
                    printError(
                            "generic return type in " + element.getSimpleName()
                                    + " is not single Uni<?> (Multi in Uni - Uni<Multi<?>>)",
                            element);
                    continue;
                }
                TypeMirror genericReturnType = genericReturnTypeArray.getFirst();
                if (types.isAssignable(types.erasure(genericReturnType), tupleType)) {
                    DeclaredType declaredTupleType = (DeclaredType) genericReturnType;
                    List<? extends TypeMirror> genericTupleTypeArray = declaredTupleType.getTypeArguments();
                    if (genericTupleTypeArray.size() != 2) {
                        isProcessFalse = true;
                        printError(
                                "generic return type in " + element.getSimpleName()
                                        + " is not Uni<Tuple2<Integer, Multi<?>>> (Is not Tuple2)",
                                element);
                        continue;
                    }

                    if (!genericTupleTypeArray.getFirst().equals(integerType)) {
                        isProcessFalse = true;
                        printError(
                                "generic return type in " + element.getSimpleName()
                                        + " is not Uni<Tuple2<Integer, Multi<?>>> (First element is not Integer)",
                                element);
                        continue;
                    }

                    if (!types.isAssignable(types.erasure(genericTupleTypeArray.get(1)), multiType)) {
                        isProcessFalse = true;
                        printError(
                                "generic return type in " + element.getSimpleName()
                                        + " is not Uni<Tuple2<Integer, Multi<?>>> (Second element is not Multi)",
                                element);
                        continue;
                    }
                    continue;
                }

                if (genericReturnType.equals(voidType)) {
                    continue;
                }
            } else {
                printError("", element);
                isProcessFalse = true;
            }
        }
        if (isProcessFalse) {
            try {
                throw new RuntimeException("Error");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isProcessFalse;
    }

    private void printError(String message, Element annotatedElement) {
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.ERROR, message, annotatedElement);
    }
}
