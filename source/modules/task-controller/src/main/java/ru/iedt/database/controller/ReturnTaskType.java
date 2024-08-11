package ru.iedt.database.controller;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public class ReturnTaskType<T> {

    private final int size;
    private final boolean isSingle;
    private final Uni<T> uni;
    private final Multi<T> multi;
    private final boolean isDeprecated;

    ReturnTaskType(Uni<T> uni, boolean isDeprecated) {
        isSingle = true;
        this.uni = uni;
        this.isDeprecated = isDeprecated;
        this.multi = null;
        this.size = 0;
    }

    ReturnTaskType(Integer size, Multi<T> multi, boolean isDeprecated) {
        isSingle = false;
        this.size = size;
        this.multi = multi;
        this.isDeprecated = isDeprecated;
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

    public boolean isDeprecated() {
        return isDeprecated;
    }

    public int getSize() {
        return size;
    }
}
