package ru.iedt.database.request.structures.nodes.java;

import ru.iedt.database.request.structures.base.Node;

public class JavaClassRunner extends Node {
    String className;

    public JavaClassRunner(String className) {
        super("JavaClassRunner","QuerySet");
        this.className = className;
    }
}
