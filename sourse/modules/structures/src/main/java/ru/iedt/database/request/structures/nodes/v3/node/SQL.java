package ru.iedt.database.request.structures.nodes.v3.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.iedt.database.request.structures.nodes.v3.Elements;

public class SQL implements Elements.SQL {

    protected final StringBuilder value;
    protected final String name;
    protected final String wrapper;

    public SQL(StringBuilder value, String name, String wrapper) {
        this.value = value;
        this.name = name;
        this.wrapper = wrapper;
    }

    public StringBuilder getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getWrapper() {
        return wrapper;
    }

    @Override
    public String toString() {
        return String.format(
                "[name=%s, wrapper=%s, value=%s]",
                name, wrapper, value.toString().replaceAll("\\s+", " ").replaceAll("\\n", " "));
    }

    public static class InsertData {
        private final String sql;
        private final List<String> parametersTokens;
        protected final String name;

        protected final String wrapper;

        public InsertData(String sql, List<String> parametersTokens, String name, String wrapper) {
            this.sql = sql;
            this.parametersTokens = parametersTokens;
            this.name = name;
            this.wrapper = wrapper;
        }

        public String getSql() {
            return sql;
        }

        public List<String> getParametersTokens() {
            return parametersTokens;
        }

        public String getName() {
            return name;
        }

        public String getWrapper() {
            return wrapper;
        }
    }

    public static InsertData getInsertData(Elements.SQL sql, Map<String, Elements.Parameter<?>> parameters) {
        String update = sql.getValue().toString();
        // TODO более продуманная обработка
        for (Elements.Parameter<?> parameter : parameters.values()) {
            Map<String, String> when = parameter.getWhenMap();
            if (when == null || when.isEmpty()) continue;
            String parameterName = parameter.getParameterName();
            String regex = String.format("\\$%s\\$", parameterName);
            String parameterValue = parameter.getValue().toString();
            if (when.get(parameterValue) == null && when.get(null) != null) {
                update = update.replaceAll(regex, Matcher.quoteReplacement(when.get(null)));
            } else if (when.get(parameterValue) != null) {
                update = update.replaceAll(regex, Matcher.quoteReplacement(when.get(parameterValue)));
            }
        }
        Matcher matcher = Pattern.compile("\\$.*?\\$").matcher(update);
        List<String> parametersTokens = new ArrayList<>();
        StringBuilder out = new StringBuilder();
        int index = 1;
        while (matcher.find()) {
            String token = update.substring(matcher.start(), matcher.end()).substring(1);
            if (!token.isEmpty()) {
                token = token.substring(0, token.length() - 1);
            }
            parametersTokens.add(token);
            matcher.appendReplacement(out, "");
            out.append("$")
                    .append(index)
                    .append("::")
                    .append(parameters.get(token).getParameterType());
            index++;
        }
        return new InsertData(matcher.appendTail(out).toString(), parametersTokens, sql.getName(), sql.getWrapper());
    }
}
