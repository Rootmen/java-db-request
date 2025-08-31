package ru.iedt.database.request.controller.v2;

import java.util.*;
import java.util.stream.Collectors;
import ru.iedt.database.request.controller.parameter.ParameterInput;

public class Utils {
    private static List<ParameterInput> addParameters(ParameterInput... parameterInput) {
        return new LinkedList<>(List.of(parameterInput));
    }

    private static <T> void addOptionalParameter(List<ParameterInput> parameters, String paramName, T value) {
        if (value != null) {
            parameters.add(new ParameterInput(paramName, value.toString()));
        }
    }

    private static <T> void addOptionalParameterArray(ArrayList<ParameterInput> parameter, String name, List<T> array) {
        if (array != null && !array.isEmpty()) {
            parameter.add(new ParameterInput(
                    name, array.stream().map(Object::toString).collect(Collectors.joining(","))));
        }
    }

    private static <T> void addOptionalParameterArray(ArrayList<ParameterInput> parameter, String name, T[] array) {
        if (array != null && array.length != 0) {
            parameter.add(new ParameterInput(
                    name, Arrays.stream(array).map(Object::toString).collect(Collectors.joining(","))));
        }
    }
}
