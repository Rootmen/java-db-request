package ru.iedt.database.request.structures.nodes.database;

public class Connection {
    String ref;
    String name;

    public Connection(String ref, String name) {
        this.ref = ref;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Connection{ refid='%s', name='%s' }", this.ref, this.name);
    }
}
