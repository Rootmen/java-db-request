package ru.iedt.database.request.database.controller.binder.error;

abstract public class BinderError extends Exception {
    abstract public String getError();
}
