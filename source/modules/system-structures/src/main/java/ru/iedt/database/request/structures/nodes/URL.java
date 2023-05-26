package ru.iedt.database.request.structures.nodes;

import ru.iedt.database.request.structures.base.Node;

public class URL extends Node {
    String url;
    String body;
    String requestType;

    public URL(String url, String body, String requestType) {
        super("url");
        this.url = url;
        this.body = body;
        this.requestType = requestType;
    }
}
