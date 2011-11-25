package eu.interedition.collatex.web.io;

import com.google.common.collect.Iterables;
import eu.interedition.collatex.implementation.output.AlignmentTable;
import eu.interedition.collatex.implementation.output.Column;
import eu.interedition.collatex.interfaces.*;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;

/**
 * @author <a href="http://gregor.middell.net/" title="Homepage">Gregor Middell</a>
 */
public class AlignmentTableSerializer extends JsonSerializer<AlignmentTable> {

  @Override
  public Class<AlignmentTable> handledType() {
    return AlignmentTable.class;
  }

  @Override
  public void serialize(AlignmentTable value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
    final SortedSet<IWitness> witnesses = value.getWitnesses();
    final List<Column> columns = value.getColumns();

    jgen.writeStartObject();

    jgen.writeNumberField("rows", Iterables.size(value.getRows()));
    jgen.writeNumberField("columns", Iterables.size(columns));

    jgen.writeArrayFieldStart("sigils");
    for (IWitness witness : witnesses) {
      jgen.writeString(witness.getSigil());
    }
    jgen.writeEndArray();


    jgen.writeArrayFieldStart("variance");
    for (Column column : columns) {
      jgen.writeNumber(column.getState().ordinal());
    }
    jgen.writeEndArray();

    jgen.writeArrayFieldStart("table");
    for (Column column : columns) {
      jgen.writeStartArray();
      for (IWitness witness : witnesses) {
        if (column.containsWitness(witness)) {
          provider.defaultSerializeValue(column.getToken(witness), jgen);
        } else {
          jgen.writeNull();
        }

      }
      jgen.writeEndArray();
    }
    jgen.writeEndArray();

    jgen.writeEndObject();
  }
}
