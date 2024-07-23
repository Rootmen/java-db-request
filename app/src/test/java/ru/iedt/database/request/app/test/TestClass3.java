package ru.iedt.database.request.app.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.iedt.database.request.controller.entity.BaseEntity;

public class TestClass3 extends BaseEntity {

  final Integer param_1;
  final Integer param_2;
  final Integer param_3;

  @JsonCreator
  public TestClass3(
      @JsonProperty("param_1") Integer param_1,
      @JsonProperty("param_2") Integer param_2,
      @JsonProperty("param_3") Integer param_3) {
    this.param_1 = param_1;
    this.param_2 = param_2;
    this.param_3 = param_3;
  }
}
