package ru.iedt.database.request.app.test.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@AllArgsConstructor
public class TestClassJson {
    @JsonProperty("param_one")
    TestClassJsonInner paramOne;

    @JsonProperty("param_integer_two")
    ArrayList<TestClassJsonInner> paramTwo;
}
