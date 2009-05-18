package tudelft.nl.ir.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tudelft.nl.ir.docs.Document;
import tudelft.nl.ir.preprocessing.Preprocessor;
import tudelft.nl.ir.preprocessing.Reuters21578Preprocessor;
import tudelft.nl.ir.preprocessing.Stemmer;
import tudelft.nl.ir.storage.DBStorage;

public class InvertedIndex implements Index {

	private HashMap<String, List<Posting>> m_Term2Postings;
	private List<Stemmer> m_Stemmers;
	private DBStorage m_Storage;
	private Preprocessor m_Preprocessor;

	public InvertedIndex() {
		this.m_Term2Postings = new HashMap<String, List<Posting>>();
		this.m_Stemmers = new ArrayList<Stemmer>();
		this.m_Storage = new DBStorage();
		this.m_Preprocessor = new Reuters21578Preprocessor();
	}

	/**
	 * Returns a list of postings that point to the given term.
	 * 
	 * @param String
	 *            term Term to look for.
	 * @return List<Posting> An ArrayList with postings of a normalized term.
	 */
	public List<Posting> getPostings(String term) {		
		ArrayList<Posting> result = new ArrayList<Posting>();
		term = this.m_Preprocessor.processToken(term);
		
		
		return result;
	}

	/**
	 * Function adds a document by adding the terms one by one.
	 * 
	 * @param Document
	 *            document Document instance with terms.
	 */
	public void addDocument(Document document) {
		HashMap<String, ArrayList<Integer>> map = this.m_Preprocessor.process(document.getContent());
		for( Map.Entry<String, ArrayList<Integer>> entry : map.entrySet()){
			this.m_Storage.addTerm(entry.getKey(), document, entry.getValue());
		}
		// Set<String> terms = document.getTerms();
		// for (Iterator<String> i = terms.iterator(); i.hasNext();) {
		// this.addTerm((String) i.next(), document);
		// }
	}

	/**
	 * Function that adds a term to the index. The term will first be normalized
	 * by stemmers, and parsers etc before it's added to the index.
	 * 
	 * @todo, not store the document with each posting, but only its id.
	 * @param String
	 *            term
	 * @param Document
	 *            document
	 */
	private void addTerm(String term, Document document) {
		String stem;
		// Posting p;
		// ArrayList<Posting> postings;
		ArrayList<Integer> positions = (ArrayList<Integer>) document
				.getTermPositions(term);

		for (int i = 0; i < this.m_Stemmers.size(); i++) {
			stem = this.m_Stemmers.get(i).getStem(term);
			if (stem != null)
				this.m_Storage.addTerm(stem, document, positions);
			// p = new InvertedPosting(document, positions);
			//			
			// if (this.m_Term2Postings.containsKey(stem)) {
			// postings = (ArrayList<Posting>) this.m_Term2Postings.get(stem);
			// postings.add(p);
			// } else {
			// postings = new ArrayList<Posting>();
			// postings.add(p);
			// this.m_Term2Postings.put(stem, postings);
			// }
		}
	}

	@Override
	public void addStemmer(Stemmer stemmer) {
		this.m_Stemmers.add(stemmer);
	}
}
