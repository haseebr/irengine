package tudelft.nl.ir.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import tudelft.nl.ir.docs.Document;
import tudelft.nl.ir.preprocessing.Stemmer;


public class InvertedIndex implements Index {

	private HashMap<String,List<Posting>> m_Term2Postings;
	private List<Stemmer> m_Stemmers;
	
	public InvertedIndex(){
		m_Term2Postings = new HashMap<String,List<Posting>>();
		this.m_Stemmers = new ArrayList<Stemmer>();
	}
	
	public List<Posting> getPostings(String term) {

		if (m_Term2Postings.containsKey(term)) return (List<Posting>) m_Term2Postings.get(term);
		else return null;
	
	}

	public void addDocument(Document document) {
	
		Set<String> terms = document.getTerms();
		String term;
		
		for (Iterator<String> i = terms.iterator(); i.hasNext();){
			term = (String) i.next();
			this.addTerm(term,document);
		}		
	}

	private void addTerm(String term, Document document) {
		String stem;
		Posting p;
		ArrayList<Posting> postings;
		ArrayList<Integer> positions = (ArrayList<Integer>) document.getTermPositions(term);
		
		for(int i = 0; i < this.m_Stemmers.size(); i++){
			stem = this.m_Stemmers.get(i).getStem(term);
			p = new InvertedPosting(document, positions);
			
			if(this.m_Term2Postings.containsKey(stem)){
				postings = (ArrayList<Posting>)this.m_Term2Postings.get(stem);
				postings.add(p);
			}else{
				postings = new ArrayList<Posting>();
				postings.add(p);
				this.m_Term2Postings.put(stem, postings);
			}
		}
	}

	@Override
	public void addStemmer(Stemmer stemmer) {
		this.m_Stemmers.add(stemmer); 
	}
}
