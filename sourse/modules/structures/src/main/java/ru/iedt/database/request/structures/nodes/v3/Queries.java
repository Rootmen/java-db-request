package ru.iedt.database.request.structures.nodes.v3;

import java.util.ArrayList;

public class Queries {
    protected final ArrayList<SQL> sql = new ArrayList<>();

    public ArrayList<SQL> getSql() {
        return sql;
    }

    @Override
    public String toString() {
        return String.format("[sql=%s]", sql);
    }
}
