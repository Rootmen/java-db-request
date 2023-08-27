package ru.iedt.database.request.database.controller.parameter.parameter.elements.array.elements.type;

import ru.iedt.database.request.database.controller.parameter.exceptions.ParameterException;
import ru.iedt.database.request.database.controller.parameter.parameter.elements.array.elements.ParameterArray;

import java.math.BigInteger;
import java.util.ArrayList;

public class ParameterArrayBigInteger extends ParameterArray<ArrayList<BigInteger>> {

    public ParameterArrayBigInteger(String ID, String name, ArrayList<BigInteger> value) throws ParameterException {
        super(ID, name, value, "BIGINT");
        this.arrayType = "BIGINT";
    }

    @Override
    public ParameterException getExceptionError() {
        return null;
    }
}
