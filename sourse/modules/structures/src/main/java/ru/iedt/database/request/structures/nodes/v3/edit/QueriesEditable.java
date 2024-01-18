package ru.iedt.database.request.structures.nodes.v3.edit;

import ru.iedt.database.request.structures.nodes.v3.Queries;
import ru.iedt.database.request.structures.nodes.v3.SQL;

public class QueriesEditable extends Queries {


    public void addSql(SQL sql) {
        this.sql.add(sql);
    }

}
