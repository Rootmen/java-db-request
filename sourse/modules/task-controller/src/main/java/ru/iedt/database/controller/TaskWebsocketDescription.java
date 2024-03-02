package ru.iedt.database.controller;

import java.util.StringJoiner;
import java.util.UUID;

public class TaskWebsocketDescription {

    public UUID user_id;
    public String socket;
    public String token;
    public UUID app_id;
    public UUID task_id;
    public String task_name;
    public String task_data;

    public TaskWebsocketDescription(UUID user_id, String socket, String token, UUID task_id, String taskName, String task_data) {
        this.user_id = user_id;
        this.socket = socket;
        this.token = token;
        this.task_id = task_id;
        this.task_name = taskName;
        this.task_data = task_data;
    }

    // TODO более сложная проверка
    public boolean isCorrect() {
        return user_id != null && socket != null && token != null && app_id != null && task_id != null && task_name != null && task_data != null;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TaskDescription.class.getSimpleName() + "[", "]")
            .add("user_id=" + user_id)
            .add("socket='" + socket + "'")
            .add("token='" + token + "'")
            .add("app_id=" + app_id)
            .add("id=" + task_id)
            .add("task_name='" + task_name + "'")
            .add("task_data='" + task_data + "'")
            .toString();
    }
}
