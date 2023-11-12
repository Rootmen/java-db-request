package ru.iedt.database.request.structures.nodes.database.temporary.table;

import ru.iedt.database.request.structures.base.Node;

import java.util.ArrayList;

public class TemporaryTable extends Node {

    String id;
    ArrayList<TemporaryTableColumns> columns;
    TemporaryTableType type;

    public TemporaryTable(String id, TemporaryTableType type) {
        super("TemporaryTable");
        this.id = id;
        this.type = type;
    }
}
