package ru.iedt.database.request.controller.parameter;

public class ParameterInput {
    private final String value;
    private final String name;

    public ParameterInput(String name, String value) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
