package ru.iedt.database.request.app.test.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@AllArgsConstructor
public class TestClass {
    @JsonProperty("param_integer_one")
    Integer paramIntegerOne;

    @JsonProperty("param_integer_two")
    Integer paramIntegerTwo;

    @JsonProperty("param_integer3")
    Integer paramInteger3;

    @JsonProperty("param4")
    String param4;

    @JsonProperty("param5")
    Integer[] param5;

    @JsonProperty("param6")
    Integer[] param6;

    @JsonProperty("param7")
    ArrayList<Integer> param7;

    @JsonProperty("param8")
    List<Integer> param8;
}
