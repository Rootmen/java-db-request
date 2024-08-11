package ru.iedt.database.controller;

import io.quarkus.arc.Arc;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.inject.Singleton;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import org.reflections.Reflections;
import ru.iedt.database.controller.annotation.Task;
import ru.iedt.database.controller.annotation.TaskSynchronous;
import ru.iedt.database.controller.annotation.Tasks;
import ru.iedt.database.messaging.WebsocketMessage;

/**
 * Класс-контроллер для выполнения задач.
 */
@Singleton
public class Controller {

    private static final HashMap<String, Method> methodsOld = new HashMap<>();
    private static final HashMap<String, Method> methodsNew = new HashMap<>();
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
                    methodsOld.put(name, m);
                    clazzs.put(name, object);
                }
                if (m.isAnnotationPresent(TaskSynchronous.class)) {
                    TaskSynchronous task = m.getAnnotation(TaskSynchronous.class);
                    String name = task.value();
                    System.out.printf(
                            "Задача: %-10s --- метод %-10s класс %-10s\n",
                            name, m.getName(), object.getClass().getName());
                    methodsNew.put(name, m);
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
    @SuppressWarnings("unchecked")
    public Uni<ReturnTaskType<?>> runTaskSynchronous(String taskName, TaskDescription task, WebsocketMessage message) {
        try {
            if (methodsOld.containsKey(taskName)) {
                return this.runTaskOld(taskName, task, message)
                        .onItem()
                        .transform(
                                unused -> new ReturnTaskType<>(Uni.createFrom().voidItem(), true));
            }
            Method method = methodsNew.get(taskName);
            Object clazz = clazzs.get(taskName);
            if (method == null || clazz == null) throw new RuntimeException("Задача " + taskName + " не найдена");
            Class<?> arrayGeneric = (Class<?>) ((ParameterizedType)
                            (((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0]))
                    .getRawType();
            ((ParameterizedType) (((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0]))
                    .getRawType();
            if (arrayGeneric == Tuple2.class) {
                return ((Uni<Tuple2<Integer, Multi<?>>>) method.invoke(clazz, task, message))
                        .onItem()
                        .transform(objects -> new ReturnTaskType<>(objects.getItem1(), objects.getItem2(), false));
            } else if (arrayGeneric == ReturnTaskType.class) {
                return Uni.createFrom()
                        .item(Unchecked.supplier(
                                () -> new ReturnTaskType<>((Uni<?>) method.invoke(clazz, task, message), false)));
            }
            throw new RuntimeException("Задача " + taskName + " не найдена");
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated(since = "Для совемстимости")
    public Uni<Void> runTask(String taskName, TaskDescription task, WebsocketMessage message) {
        return this.runTaskSynchronous(taskName, task, message)
                .onItem()
                .transformToUni(ReturnTaskType::getUni)
                .replaceWithVoid();
    }

    @SuppressWarnings("unchecked")
    private Uni<Void> runTaskOld(String taskName, TaskDescription task, WebsocketMessage message) {
        try {
            Method method = methodsOld.get(taskName);
            Object clazz = clazzs.get(taskName);
            return (Uni<Void>) method.invoke(clazz, task, message);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
