package tudelft.nl.ir.test;

import java.io.File;

import tudelft.nl.ir.importer.DocImporter;
import tudelft.nl.ir.importer.Reuters21578_Importer;

public class Reuters_21578_ImporterTest {

	public static void main(String[] args){
		// http://modnlp.berlios.de/reuters21578.html
		DocImporter myImporter = new Reuters21578_Importer();
		myImporter.addDocumentsFromDirectory(new File("E:\\Eclipse\\irengine\\reuters21578-xml"));
	}
}
