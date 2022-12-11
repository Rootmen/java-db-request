package com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ArrayElements.Type;

import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ArrayElements.ParameterArray;

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
