package ru.iedt.database.request.app.test.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@AllArgsConstructor
public class TestClassJsonInner {
    @JsonProperty("param_integer_one")
    Integer paramIntegerOne;

    @JsonProperty("param_integer_two")
    Integer paramIntegerTwo;

    @JsonProperty("param_integer3")
    Integer paramInteger3;
}
