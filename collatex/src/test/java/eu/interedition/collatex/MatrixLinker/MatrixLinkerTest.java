package eu.interedition.collatex.MatrixLinker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import eu.interedition.collatex.AbstractTest;
import eu.interedition.collatex.Token;
import eu.interedition.collatex.graph.VariantGraph;
import eu.interedition.collatex.graph.VariantGraphVertex;
import eu.interedition.collatex.input.SimpleWitness;
import eu.interedition.collatex.matching.EqualityTokenComparator;

public class MatrixLinkerTest extends AbstractTest {

  @Ignore
	@Test
  public void test1() {
  	SimpleWitness[] sw = createWitnesses("A B C A B","A B C A B");
    int baseWitness = 0;
		int posBaseWitness = 0;
  	int secondWitness = 1;
  	int posSecondWitness = 0;
  	
		VariantGraph vg = collate(sw[posBaseWitness]);
  	MatrixLinker linker = new MatrixLinker();
  	Map<Token, VariantGraphVertex> link = linker.link(vg,sw[1],new EqualityTokenComparator());

		compareWitnesses(sw, baseWitness, posBaseWitness, secondWitness,
        posSecondWitness, link);
  }
  
  @Test
  public void testBuildMatrix() {
  	SimpleWitness[] sw = createWitnesses("A B C A B","A B C A B");
		VariantGraph vg = collate(sw[0]);
  	MatrixLinker linker = new MatrixLinker();
  	SparseMatrix buildMatrix = linker.buildMatrix(vg,sw[1],new EqualityTokenComparator());
  	assertTrue(buildMatrix.at(0, 0));
  	assertTrue(buildMatrix.at(1, 1));
  	assertTrue(buildMatrix.at(1, 4));
  	assertTrue(buildMatrix.at(2, 2));
  	assertTrue(buildMatrix.at(0, 3));
  	assertTrue(buildMatrix.at(3, 0));
  	assertTrue(buildMatrix.at(3, 3));
  	assertTrue(buildMatrix.at(4, 1));
  	assertTrue(buildMatrix.at(4, 4));
//  	System.out.println(buildMatrix.toHtml());
  }
  
  @Test
  public void testAsymmatricMatrix() {
  	SimpleWitness[] sw = createWitnesses("A B A B C","A B C A B");
		VariantGraph vg = collate(sw[0]);
  	MatrixLinker linker = new MatrixLinker();
  	SparseMatrix buildMatrix = linker.buildMatrix(vg,sw[1],new EqualityTokenComparator());
  	assertTrue(buildMatrix.at(4, 2));
  }
  
  @Ignore
  @Test
  public void testHermansText1() {
  	String textD1 = "Op den Atlantischen Oceaan voer een groote stoomer, de lucht was helder blauw, het water rimpelend satijn.";
  	String textD9 = "Over de Atlantische Oceaan voer een grote stomer. De lucht was helder blauw, het water rimpelend satijn.<p/>";
  	SimpleWitness[] sw = createWitnesses(textD1,textD9);
		VariantGraph vg = collate(sw[0]);
  	MatrixLinker linker = new MatrixLinker();
  	SparseMatrix buildMatrix = linker.buildMatrix(vg,sw[1],new EqualityTokenComparator());
  	System.out.println(buildMatrix.toHtml());
  }
  
  @Ignore
  @Test
  public void testHermansText2() {
  	String textD1 = "Op den Atlantischen Oceaan voer een groote stoomer. Onder de velen aan boojrd bevond zich een bruine, korte dikke man. <i> JSg </i> werd nooit zonder sigaar gezien. Zijn pantalon had lijnrechte vouwen in de pijpen, maar zat toch altijd vol rimpels. <b> De </b> pantalon werd naar boven toe breed, ontzaggelijk breed; hij omsloot den buik van den kleinen man als een soort balcon.";
  	String textD9 = "Op de Atlantische Oceaan voer een ontzaggelijk zeekasteel. Onder de vele passagiers aan boord, bevond zich een bruine, korte dikke man. Hij werd nooit zonder sigaar gezien. Zijn pantalon had lijnrechte vouwen in de pijpen, maar zat toch altijd vol rimpels. De pantalon werd naar boven toe breed, ongelofelijk breed: hij omsloot de buik van de kleine man als een soort balkon.";
  	SimpleWitness[] sw = createWitnesses(textD1,textD9);
		VariantGraph vg = collate(sw[0]);
  	MatrixLinker linker = new MatrixLinker();
  	SparseMatrix buildMatrix = linker.buildMatrix(vg,sw[1],new EqualityTokenComparator());
  	System.out.println(buildMatrix.toHtml());
  	Archipelago archipelago = buildMatrix.getIslands();
  	assertEquals(42, archipelago.size());
    assertEquals(98,archipelago.numOfConflicts());

  }
  
