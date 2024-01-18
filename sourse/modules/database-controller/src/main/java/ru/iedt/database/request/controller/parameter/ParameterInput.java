package ru.iedt.database.request.controller.parameter;

public class ParameterInput {
    public String value;
    public String name;

    public ParameterInput(String name, String value) {
        this.value = value;
        this.name = name;
    }
}
