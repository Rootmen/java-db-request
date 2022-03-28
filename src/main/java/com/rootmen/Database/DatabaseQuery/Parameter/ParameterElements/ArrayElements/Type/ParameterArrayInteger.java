package com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ArrayElements.Type;

import com.rootmen.Database.DatabaseQuery.Parameter.Exceptions.ParameterException;
import com.rootmen.Database.DatabaseQuery.Parameter.ParameterElements.ArrayElements.ParameterArray;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class ParameterArrayInteger extends ParameterArray<ArrayList<Integer>> {

    public ParameterArrayInteger(String ID, String name, ArrayList<Integer> value, String arrayType) throws ParameterException {
        super(ID, name, value, arrayType);
    }

    @Override
    public void addParameterToStatement(PreparedStatement statement, int index, Connection connection) throws SQLException {
        Array array = connection.createArrayOf(this.arrayType, this.currentValue.toArray());
        statement.setArray(index, array);
    }

    @Override
    public ParameterException getExceptionError() {
        return null;
    }
}
