package com.rootmen.Database.DatabaseQuery.Query.Binder.Error.Types;

import com.rootmen.Database.DatabaseQuery.Query.Binder.Error.BinderError;

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
