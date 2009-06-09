package tudelft.nl.ir.test;

import java.io.File;
import tudelft.nl.ir.importer.DocImporter;
import tudelft.nl.ir.importer.DocImporterImpl;
import tudelft.nl.ir.index.TrivialIndex;
import tudelft.nl.ir.query.And_Node;
import tudelft.nl.ir.query.BooleanQuery;
import tudelft.nl.ir.query.BooleanQueryTree;
import tudelft.nl.ir.query.TermNode;

public class TrivialIndexTest {
	
	public static void main(String[] args)
	{
		
		DocImporter myImporter = new DocImporterImpl();
		
		myImporter.addDocumentsFromDirectory(new File ("C:\\Dokumente und Einstellungen\\Philipp Cimiano\\Eigene Dateien\\Research\\DataSets\\IMDB"));
	
		TrivialIndex index = new TrivialIndex();
	
		myImporter.setIndex(index);
		
		BooleanQueryTree queryTree = new BooleanQueryTree();
		
		BooleanQuery query = new BooleanQuery(queryTree);
		
		And_Node node = new And_Node();
		
		queryTree.setRoot(node);
		
		node.setLeft(new TermNode("swedish"));
		node.setRight(new TermNode("story"));
		
		query.evaluate(index);
		
	}
}
