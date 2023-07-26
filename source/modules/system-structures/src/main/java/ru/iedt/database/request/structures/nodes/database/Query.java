package ru.iedt.database.request.structures.nodes.database;

import ru.iedt.database.request.structures.base.Node;

import java.util.ArrayList;

public class Query extends Node {
    ArrayList<Parameter> parameters = new ArrayList<>();
    ArrayList<SQL> sql = new ArrayList<>();

    public Query() {
        super("Query", "QuerySet");
    }

    public void addParameters(Parameter parameter) {
        this.parameters.add(parameter);
    }

    public void addSql(SQL sql) {
        this.sql.add(sql);
    }

    @Override
    public String toString() {
        return "Query{" +
                "\n\t\tparameters=" + parameters +
                ",\n\t\t text=" + sql +
                '}';
    }
}
