package com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ArrayElements.Type;

import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ArrayElements.ParameterArray;

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
