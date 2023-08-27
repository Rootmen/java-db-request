package ru.iedt.database.request.database.controller.binder.error.types;


import ru.iedt.database.request.database.controller.binder.error.BinderError;

public class ExceptionDuplicateAnnotation extends BinderError {
    String parameterName;
    String className;

    public ExceptionDuplicateAnnotation(String parameterName, String className) {
        this.parameterName = parameterName;
        this.className = className;
    }

    @Override
    public String getError() {
        return "Duplicate annotation in class " + this.className + ", parameter " + parameterName;
    }
}
