package ru.iedt.database.request.structures.nodes;

import ru.iedt.database.request.structures.base.Node;

public class JavaClassRunner extends Node {
    String className;

    public JavaClassRunner(String className) {
        super("javaclassrunner");
        this.className = className;
    }
}
