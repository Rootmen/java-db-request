package ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.array.type;

import static ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterFactory.generateArray;

import io.vertx.mutiny.sqlclient.Tuple;
import java.util.ArrayList;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.ParameterTypes;
import ru.iedt.database.request.structures.nodes.v3.node.parameter.elements.ParameterAbstract;

public class ParameterArrayInteger extends ParameterAbstract<ArrayList<Integer>> {
  public ParameterArrayInteger(ArrayList<Integer> defaultValue, String parameterName) {
    super(defaultValue, parameterName, ParameterTypes.INTEGER_ARRAY);
  }

  @Override
  public void setValue(String value) {
    this.currentValue = generateArray(value, Integer.class);
  }

  @Override
  public void addToTuple(Tuple tuple) {
    ArrayList<Integer> arrayList = this.getValue();
    if (arrayList == null) {
      tuple.addArrayOfInteger(null);
      return;
    }
    tuple.addArrayOfInteger(arrayList.toArray(Integer[]::new));
  }
}
