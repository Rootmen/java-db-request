package ru.iedt.database.request.structures.nodes.v3;

public class SQL {

    private final StringBuilder value;
    private final String name;
    private final String wrapper;

    public SQL(StringBuilder value, String name, String wrapper) {
        this.value = value;
        this.name = name;
        this.wrapper = wrapper;
    }

    public StringBuilder getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getWrapper() {
        return wrapper;
    }

    @Override
    public String toString() {
        return String.format(
                "[name=%s, wrapper=%s, value=%s]",
                name, wrapper, value.toString().replaceAll("\\s+", " ").replaceAll("\\n", " "));
    }
}
