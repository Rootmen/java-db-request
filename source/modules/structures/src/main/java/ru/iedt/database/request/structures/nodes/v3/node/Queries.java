package ru.iedt.database.request.structures.nodes.v3.node;

import java.util.ArrayList;
import java.util.List;
import ru.iedt.database.request.structures.nodes.v3.Elements;

public class Queries implements Elements.Queries {

    protected final ArrayList<Elements.SQL> sql = new ArrayList<>();

    public void addSql(Elements.SQL sql) {
        this.sql.add(sql);
    }

    public List<Elements.SQL> getSql() {
        return new ArrayList<>(sql);
    }

    @Override
    public String toString() {
        return String.format("[sql=%s]", sql);
    }
}
