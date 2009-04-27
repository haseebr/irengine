package tudelft.nl.ir.test;

import java.io.File;
import java.util.List;

import tudelft.nl.ir.docs.Document;
import tudelft.nl.ir.importer.DocImporter;
import tudelft.nl.ir.importer.DocImporterImpl;
import tudelft.nl.ir.index.TrivialIndex;

public class BooleanQueryTest {


	
	public static void main(String[] args)
	{
		DocImporter myImporter = new DocImporterImpl();
		
		TrivialIndex index = new TrivialIndex();
		
		Document doc;
		
		myImporter.addDocumentsFromDirectory(new File ("C:\\Dokumente und Einstellungen\\Philipp Cimiano\\Eigene Dateien\\Research\\DataSets\\IMDB"));
	
		
	
	}
	
}
