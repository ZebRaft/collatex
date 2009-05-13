package com.sd_editions.collatex.output;

import com.sd_editions.collatex.permutations.Witness;
import com.sd_editions.collatex.permutations.Word;

public abstract class Column {

  public abstract void toXML(StringBuilder builder);

  public abstract Word getWord(Witness witness);

  public abstract void addMatch(Witness witness, Word word);

}