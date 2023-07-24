package ru.iedt.database.request.structures.base;

public class Node {
    protected String nodeName;
    protected String parentName;

    public Node(String nodeName, String parentName) {

        this.nodeName = nodeName;
        this.parentName = parentName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getParentName() {
        return parentName;
    }
}
