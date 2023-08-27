package ru.iedt.database.request.database.controller.parameter;

public class ParameterInput {
    public String value;
    public String name;

    public ParameterInput(String _name, String _value) {
        this.value = _value;
        this.name = _name;
    }
}
