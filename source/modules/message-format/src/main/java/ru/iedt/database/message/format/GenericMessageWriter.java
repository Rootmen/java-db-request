package ru.iedt.database.message.format;

import static ru.iedt.database.message.format.MessageUtils.SUCCEED_MESSAGE;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import ru.iedt.database.controller.TaskDescription;
import ru.iedt.database.messaging.WebsocketMessage;
import ru.iedt.database.messaging.WebsocketRequest;

public class GenericMessageWriter {

    public static <T> Uni<Void> sendMessagesFromMulti(
            Uni<Tuple2<Integer, Multi<T>>> result, WebsocketMessage message, TaskDescription task) {
        return sendMessagesFromMulti(result, message, task, TARGET_SOCKET);
    }

    static ExecutorService executorService = Executors.newFixedThreadPool(100);

    public static <T> Uni<Void> sendMessagesFromMulti(
            Uni<Tuple2<Integer, Multi<T>>> result, WebsocketMessage message, TaskDescription task, String target) {
        AtomicInteger index = new AtomicInteger(0);
        return result.onItem()
                .transformToUni(tuple -> message.message(WebsocketRequest.newBuilder()
                                .setUser(task.user_id.toString())
                                .setSocket(task.socket)
                                .setTarget(target)
                                .setTaskName(task.task_name)
                                .setTaskId(task.task_id.toString())
                                .setPayload(MessageUtils.startMessage(tuple.getItem1()))
                                .build())
                        .replaceWith(tuple.getItem2()))
                .onItem()
                .transformToMulti(tMulti -> tMulti)
                .emitOn(executorService)
                .onItem()
                .transformToUni(t -> message.message(WebsocketRequest.newBuilder()
                        .setUser(task.user_id.toString())
                        .setSocket(task.socket)
                        .setTarget(target)
                        .setTaskName(task.task_name)
                        .setTaskId(task.task_id.toString())
                        .setPayload(MessageUtils.rowMessage(index.getAndIncrement(), t))
                        .build()))
                .merge()
                .select()
                .where(i -> false)
                .toUni()
                .onItem()
                .transformToUni(unused -> message.message(WebsocketRequest.newBuilder()
                        .setUser(task.user_id.toString())
                        .setSocket(task.socket)
                        .setTarget(target)
                        .setTaskName(task.task_name)
                        .setTaskId(task.task_id.toString())
                        .setPayload(MessageUtils.endMessage())
                        .build()))
                .replaceWithVoid();
    }

    public static <T> Uni<Void> sendMessagesFromUni(
            Uni<T> result, WebsocketMessage message, TaskDescription task, String target) {
        return result.onItem()
                .transformToUni(t -> message.message(WebsocketRequest.newBuilder()
                        .setUser(task.user_id.toString())
                        .setSocket(task.socket)
                        .setTarget(target)
                        .setTaskName(task.task_name)
                        .setTaskId(task.task_id.toString())
                        .setPayload(MessageUtils.message(t))
                        .build()))
                .replaceWithVoid();
    }

    public static Uni<Void> sendErrorMessage(String errorMessage, WebsocketMessage message, TaskDescription task) {
        return Uni.createFrom()
                .voidItem()
                .onItem()
                .transformToUni(t -> message.message(WebsocketRequest.newBuilder()
                        .setUser(task.user_id.toString())
                        .setSocket(task.socket)
                        .setTarget(TARGET_SOCKET)
                        .setTaskName(task.task_name)
                        .setTaskId(task.task_id.toString())
                        .setPayload(MessageUtils.errorMessage(errorMessage))
                        .build()))
                .replaceWithVoid();
    }

    public static Uni<Void> sendSucceedMessage(WebsocketMessage message, TaskDescription task) {
        return Uni.createFrom()
                .voidItem()
                .onItem()
                .transformToUni(t -> message.message(WebsocketRequest.newBuilder()
                        .setUser(task.user_id.toString())
                        .setSocket(task.socket)
                        .setTarget(TARGET_SOCKET)
                        .setTaskName(task.task_name)
                        .setTaskId(task.task_id.toString())
                        .setPayload(SUCCEED_MESSAGE)
                        .build()))
                .replaceWithVoid();
    }

    public static String TARGET_ALL = "all";
    public static String TARGET_SOCKET = "socket";
}
