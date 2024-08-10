package ru.iedt.database.message.format;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.runtime.annotations.RegisterForReflection;

public class MessageUtils {

    public static String SUCCEED_MESSAGE = "{\"succeed\":true}";
    public static String INIT_MESSAGE = "{\"status\":0}";

    public static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.setVisibility(mapper.getSerializationConfig()
                .getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
    }

    public static String errorMessage(String error) {
        if (error == null) error = "error is null";
        return String.format("{\"succeed\":false,\"error\":\"%s\"}", error.replace("\"", "\\\""));
    }

    public static String message(Object message) {
        try {
            return String.format("{\"succeed\":true,\"result\":%s}", mapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // ------------------------------------------
    public static String startMessage(int rowCount) {
        try {
            return String.format("{\"status\":1,\"row_count\":%s}", mapper.writeValueAsString(rowCount));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String rowMessage(int index, Object message) {
        try {
            return mapper.writeValueAsString(new RowLine(index, message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String SUCCEED_PACKET_MESSAGE = "{\"status\": 3,\"succeed\":true}";

    public static String endMessage() {
        return SUCCEED_PACKET_MESSAGE;
    }

    public static RowDelete rowDeleted(Object message) {
        return new RowDelete(message);
    }

    @RegisterForReflection
    public static class RowLine {

        public int status = 2;
        public final int index;
        public final Object result;

        public RowLine(int index, Object result) {
            this.index = index;
            this.result = result;
        }
    }

    @RegisterForReflection
    public static class RowDelete {

        public int status = 4;
        public final Object deleted_id;

        public RowDelete(Object deleted_id) {
            this.deleted_id = deleted_id;
        }
    }
}
