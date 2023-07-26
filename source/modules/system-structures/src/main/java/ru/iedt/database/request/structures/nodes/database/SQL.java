package ru.iedt.database.request.structures.nodes.database;

public class SQL {

    private StringBuilder value;
    private String refid;

    public void setRefid(String refid) {
        this.refid = refid;
    }

    public void setValue(StringBuilder value) {
        this.value = value;
    }
}
