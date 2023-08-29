package ru.iedt.database.request.database.controller.query.execution;

import ru.iedt.database.request.database.controller.parameter.Parameter;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExecutionQuery<T, F, D> {
    public ExecutionInterface.ExecutionInterfaceReturn<T, F, D> executionQuery(StringBuilder query, HashMap<String, Parameter<?>> parameters, Connection connection, Class<? extends ExecutionInterface<T, F, D>> executionInterfaceClass, F input, Class<? extends ExecutionInterface.ExecutionInterfaceReturn<T, F, D>> executionInterfaceReturnClass) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ExecutionInterface<T, F, D> executionInterfaceObject = executionInterfaceClass.getDeclaredConstructor().newInstance();
        ExecutionInterface.ExecutionInterfaceReturn<T, F, D> executionInterfaceReturn = executionInterfaceReturnClass.getDeclaredConstructor().newInstance();
        boolean isNeedSaveData = executionInterfaceReturn.isNeedSaveResult();
        try (PreparedStatement statement = this.generatePreparedStatement(query, parameters, connection)) {
            boolean hasMoreResults = statement.execute();
            while (hasMoreResults || statement.getUpdateCount() > -1) {
                ResultSetData resultSetData = getNextData(statement);
                T result = executionInterfaceObject.getNextRow(resultSetData.resultSet, input);
                if (isNeedSaveData) {
                    executionInterfaceReturn.addResult(result);
                }
            }
        }
        return executionInterfaceReturn;
    }


    protected PreparedStatement generatePreparedStatement(StringBuilder text, HashMap<String, Parameter<?>> parameters, Connection connection) throws SQLException {
        String update = text.toString();
        for (Parameter<?> parameter : parameters.values()) {
            HashMap<String, String> when = parameter.getWhen();
            if (when == null || when.size() == 0) continue;
            if (when.get(parameter.getValue().toString()) == null && when.get(null) != null) {
                update = update.replaceAll("\\$" + parameter.getParameterName().substring(1, parameter.getParameterName().length() - 1) + "\\$", Matcher.quoteReplacement(when.get(null)));
            } else if (when.get(parameter.getValue().toString()) != null) {
                update = update.replaceAll("\\$" + parameter.getParameterName().substring(1, parameter.getParameterName().length() - 1) + "\\$", Matcher.quoteReplacement(when.get(parameter.getValue().toString())));
            }
        }
        text = new StringBuilder(update);
        Matcher matcher = Pattern.compile("\\$.*?\\$").matcher(text);
        ArrayList<String> parametersArray = new ArrayList<>();
        while (matcher.find()) {
            String token = text.substring(matcher.start(), matcher.end());
            if (parameters.containsKey(token)) {
                parametersArray.add(token);
            }
        }
        PreparedStatement statement = connection.prepareStatement(text.toString().replaceAll("\\$.*?\\$", "?"));
        for (int g = 0; g < parametersArray.size(); g++) {
            Parameter<?> current = parameters.get(parametersArray.get(g));
            if (current != null) {
                current.addParameterToStatement(statement, g + 1, connection);
            }
        }
        return statement;
    }

    public ResultSetData getNextData(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.getResultSet();
        if (resultSet == null) {
            return null;
        }
        resultSet.setFetchSize(10);
        return new ResultSetData(resultSet, resultSet.getMetaData());
    }

    static class ResultSetData {
        public ResultSet resultSet;
        public ResultSetMetaData resultSetMetaData;

        public ResultSetData(ResultSet resultSet, ResultSetMetaData resultSetMetaData) {
            this.resultSet = resultSet;
            this.resultSetMetaData = resultSetMetaData;
        }
    }


}
