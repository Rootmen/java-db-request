package ru.iedt.database.request.structures.nodes.v3.edit;

import ru.iedt.database.request.structures.nodes.v3.Parameter;

import java.util.HashMap;

public class ParameterEditable extends Parameter {

    public ParameterEditable (String defaultValue, String parameterName, String parameterType) {
        super( defaultValue,  parameterName,  parameterType);
    }

    public void setWhenMap(HashMap<String, String> whenMap) {
        this.whenMap.clear();
        this.whenMap.putAll(whenMap);
    }
}
