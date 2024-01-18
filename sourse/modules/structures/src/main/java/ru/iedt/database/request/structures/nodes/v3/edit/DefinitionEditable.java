package ru.iedt.database.request.structures.nodes.v3.edit;

import ru.iedt.database.request.structures.nodes.v3.Definition;
import ru.iedt.database.request.structures.nodes.v3.QuerySet;
import ru.iedt.database.request.structures.nodes.v3.Template;
import java.util.Map;

public class DefinitionEditable  extends Definition {

    public final void setQuerySetArrayList(Map<String, QuerySet> querySetMap) {
        this.querySet.clear();
        this.querySet.putAll(querySetMap);
    }

    public void setSqlArrayList(Map<String, Template> templateMap) {
        this.template.clear();
        this.template.putAll(templateMap);
    }

}
