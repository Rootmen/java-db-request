package ru.iedt.database.controller;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Singleton;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.reflections.Reflections;

/**
 * Класс-контроллер для выполнения задач.
 * Реализует паттерн Singleton.
 */
@Singleton
public class Controller {

    private static final HashMap<String, Method> methods = new HashMap<>();
    private static final HashMap<String, Object> clazzs = new HashMap<>();

    /**
     * Статический блок инициализации для поиска методов с аннотацией @Task и сохранения их в maps.
     */
    static {
        Reflections reflections = new Reflections("ru");
        ArrayList<Class<?>> classes = new ArrayList<>(reflections.getTypesAnnotatedWith(Tasks.class));
        for (Class<?> clazz : classes) {
            Object object = CDI.current().select(clazz).get();
            for (Method m : clazz.getDeclaredMethods()) {
                if (m.isAnnotationPresent(Task.class)) {
                    Task task = m.getAnnotation(Task.class);
                    String name = task.value();
                    methods.put(name, m);
                    clazzs.put(name, object);
                }
            }
        }
    }

    /**
     * Метод для выполнения задачи.
     *
     * @param taskName Название задачи.
     * @param task     Описание задачи.
     * @return Результат выполнения задачи в виде Uni<Void>.
     * @throws RuntimeException если задача не найдена или возникла ошибка при выполнении.
     */
    public static Uni<Void> runTask(String taskName, TaskDescription task, Emitter<String> emitter) {
        try {
            Method method = methods.get(taskName);
            Object clazz = clazzs.get(taskName);
            if (method == null || clazz == null) throw new RuntimeException("Задача " + taskName + " не найдена");
            return (Uni<Void>) method.invoke(clazz, task, emitter);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
