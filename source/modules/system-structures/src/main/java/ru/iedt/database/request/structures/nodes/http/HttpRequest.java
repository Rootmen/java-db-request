package ru.iedt.database.request.structures.nodes.http;

import ru.iedt.database.request.structures.base.Node;

public class HttpRequest extends Node {
    String url;
    String body;
    String requestType;

    public HttpRequest(String url, String body, String requestType) {
        super("HttpRequest", "Query");
        this.url = url;
        this.body = body;
        this.requestType = requestType;
    }
}
