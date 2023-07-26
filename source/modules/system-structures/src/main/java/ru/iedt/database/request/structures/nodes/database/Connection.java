package ru.iedt.database.request.structures.nodes.database;

public class Connection {
    String refid;

    public Connection(String refid) {
        this.refid = refid;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "refid='" + refid + '\'' +
                '}';
    }
}
