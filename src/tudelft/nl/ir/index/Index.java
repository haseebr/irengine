package tudelft.nl.ir.index;

import java.util.List;

import tudelft.nl.ir.docs.Document;
import tudelft.nl.ir.preprocessing.Stemmer;

public interface Index {

	List<Posting> getPostings(String term);
	
	public void addDocument(Document document);
	public void addStemmer(Stemmer stemmer);
	
}
