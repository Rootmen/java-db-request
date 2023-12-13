package ru.iedt.database.request.structures.nodes.v3;

import java.util.ArrayList;

public class Queries {
    ArrayList<SQL> sql = new ArrayList<>();

    public void addSql(SQL sql) {
        this.sql.add(sql);
    }

    public ArrayList<SQL> getSql() {
        return sql;
    }

    @Override
    public String toString() {

        return String.format("[sql=%s]", sql);
    }
}