  @Ignore
  @Test
  public void testHermansText3() {
  	String textMZ_DJ233 = "Werumeus Buning maakt artikelen van vijf pagina&APO+s over de geologie van de diepzee, die hij uit Engelse boeken overschrijft, wat hij pas in de laatste regel vermeldt, omdat hij zo goed kan koken.<p/>\n" +
"J. W. Hofstra kan niet lezen en nauwelijks stotteren, laat staan schrijven. Hij oefent het ambt van litterair criticus uit omdat hij uiterlijk veel weg heeft van een Duitse filmacteur (Adolf Wohlbr�ck).<p/>\n" +
"Zo nu en dan koopt Elsevier een artikel van een echte professor wiens naam en titels z� vet worden afgedrukt, dat zij allicht de andere copie ook iets professoraals geven, in het oog van de speksnijders.<p/>\n" +
"Edouard Bouquin is het olijke culturele geweten. Bouquin betekent: 1) oud boek van geringe waarde, 2) oude bok, 3) mannetjeskonijn. Ik kan het ook niet helpen, het staat in Larousse.<p/>\n" +
"De politiek van dit blad wordt geschreven door een der leeuwen uit het Nederlandse wapen (ik geloof de rechtse) op een krakerige gerechtszaaltoon in zeer korte zinnetjes, omdat hij tot zijn spijt de syntaxis onvoldoende beheerst.<p/>\n";
  	String textD4F ="Werumeus  Buning maakt artikelen van vijf pagina&APO+s  over de  geologie van de  diepzee, die  hij uit Engelse  boeken overschrijft,   wat hij  pas in de laatste  regel  vermeldt,   omdat hij   zo  goed kan koken.<p/>\n"+
"J. W.Hofstra kan niet lezen en nauwelijks stotteren,   laat staan schrijven.   Hij  oefent het ambt van literair kritikus uit omdat hij uiterlijk veel weg heeft van een Duitse filmacteur (Adolf Wohlbr�ck).<p/>\n" +
"Edouard  Bouquin is  het olijke  culturele  geweten.   Bouquin betekent:   1)  oud boek  van geringe  waarde,   2)  oude bok,   3)  mannetjeskonijn.   Ik kan het ook niet helpen,   het staat in Larousse.<p/>\n" +
"Nu en dan koopt Elsevier een artikel van een echte professor, wiens naam en titels z� vet worden afgedrukt, dat zij allicht de andere copie ook iets professoraals geven, in het oog van de speksnijders.<p/>\n" +
"\n" +
"De politiek van dit blad  wordt geschreven door een der leeuwen uit het nederlandse wapen (ik geloof de   rechtse)  op een krakerige  gerechtszaaltoon in zeer korte  zinnetjes, omdat hij  tot zijn  spijt  de  syntaxis  onvoldoende  beheerst. <p/>";
  	SimpleWitness[] sw = createWitnesses(textMZ_DJ233,textD4F);
		VariantGraph vg = collate(sw[0]);
  	MatrixLinker linker = new MatrixLinker();
  	SparseMatrix buildMatrix = linker.buildMatrix(vg,sw[1],new EqualityTokenComparator());
  	try {
	    FileWriter fw = new FileWriter("C:\\Documents and Settings\\meindert\\Mijn Documenten\\Project Hermans productielijn\\Materiaal input collateX\\Hulp1.html");
	    fw.write(buildMatrix.toHtml());
    } catch (IOException e) {
	    e.printStackTrace();
    }
  	System.out.println(buildMatrix.toHtml());
  }
  
  @Test
  public void testRowLabels() {
  	String textD1 = "de het een";
  	String textD9 = "het een de";
  	SimpleWitness[] sw = createWitnesses(textD1,textD9);
		VariantGraph vg = collate(sw[0]);
  	MatrixLinker linker = new MatrixLinker();
  	SparseMatrix buildMatrix = linker.buildMatrix(vg,sw[1],new EqualityTokenComparator());
    ArrayList<String> labels = buildMatrix.rowLabels();
    assertEquals("de",labels.get(0));
    assertEquals("het",labels.get(1));
    assertEquals("een",labels.get(2));
  }
  
  @Test
  public void testColumnLabels() {
  	String textD1 = "de het een";
  	String textD9 = "het een de";
  	SimpleWitness[] sw = createWitnesses(textD1,textD9);
		VariantGraph vg = collate(sw[0]);
  	MatrixLinker linker = new MatrixLinker();
  	SparseMatrix buildMatrix = linker.buildMatrix(vg,sw[1],new EqualityTokenComparator());
    ArrayList<String> labels = buildMatrix.columnLabels();
    assertEquals("het",labels.get(0));
    assertEquals("een",labels.get(1));
    assertEquals("de",labels.get(2));
  }
  
