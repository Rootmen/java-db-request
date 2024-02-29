package ru.iedt.database.controller;

import java.util.StringJoiner;
import java.util.UUID;

public class TaskDescription {
    public UUID user;
    public String socket;
    public String token;
    public UUID app;
    public UUID id;
    public String taskName;
    public String taskData;

    public TaskDescription(
            UUID user, String socket, String token, UUID app, UUID id, String taskName, String taskData) {
        this.user = user;
        this.socket = socket;
        this.token = token;
        this.app = app;
        this.id = id;
        this.taskName = taskName;
        this.taskData = taskData;
    }

    // TODO более сложная проверка
    public boolean isCorrect() {
        return user != null
                && socket != null
                && token != null
                && app != null
                && id != null
                && taskName != null
                && taskData != null;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TaskDescription.class.getSimpleName() + "[", "]")
                .add("user=" + user)
                .add("socket='" + socket + "'")
                .add("token='" + token + "'")
                .add("app=" + app)
                .add("id=" + id)
                .add("taskName='" + taskName + "'")
                .add("taskData='" + taskData + "'")
                .toString();
    }
}
