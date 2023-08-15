package ru.iedt.database.request.structures.nodes.database;

public class SQL {

    private StringBuilder value;
    private String refId;

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public void setValue(String value) {
        this.value = new StringBuilder(value.trim());
    }

    @Override
    public String toString() {
        return "SQL{" +
                "value='" + value + "'"+
                ", refid='" + refId + '\'' +
                '}';
    }
}