  @Test
  public void testAllTrues() {
  	SimpleWitness[] sw = createWitnesses("A B A B C","A B C A B");
		VariantGraph vg = collate(sw[0]);
  	MatrixLinker linker = new MatrixLinker();
  	SparseMatrix buildMatrix = linker.buildMatrix(vg,sw[1],new EqualityTokenComparator());
  	ArrayList<Coordinate> allTrue = buildMatrix.allTrues();
  	assertEquals(9,allTrue.size());
  	assertTrue(allTrue.contains(new Coordinate(0,0)));
  	assertFalse(allTrue.contains(new Coordinate(1,0)));
  }
  
  @Test
  public void testCoordinates() {
  	Coordinate a = new Coordinate(0,0);
  	Coordinate b = new Coordinate(0,0);
  	Coordinate c = new Coordinate(1,1);
  	assertEquals(new Coordinate(0,0),a);
  	assertEquals(b, a);
  	assertFalse(a.equals(c));
  }
  
  @Test
  public void testBorders() {
  	Coordinate a = new Coordinate(0,0);
  	Coordinate b = new Coordinate(1,1);
  	Coordinate c = new Coordinate(2,1);
  	assertTrue(a.borders(b));
  	assertFalse(a.borders(c));
  	assertFalse(b.borders(c));
  }
  
  @Test
  public void testUndirectedIsland() {
  	Island isl = new Island();
  	isl.add(new Coordinate(0, 0));
  	assertEquals(1,isl.size());
  	isl.add(new Coordinate(0, 0));
  	assertEquals(1,isl.size());
  	isl.add(new Coordinate(0, 1));
  	assertEquals(1,isl.size());
  	isl.add(new Coordinate(2, 2));
  	assertEquals(1,isl.size());
  	assertTrue(isl.neighbour(new Coordinate(1,1)));
  	isl.add(new Coordinate(1, 1));
  	assertEquals(2,isl.size());
}
  
  @Test
  public void testDirectedIsland() {
  	Island isl = new Island();
  	isl.add(new Coordinate(0, 0));
  	assertEquals(1,isl.size());
  	assertEquals(0,isl.direction());
  	isl.add(new Coordinate(1, 1));
  	assertEquals(2,isl.size());
  	assertEquals(1,isl.direction());
  	isl.add(new Coordinate(2, 2));
  	assertEquals(3,isl.size());
  	assertEquals(1,isl.direction());
  }
  
  @Test
  public void testArchipelago() {
  	Archipelago arch = new Archipelago();
  	Island isl_1 = new Island();
  	isl_1.add(new Coordinate(0,0));
  	isl_1.add(new Coordinate(1,1));
  	arch.add(isl_1);
  	Island isl_2 = new Island();
  	isl_2.add(new Coordinate(2,2));
  	isl_2.add(new Coordinate(3,3));
  	arch.add(isl_2);
  	assertEquals(2, arch.size());
  	assertTrue(isl_1.overlap(isl_2));
  	arch.mergeIslands();
  	assertEquals(1, arch.size());
  }

  @Test
  public void testArchipelagoRivalIslands() {
  	SimpleWitness[] sw = createWitnesses("A B C A B","A B C A B");
		VariantGraph vg = collate(sw[0]);
  	MatrixLinker linker = new MatrixLinker();
  	SparseMatrix buildMatrix = linker.buildMatrix(vg,sw[1],new EqualityTokenComparator());
  	Archipelago archipelago = buildMatrix.getIslands();
    assertEquals(3,archipelago.size());
    assertEquals(2,archipelago.numOfConflicts());
  }

  @Test
  public void testIslands() {
  	SimpleWitness[] sw = createWitnesses("A B C A B","A B C A B");
		VariantGraph vg = collate(sw[0]);
  	MatrixLinker linker = new MatrixLinker();
  	SparseMatrix buildMatrix = linker.buildMatrix(vg,sw[1],new EqualityTokenComparator());
  	ArchipelagoWithVersions islands = buildMatrix.getIslands();
//  	System.out.println("islands: "+islands);
  	assertEquals(3, islands.size());
  	assertEquals(2,islands.numOfConflicts());
  	islands.createNonConflictingVersions();
//		System.out.println("version(0): "+islands.getVersion(0));
//  	System.out.println("version(1): "+islands.getVersion(1));
//  	System.out.println("version(2): "+islands.getVersion(2));
  	int res = islands.numOfNonConflConstell();
  	assertEquals(2, res);
  	assertEquals(1, islands.getVersion(0).size());
//		System.out.println("version(0): "+islands.getVersion(0));
//  	System.out.println("version(1): "+islands.getVersion(1));
  	assertEquals(3, islands.getVersion(1).size());
  	assertEquals(null, islands.getVersion(2));
  }

