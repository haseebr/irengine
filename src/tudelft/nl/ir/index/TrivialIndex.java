package tudelft.nl.ir.index;

import java.util.ArrayList;
import java.util.List;

import tudelft.nl.ir.docs.Document;

public class TrivialIndex implements Index{

	List<Document> documents;
	
	
	public List<Posting> getPostings(String term) {
		
		Document document;
		
		List<Posting> postings = new ArrayList<Posting>();
		
		for (int i=0; i < documents.size(); i++)
		{
			document = documents.get(i);
			
			if (document.contains(term)) postings.add(new PostingWithPosition(document,document.getTermPositions(term)));
		}
		
		return postings;
	}

	public void addDocument(Document document) {
		documents.add(document);
		
	}

}
