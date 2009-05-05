package tudelft.nl.ir.index;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import tudelft.nl.ir.docs.Document;


public class InvertedIndex implements Index {

	HashMap<String,List<Posting>> m_Term2Postings;
	
	public List<Posting> getPostings(String term) {

		if (m_Term2Postings.containsKey(term)) return (List<Posting>) m_Term2Postings.get(term);
		else return null;
	
	}

	public void addDocument(Document document) {
	
		Set<String> terms = document.getTerms();
		String term;
		
		for (Iterator<String> i = terms.iterator(); i.hasNext();){
			term = (String) i.next();
			addTerm(term,document);
		}		
	}

	private void addTerm(String term, Document document) {
		
		// add term to HashMap, with postingss
		
	}

}
