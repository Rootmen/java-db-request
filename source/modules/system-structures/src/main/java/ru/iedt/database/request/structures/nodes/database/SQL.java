package ru.iedt.database.request.structures.nodes.database;

public class SQL {

    private StringBuilder value;
    private String refid;

    public void setRefid(String refid) {
        this.refid = refid;
    }

    public void setValue(String value) {
        this.value = new StringBuilder(value.trim());
    }

    @Override
    public String toString() {
        return "SQL{" +
                "value='" + value + "'"+
                ", refid='" + refid + '\'' +
                '}';
    }
}
