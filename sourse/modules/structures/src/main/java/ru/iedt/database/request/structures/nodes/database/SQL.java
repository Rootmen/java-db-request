package ru.iedt.database.request.structures.nodes.database;

public class SQL {

    private StringBuilder value;
    private String refId;

    public void setRefId(String refId) {
        if (refId != null) refId = refId.trim();
        this.refId = refId;
    }

    public void setValue(String value) {
        this.value = new StringBuilder(value.trim());
    }

    public StringBuilder getValue() {
        return value;
    }

    public String getRefId() {
        return refId;
    }

    @Override
    public String toString() {
        return String.format("SQL{ refid='%s', value='%s' }", value.toString().replaceAll("\\s+", " ").replaceAll("\\n", " "), refId);
    }
}
