package com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ArrayElements.Type;

import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ArrayElements.ParameterArray;

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
