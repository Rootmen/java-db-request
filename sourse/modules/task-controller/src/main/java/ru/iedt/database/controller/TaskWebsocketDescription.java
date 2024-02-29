package ru.iedt.database.controller;

import java.util.StringJoiner;
import java.util.UUID;

public class TaskWebsocketDescription {

    public UUID user_id;
    public String socket;
    public String token;
    public UUID app_id;
    public UUID task_id;
    public String taskName;
    public String taskData;

    public TaskWebsocketDescription(UUID user_id, String socket, String token, UUID app_id, UUID task_id, String taskName, String taskData) {
        this.user_id = user_id;
        this.socket = socket;
        this.token = token;
        this.app_id = app_id;
        this.task_id = task_id;
        this.taskName = taskName;
        this.taskData = taskData;
    }

    // TODO более сложная проверка
    public boolean isCorrect() {
        return user_id != null && socket != null && token != null && app_id != null && task_id != null && taskName != null && taskData != null;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TaskDescription.class.getSimpleName() + "[", "]")
            .add("user_id=" + user_id)
            .add("socket='" + socket + "'")
            .add("token='" + token + "'")
            .add("app_id=" + app_id)
            .add("id=" + task_id)
            .add("taskName='" + taskName + "'")
            .add("taskData='" + taskData + "'")
            .toString();
    }
}
