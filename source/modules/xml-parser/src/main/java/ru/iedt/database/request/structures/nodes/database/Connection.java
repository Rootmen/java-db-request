package ru.iedt.database.request.structures.nodes.database;

public class Connection {
    String refId;

    public Connection(String refId) {
        this.refId = refId;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "refid='" + refId + '\'' +
                '}';
    }
}
