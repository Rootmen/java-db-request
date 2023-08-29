package ru.iedt.database.request.database.controller.query.execution.type;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ru.iedt.database.request.database.controller.query.execution.ExecutionInterface;

import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;

public class ExecutionServlet extends ExecutionInterface<Void, PrintWriter, Void> {

    @Override
    public Void getNextRow(ResultSet resultSet, PrintWriter input) {
        return null;
    }

    public static class ExecutionServletReturn extends ExecutionInterfaceReturn<Void, PrintWriter, Void> {
        @Override
        public void addResult(Void result) {

        }

        @Override
        public Void getResult() {
            return null;
        }

        @Override
        public boolean isNeedSaveResult() {
            return true;
        }
    }

}
