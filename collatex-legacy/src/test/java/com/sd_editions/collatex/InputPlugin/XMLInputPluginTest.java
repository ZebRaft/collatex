package com.sd_editions.collatex.InputPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;

import com.sd_editions.collatex.Block.Block;
import com.sd_editions.collatex.Block.BlockStructure;
import com.sd_editions.collatex.Block.BlockStructureCascadeException;
import com.sd_editions.collatex.Block.BlockStructureListIterator;

public class XMLInputPluginTest extends TestCase {
  public void test_simple_xml_input() throws FileNotFoundException, IOException, BlockStructureCascadeException {
    final IntInputPlugin plugin = new XMLInputPlugin(new File("examples/inputfiles/simple_xml.xml"));
    final BlockStructure document = plugin.readFile();
    assertEquals(16, document.getNumberOfBlocks());
    final BlockStructureListIterator<? extends Block> listIterator = document.listIterator();
    assertEquals("<l number=\"1\">", listIterator.next().toString());
    assertEquals("<w>The</w>", listIterator.next().toString());
    assertEquals("<w>prologe</w>", listIterator.next().toString());
    assertEquals("<w>of</w>", listIterator.next().toString());
    assertEquals("<w>the</w>", listIterator.next().toString());
    assertEquals("<w>Milleres</w>", listIterator.next().toString());
    assertEquals("<w>tale</w>", listIterator.next().toString());
    //    assertEquals("<l number=\"2\">", listIterator.next().toString());
    //    assertEquals("<w>Whan</w>", listIterator.next().toString());
  }

}