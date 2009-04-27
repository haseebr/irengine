package tudelft.nl.ir.index;

import java.util.List;

import tudelft.nl.ir.docs.Document;

public interface Index {

	List<Posting> getPostings(String term);
	
	public void addDocument(Document document);
	
}
