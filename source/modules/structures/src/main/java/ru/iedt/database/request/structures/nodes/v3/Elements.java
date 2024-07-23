package ru.iedt.database.request.structures.nodes.v3;

import io.vertx.mutiny.sqlclient.Tuple;
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

        Map<String, Elements.Parameter<?>> getParameters();

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

    interface Parameter<T> {
        T getDefaultValue();

        T getValue();

        void setValue(T value);

        void setValue(String value);

        String getParameterName();

        String getParameterType();

        Map<String, String> getWhenMap();

        @Override
        String toString();

        void addToTuple(Tuple tuple);
    }

    interface Queries {

        List<Elements.SQL> getSql();

        @Override
        String toString();
    }
}
