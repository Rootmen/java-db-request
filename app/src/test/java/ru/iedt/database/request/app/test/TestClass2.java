package ru.iedt.database.request.app.test;

import java.util.ArrayList;
import java.util.StringJoiner;
import ru.iedt.database.request.controller.entity.BaseEntity;
import ru.iedt.database.request.controller.entity.annotation.CreateConstructor;
import ru.iedt.database.request.controller.entity.annotation.CreateParameter;

public class TestClass2 extends BaseEntity {

    TestClass3 param_1;
    ArrayList<TestClass3> param_2;

    @CreateConstructor
    public TestClass2(
            @CreateParameter(value = "param_1", is_json_mapping = true) TestClass3 param_1,
            @CreateParameter(value = "param_2", is_json_mapping = true) ArrayList<TestClass3> param_2) {
        this.param_1 = param_1;
        this.param_2 = param_2;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TestClass2.class.getSimpleName() + "[", "]")
                .add("param_1=" + param_1)
                .add("param_2=" + param_2)
                .toString();
    }
}
