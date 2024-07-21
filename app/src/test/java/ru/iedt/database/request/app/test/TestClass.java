package ru.iedt.database.request.app.test;

import java.util.ArrayList;
import java.util.List;
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
      @CreateParameter(column_name = "param_1") Integer param_1,
      @CreateParameter(column_name = "param_2") Integer param_2,
      @CreateParameter(column_name = "param_3") Integer param_3,
      @CreateParameter(column_name = "param_4") String param_4,
      @CreateParameter(column_name = "param_5") Integer[] param_5,
      @CreateParameter(column_name = "param_5") Integer[] param_6,
      @CreateParameter(column_name = "param_5") ArrayList<Integer> param_7,
      @CreateParameter(column_name = "param_5") ArrayList<Integer> param_8) {
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
    return "TestClass{"
        + "param_1="
        + param_1
        + ", param_2="
        + param_2
        + ", param_3="
        + param_3
        + ", param_4='"
        + param_4
        + '\''
        + '}';
  }
}
