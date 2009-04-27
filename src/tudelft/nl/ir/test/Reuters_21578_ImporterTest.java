package tudelft.nl.ir.test;

import java.io.File;

import tudelft.nl.ir.importer.DocImporter;
import tudelft.nl.ir.importer.Reuters21578_Importer;

public class Reuters_21578_ImporterTest {

	public static void main(String[] args)
	{
		DocImporter myImporter = new Reuters21578_Importer();
		myImporter.addDocumentsFromDirectory(new File("C:\\Dokumente und Einstellungen\\Philipp Cimiano\\Eigene Dateien\\Research\\DataSets\\reuters21578-xml"));
	}
}
