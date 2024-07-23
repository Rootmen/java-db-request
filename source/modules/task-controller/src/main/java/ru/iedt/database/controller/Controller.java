package ru.iedt.database.controller;

import io.quarkus.arc.Arc;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Singleton;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import org.reflections.Reflections;
import ru.iedt.database.messaging.WebsocketMessage;

/** Класс-контроллер для выполнения задач. */
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

        System.out.printf("Найдено классов Tasks-runner: %-10s\n", classes.size());
        for (Class<?> clazz : classes) {
            System.out.println(clazz.getName());
        }
        System.out.print("Поиск методов:\n");
        for (Class<?> clazz : classes) {
            Object object = Arc.container().select(clazz).get();
            for (Method m : clazz.getDeclaredMethods()) {
                if (m.isAnnotationPresent(Task.class)) {
                    Task task = m.getAnnotation(Task.class);
                    String name = task.value();
                    System.out.printf(
                            "Задача: %-10s --- метод %-10s класс %-10s\n",
                            name, m.getName(), object.getClass().getName());
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
     * @param task Описание задачи.
     * @return Результат выполнения задачи в виде Uni<Void>.
     * @throws RuntimeException если задача не найдена или возникла ошибка при выполнении.
     */
    @SuppressWarnings("unchecked")
    public Uni<Void> runTask(String taskName, TaskDescription task, WebsocketMessage message) {
        try {
            Method method = methods.get(taskName);
            Object clazz = clazzs.get(taskName);
            if (method == null || clazz == null) throw new RuntimeException("Задача " + taskName + " не найдена");
            return (Uni<Void>) method.invoke(clazz, task, message);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
