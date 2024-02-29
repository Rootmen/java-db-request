package ru.iedt.database.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.rabbitmq.IncomingRabbitMQMetadata;
import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata;
import jakarta.json.JsonObject;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;

public class Utils {

    static ObjectMapper mapper = io.vertx.core.json.jackson.DatabindCodec.mapper();

    public static TaskDescription parseMessageFromRest(Message<JsonObject> payload) {
        Optional<IncomingRabbitMQMetadata> metadata = payload.getMetadata(IncomingRabbitMQMetadata.class);
        if (metadata.isEmpty()) {
            throw new RuntimeException();
        }
        IncomingRabbitMQMetadata mqMetadata = metadata.get();
        return new TaskDescription(
            UUID.fromString(mqMetadata.getHeader(Attributes.Rest.USER_ID, String.class).orElse("")),
            mqMetadata.getHeader(Attributes.Rest.SOCKET, String.class).orElse(null),
            mqMetadata.getHeader(Attributes.Rest.SECRET, String.class).orElse(null),
            UUID.fromString(mqMetadata.getHeader(Attributes.Rest.APP_ID, String.class).orElse("")),
            UUID.fromString(mqMetadata.getHeader(Attributes.Rest.TASK_ID, String.class).orElse("")),
            mqMetadata.getHeader(Attributes.Rest.TASK_NAME, String.class).orElse(null),
            payload.getPayload().toString()
        );
    }

    public static TaskWebsocketDescription parseMessageFromWebsocket(Message<JsonObject> payload) {
        Optional<IncomingRabbitMQMetadata> metadata = payload.getMetadata(IncomingRabbitMQMetadata.class);
        if (metadata.isEmpty()) {
            throw new RuntimeException();
        }
        IncomingRabbitMQMetadata mqMetadata = metadata.get();
        return new TaskWebsocketDescription(
            UUID.fromString(mqMetadata.getHeader(Attributes.Rest.USER_ID, String.class).orElse("")),
            mqMetadata.getHeader(Attributes.Rest.SOCKET, String.class).orElse(null),
            mqMetadata.getHeader(Attributes.Rest.SECRET, String.class).orElse(null),
            UUID.fromString(mqMetadata.getHeader(Attributes.Rest.APP_ID, String.class).orElse("")),
            UUID.fromString(mqMetadata.getHeader(Attributes.Rest.TASK_ID, String.class).orElse("")),
            mqMetadata.getHeader(Attributes.Rest.TASK_NAME, String.class).orElse(null),
            mapper.valueToTree(payload.getPayload()).toString()
        );
    }

    public static void addRabbitTaskRest(Emitter<String> emitter, String user, String socket, String taskName, String token, String appId, String taskId, String json) {
        OutgoingRabbitMQMetadata metadata = new OutgoingRabbitMQMetadata.Builder()
            .withHeader(Attributes.Rest.USER_ID, user)
            .withHeader(Attributes.Rest.SOCKET, socket)
            .withHeader(Attributes.Rest.TASK_NAME, taskName)
            .withHeader(Attributes.Rest.SECRET, token)
            .withHeader(Attributes.Rest.APP_ID, appId)
            .withHeader(Attributes.Rest.TASK_ID, taskId)
            .withContentType("application/json")
            .withTimestamp(ZonedDateTime.now())
            .build();
        emitter.send(Message.of(json, Metadata.of(metadata)));
    }

    public static void addRabbitTaskWebsocket(Emitter<String> emitter, String user, String target, String taskName, String taskId, String json) {
        OutgoingRabbitMQMetadata metadata = new OutgoingRabbitMQMetadata.Builder()
            .withHeader(Attributes.Websocket.USER_ID, user)
            .withHeader(Attributes.Websocket.TASK_NAME, taskName)
            .withHeader(Attributes.Websocket.TASK_ID, taskId)
            .withHeader(Attributes.Websocket.TARGET, target)
            .withContentType("application/json")
            .withTimestamp(ZonedDateTime.now())
            .build();
        emitter.send(Message.of(json, Metadata.of(metadata)));
    }
}
