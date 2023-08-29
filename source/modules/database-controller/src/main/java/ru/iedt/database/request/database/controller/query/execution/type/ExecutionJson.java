package ru.iedt.database.request.database.controller.query.execution.type;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ru.iedt.database.request.database.controller.query.execution.ExecutionInterface;

import java.io.InputStream;
import java.sql.ResultSet;

public class ExecutionJson extends ExecutionInterface<ObjectNode, Void, ArrayNode> {

    @Override
    public ObjectNode getNextRow(ResultSet resultSet, Void input) {
        return null;
    }

    public static class ExecutionJsonReturn extends ExecutionInterfaceReturn<ObjectNode, Void, ArrayNode> {
        private final ArrayNode arrayNode = new ObjectMapper().createArrayNode();
        @Override
        public void addResult(ObjectNode result) {
            arrayNode.add(result);
        }

        @Override
        public ArrayNode getResult() {
            return arrayNode;
        }

        @Override
        public boolean isNeedSaveResult() {
            return true;
        }
    }

}
