package eu.interedition.collatex.matching;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.interedition.collatex.alignment.Alignment;
import eu.interedition.collatex.alignment.Match;
import eu.interedition.collatex.alignment.functions.Matcher;
import eu.interedition.collatex.input.Segment;
import eu.interedition.collatex.input.builders.WitnessBuilder;

// TODO: rename to Alignment Test!
public class MatchingTest {

  private WitnessBuilder builder;

  @Before
  public void setUp() {
    builder = new WitnessBuilder();
  }

  @Test
  // TODO: assert near matches separate?
  public void testNearMatch() {
    Segment a = builder.build("a near match");
    Segment b = builder.build("a nar match");
    Alignment alignment = Matcher.align(a, b);
    Set<Match> fixedMatches = alignment.getMatches();
    Assert.assertEquals("[(1->1), (2->2), (3->3)]", fixedMatches.toString());
  }

  @Test
  public void testNoPermutationsOnlyExactMatches() {
    Segment a = builder.build("deze zinnen zijn hetzelfde");
    Segment b = builder.build("deze zinnen zijn hetzelfde met een aanvulling");
    Alignment alignment = Matcher.align(a, b);
    Set<Match> matches = alignment.getMatches();
    String expected = "[(1->1), (2->2), (3->3), (4->4)]";
    Assert.assertEquals(expected, matches.toString());
  }

  @Test
  public void testSelectBestMatchFromPossibleMatches() {
    Segment a = builder.build("zijn hond liep aan zijn hand");
    Segment b = builder.build("op zijn pad liep zijn hond aan zijn hand");
    Alignment alignment = Matcher.align(a, b);
    Set<Match> matches = alignment.getMatches();
    String expected = "[(3->4), (4->7), (2->6), (6->9), (1->5), (5->8)]";
    Assert.assertEquals(expected, matches.toString());
    Assert.assertEquals(1, alignment.getGaps().size());
    Assert.assertEquals(3, alignment.getMatchSequences().size());
  }

  @Test
  public void testTreeTimesZijnAlsoWorks() {
    Segment a = builder.build("zijn hond liep aan zijn hand op zijn dag");
    Segment b = builder.build("op zijn pad liep zijn hond aan zijn hand op zijn dag");
    Alignment alignment = Matcher.align(a, b);
    Set<Match> matches = alignment.getMatches();
    String expected = "[(3->4), (4->7), (9->12), (2->6), (6->9), (1->5), (5->8), (7->10), (8->11)]";
    Assert.assertEquals(expected, matches.toString());
    Assert.assertEquals(1, alignment.getGaps().size());
    Assert.assertEquals(3, alignment.getMatchSequences().size());
  }

  @Test
  public void testMatchingFromBtoA() {
    Segment a = builder.build("op zijn pad liep zijn hond aan zijn hand");
    Segment b = builder.build("zijn hond liep aan zijn hand");
    Alignment alignment = Matcher.align(a, b);
    Set<Match> matches = alignment.getMatches();
    String expected = "[(4->3), (7->4), (6->2), (9->6), (5->1), (8->5)]";
    Assert.assertEquals(expected, matches.toString());
    Assert.assertEquals(1, alignment.getGaps().size());
    Assert.assertEquals(3, alignment.getMatchSequences().size());
  }

  // Note: in test there are no exact matches!
  // so there are multiple alignments that
  // are both equally valid!
  @Test
  public void testMatchingFromAtoBandBtoAMixed() {
    Segment a = builder.build("a a b");
    Segment b = builder.build("a b b");
    Alignment alignment = Matcher.align(a, b);
    Set<Match> matches = alignment.getMatches();
    String expected = "[(1->1), (3->3)]";
    Assert.assertEquals(expected, matches.toString());
  }

  @Test
  public void testMatchingFromAtoBandBtoAMixedCFixed() {
    Segment a = builder.build("a a c b");
    Segment b = builder.build("a b c b");
    Alignment alignment = Matcher.align(a, b);
    Set<Match> matches = alignment.getMatches();
    String expected = "[(3->3), (1->1), (4->4)]";
    Assert.assertEquals(expected, matches.toString());
  }

  @Test
  public void testAlignmentTreadExactMatchesAndNearMatchesEqually() {
    Segment a = builder.build("I bought this glass, because it matches those dinner plates.");
    Segment b = builder.build("I bought those glasses.");
    Alignment alignment = Matcher.align(a, b);
    Assert.assertEquals("[(1->1), (2->2), (3->3), (4->4)]", alignment.getMatches().toString());
  }

}
