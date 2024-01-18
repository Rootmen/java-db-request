package ru.iedt.database.request.structures.nodes.v3.node;


import ru.iedt.database.request.structures.nodes.v3.Elements;

public class Template implements Elements.Template {

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
        return String.format("[id=%s, value=%s]", this.getId(), this.getValue().toString().trim());
    }

}
