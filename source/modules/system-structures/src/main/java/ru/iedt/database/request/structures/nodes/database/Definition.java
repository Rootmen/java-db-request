package ru.iedt.database.request.structures.nodes.database;

import java.util.ArrayList;

public class Definition {
    ArrayList<SQL> sqlArrayList = new ArrayList<>();
    ArrayList<QuerySet> querySetArrayList = new ArrayList<>();

    public void setQuerySetArrayList(ArrayList<QuerySet> querySetArrayList) {
        this.querySetArrayList = querySetArrayList;
    }

    public void setSqlArrayList(ArrayList<SQL> sqlArrayList) {
        this.sqlArrayList = sqlArrayList;
    }

    @Override
    public String toString() {
        return "Definition {" +
                "\nSQL=" + sqlArrayList +
                ",\nQuerySet=" + querySetArrayList +
                '}';
    }
}
