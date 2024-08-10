package ru.iedt.database.controller;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public class ReturnTaskType<T> {

    private final boolean isSingle;
    private final Uni<T> uni;
    private final Multi<T> multi;

    ReturnTaskType(Uni<T> uni) {
        isSingle = true;
        this.uni = uni;
        this.multi = null;
    }

    ReturnTaskType(Integer size, Multi<T> multi) {
        isSingle = false;
        this.multi = multi;
        this.uni = null;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public Uni<T> getUni() {
        return uni;
    }

    public Multi<T> getMulti() {
        return multi;
    }
}
