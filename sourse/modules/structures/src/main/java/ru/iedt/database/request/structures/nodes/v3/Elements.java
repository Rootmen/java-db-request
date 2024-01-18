package ru.iedt.database.request.structures.nodes.v3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Elements {

    interface Definition {
        Map<String, Elements.Template> getTemplate();

        Map<String, Elements.QuerySet> getQuerySet();

        @Override
        String toString();
    }

    interface QuerySet {
        String getRefid();

        List<Elements.Queries> getQueries();

        Map<String, Elements.Parameter> getParameters();

        @Override
        String toString();
    }

    interface Template {
        StringBuilder getValue();

        String getId();

        String toString();
    }

    interface SQL {
        StringBuilder getValue();

        String getName();

        String getWrapper();

        @Override
        String toString();
    }

    interface Parameter {
        String getDefaultValue();

        String getParameterName();

        String getParameterType();

        HashMap<String, String> getWhenMap();

        @Override
        String toString();
    }

    interface Queries {

        ArrayList<Elements.SQL> getSql();

        @Override
        String toString();
    }
}
