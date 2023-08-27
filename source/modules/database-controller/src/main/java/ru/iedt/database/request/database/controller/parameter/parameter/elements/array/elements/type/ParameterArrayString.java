package ru.iedt.database.request.database.controller.parameter.parameter.elements.array.elements.type;

import ru.iedt.database.request.database.controller.parameter.exceptions.ParameterException;
import ru.iedt.database.request.database.controller.parameter.parameter.elements.array.elements.ParameterArray;

import java.util.ArrayList;

public class ParameterArrayString extends ParameterArray<ArrayList<String>> {

    public ParameterArrayString(String ID, String name, ArrayList<String> value) throws ParameterException {
        super(ID, name, value, "text");
    }

    @Override
    public ParameterException getExceptionError() {
        return null;
    }
}
