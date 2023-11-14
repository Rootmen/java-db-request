package ru.iedt.database.request.structures.nodes.database;

public class Template {
    private StringBuilder value;
    private String id;

    public Template(StringBuilder value, String id) {
        this.value = value;
        this.id = id;
    }

    public void setId(String id) {
        if (id != null) id = id.trim();
        this.id = id;
    }

    public void setValue(String value) {
        this.value = new StringBuilder(value.trim());
    }

    public StringBuilder getValue() {
        return value;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("SQL{ refid='%s', value='%s' }", value.toString().replaceAll("\\s+", " ").replaceAll("\\n", " "), id);
    }
}
