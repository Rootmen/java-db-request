package ru.iedt.database.request.structures.nodes.v3.node;

import ru.iedt.database.request.structures.nodes.v3.Elements;

import java.util.ArrayList;

public class Queries implements Elements.Queries {

    protected final ArrayList<Elements.SQL> sql = new ArrayList<>();

    public void addSql(Elements.SQL sql) {
        this.sql.add(sql);
    }

    public ArrayList<Elements.SQL> getSql() {
        return sql;
    }

    @Override
    public String toString() {
        return String.format("[sql=%s]", sql);
    }

}
