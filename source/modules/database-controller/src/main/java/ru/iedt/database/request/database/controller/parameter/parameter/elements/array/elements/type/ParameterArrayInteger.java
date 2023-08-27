package ru.iedt.database.request.database.controller.parameter.parameter.elements.array.elements.type;

import ru.iedt.database.request.database.controller.parameter.exceptions.ParameterException;
import ru.iedt.database.request.database.controller.parameter.parameter.elements.array.elements.ParameterArray;

import java.util.ArrayList;

public class ParameterArrayInteger extends ParameterArray<ArrayList<Integer>> {

    public ParameterArrayInteger(String ID, String name, ArrayList<Integer> value) throws ParameterException {
        super(ID, name, value, "int");
    }

    @Override
    public ParameterException getExceptionError() {
        return null;
    }
}
