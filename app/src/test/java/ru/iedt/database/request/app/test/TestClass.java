package ru.iedt.database.request.app.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import ru.iedt.database.request.controller.entity.BaseEntity;
import ru.iedt.database.request.controller.entity.annotation.CreateConstructor;
import ru.iedt.database.request.controller.entity.annotation.CreateParameter;

public class TestClass extends BaseEntity {

  Integer param_1;
  Integer param_2;
  Integer param_3;
  String param_4;
  Integer[] param_5;
  Integer[] param_6;
  ArrayList<Integer> param_7;
  List<Integer> param_8;

  @CreateConstructor
  public TestClass(
      @CreateParameter("param_1") Integer param_1,
      @CreateParameter("param_2") Integer param_2,
      @CreateParameter("param_3") Integer param_3,
      @CreateParameter("param_4") String param_4,
      @CreateParameter("param_5") Integer[] param_5,
      @CreateParameter("param_5") Integer[] param_6,
      @CreateParameter("param_5") ArrayList<Integer> param_7,
      @CreateParameter("param_5") List<Integer> param_8) {
    this.param_1 = param_1;
    this.param_2 = param_2;
    this.param_3 = param_3;
    this.param_4 = param_4;
    this.param_5 = param_5;
    this.param_6 = param_6;
    this.param_7 = param_7;
    this.param_8 = param_8;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", TestClass.class.getSimpleName() + "[", "]")
        .add("param_1=" + param_1)
        .add("param_2=" + param_2)
        .add("param_3=" + param_3)
        .add("param_4='" + param_4 + "'")
        .add("param_5=" + Arrays.toString(param_5))
        .add("param_6=" + Arrays.toString(param_6))
        .add("param_7=" + param_7)
        .add("param_8=" + param_8)
        .toString();
  }
}