  @Test
  public void testRemovePoints() {
  	Island di_1 = new Island();
  	di_1.add(new Coordinate(1, 1));
  	di_1.add(new Coordinate(2, 2));
  	Island di_2 = new Island();
  	di_2.add(new Coordinate(2, 2));
  	Island di_3 = di_1.removePoints(di_2);
  	assertEquals(2, di_1.size());
  	assertEquals(1, di_3.size());
  }
  
  @Test
  public void testFindCoorOnRolOrCol() {
  	Island isl_1 = new Island();
  	isl_1.add(new Coordinate(0,0));
  	isl_1.add(new Coordinate(1,1));
    assertEquals(new Coordinate(0,0),isl_1.getCoorOnRow(0));  	
    assertEquals(new Coordinate(1,1),isl_1.getCoorOnCol(1));  	
    assertEquals(null,isl_1.getCoorOnCol(4));  	
  }
  
  @Test
  public void testOrderedIslands() {
  	Archipelago arch = new Archipelago();
  	Island isl_1 = new Island();
  	isl_1.add(new Coordinate(1,1));
  	isl_1.add(new Coordinate(2,2));
  	isl_1.add(new Coordinate(3,3));
  	arch.add(isl_1);
  	Island isl_2 = new Island();
  	isl_2.add(new Coordinate(5,5));
  	isl_2.add(new Coordinate(6,6));
  	arch.add(isl_2);
  	Island isl_3 = new Island();
  	isl_3.add(new Coordinate(8,8));
  	isl_3.add(new Coordinate(9,9));
  	isl_3.add(new Coordinate(10,10));
  	isl_3.add(new Coordinate(11,11));
  	arch.add(isl_3);
  	assertEquals(4,arch.get(0).size());
  	assertEquals(3,arch.get(1).size());
  	assertEquals(2,arch.get(2).size());
  }

  @Test
  public void testOrderedIslands2() {
  	ArchipelagoWithVersions arch = new ArchipelagoWithVersions();
  	Island isl_1 = new Island();
  	isl_1.add(new Coordinate(1,1));
  	isl_1.add(new Coordinate(2,2));
  	isl_1.add(new Coordinate(3,3));
  	arch.add(isl_1);
  	Island isl_2 = new Island();
  	isl_2.add(new Coordinate(3,5));
  	isl_2.add(new Coordinate(4,6));
  	arch.add(isl_2);
  	arch.createNonConflictingVersions();
//  	System.out.println("versions: ");
//  	for(Archipelago arch_v : arch.getNonConflVersions()) {
//  		System.out.println(arch_v);
//  	}
  	assertEquals(2,arch.getNonConflVersions().size());
  	Island isl_3 = new Island();
  	isl_3.add(new Coordinate(6,8));
  	isl_3.add(new Coordinate(7,9));
  	isl_3.add(new Coordinate(8,10));
  	isl_3.add(new Coordinate(9,11));
  	arch.add(isl_3);
  	arch.createNonConflictingVersions();
  	assertEquals(28,arch.versionsOrderedByValue().get(0).value());
  	assertEquals(27,arch.versionsOrderedByValue().get(1).value());
  }
  
  @Test
  public void testNonConflictingVersions() {
  	ArchipelagoWithVersions arch = new ArchipelagoWithVersions();
  	Island isl_1 = new Island();
  	isl_1.add(new Coordinate(1,1));
  	isl_1.add(new Coordinate(2,2));
  	isl_1.add(new Coordinate(3,3));
  	arch.add(isl_1);
  	arch.createNonConflictingVersions();
  	assertEquals(1,arch.numOfNonConflConstell());
  }
  
  @Test
  public void testIslandValue() {
  	Island isl_1 = new Island();
  	isl_1.add(new Coordinate(1,1));
  	assertEquals(1,isl_1.value());
  	isl_1.add(new Coordinate(2,2));
  	assertEquals(5,isl_1.value());
  	isl_1.add(new Coordinate(3,3));
  	assertEquals(10,isl_1.value());
  	Island isl_2 = new Island();
  	isl_2.add(new Coordinate(2,2));
  	isl_2.add(new Coordinate(3,1));
  	assertEquals(3,isl_2.value());
  	Archipelago arch = new Archipelago();
  	arch.add(isl_1);
  	arch.add(isl_2);
  	assertEquals(13,arch.value());
  }
  
	private void compareWitnesses(SimpleWitness[] sw, int baseWitness,
      int posBaseWitness, int secondWitness, int posSecondWitness,
      Map<Token, VariantGraphVertex> link) {
	  List<Token> lt = sw[secondWitness].getTokens();
    VariantGraphVertex variantGraphVertex = link.get(lt.get(posSecondWitness));
    
    Set<Token> tokens = variantGraphVertex.tokens();
    Token next = tokens.iterator().next();
		assertEquals(sw[baseWitness].getTokens().get(posBaseWitness),next);
  }
  
 
}
