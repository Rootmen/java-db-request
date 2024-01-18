package ru.iedt.database.request.structures.nodes.v3;

import java.util.HashMap;

public class Parameter {
    protected final String defaultValue;
    protected final String parameterName;
    protected final String parameterType;
    protected final HashMap<String, String> whenMap = new HashMap<>();

    public Parameter(String defaultValue, String parameterName, String parameterType) {
        this.defaultValue = defaultValue;
        this.parameterName = parameterName;
        this.parameterType = parameterType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getParameterType() {
        return parameterType;
    }

    public HashMap<String, String> getWhenMap() {
        return whenMap;
    }

    @Override
    public String toString() {
        return String.format(
                "[parameterName=%s, parameterType=%s, defaultValue=%s, whenMap=%s]",
                parameterName, parameterType, defaultValue, whenMap);
    }
}
