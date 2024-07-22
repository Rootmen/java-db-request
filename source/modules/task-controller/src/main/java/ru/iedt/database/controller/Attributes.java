package ru.iedt.database.controller;

public interface Attributes {
interface Rest {
	String USER_ID = "user_id";
	String SOCKET = "socket";
	String SECRET = "secret";
	String APP_ID = "app_id";
	String TASK_ID = "task_id";
	String TASK_NAME = "task_name";
}

interface Websocket {
	String SOCKET = "socket";
	String USER_ID = "user_id";
	String TARGET = "target";
	String TASK_ID = "task_id";
	String TASK_NAME = "task_name";
}
}
