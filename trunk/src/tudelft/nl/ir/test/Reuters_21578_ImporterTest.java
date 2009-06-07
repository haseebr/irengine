package tudelft.nl.ir.test;

import java.util.ArrayList;
import tudelft.nl.ir.docs.Document;
import tudelft.nl.ir.importer.DocImporter;
import tudelft.nl.ir.importer.Reuters21578_Importer;
import tudelft.nl.ir.index.Index;
import tudelft.nl.ir.index.InvertedIndex;
import tudelft.nl.ir.query.SimpleAndQuery;

public class Reuters_21578_ImporterTest {

	public static void main(String[] args){
		// http://modnlp.berlios.de/reuters21578.html
		DocImporter imp = new Reuters21578_Importer();
		
		Index index = new InvertedIndex();		
		imp.setIndex(index);
		//imp.addDocumentsFromDirectory(new File("reuters21578-xml"));
		
		SimpleAndQuery t = new SimpleAndQuery("Matrix technology");
		ArrayList<Document> results = (ArrayList<Document>) t.evaluate(index);
		
		for(int i = 0; i < results.size(); i++ ){
			System.out.println(results.get(i).getID() + ": "+results.get(i).getTitle());
		}
	}
}
