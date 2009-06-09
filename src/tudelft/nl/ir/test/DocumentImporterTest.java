package tudelft.nl.ir.test;

import java.io.File;
import tudelft.nl.ir.importer.DocImporter;
import tudelft.nl.ir.importer.DocImporterImpl;

public class DocumentImporterTest {


	
	public static void main(String[] args)
	{
		DocImporter myImporter = new DocImporterImpl();
		
		myImporter.addDocumentsFromDirectory(new File ("C:\\Dokumente und Einstellungen\\Philipp Cimiano\\Eigene Dateien\\Research\\DataSets\\IMDB"));
	
		
	
	}
	
}
