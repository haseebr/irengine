package tudelft.nl.ir.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import tudelft.nl.ir.docs.Document;
import tudelft.nl.ir.docs.DocumentImpl;
import tudelft.nl.ir.preprocessing.Preprocessor;
import tudelft.nl.ir.preprocessing.Reuters21578Preprocessor;
import tudelft.nl.ir.storage.DBStorage;

public class InvertedIndex implements Index {
	private DBStorage m_Storage;
	private Preprocessor m_Preprocessor;

	/**
	 * Index constructor.
	 */
	public InvertedIndex() {
		this.m_Storage = new DBStorage();
		this.m_Preprocessor = new Reuters21578Preprocessor();
	}

	/**
	 * Returns a list of postings that point to the given term.
	 * 
	 * @param String
	 *            term - Term to look for.
	 * @return List<Posting> An ArrayList with postings of a normalized term.
	 */
	public List<Posting> getPostings(String term) {
		/**
		 * Normalize term.
		 */
		term = this.m_Preprocessor.processToken(term);
		
		/**
		 * Returns the posting for the normalized term.
		 */
		return this.m_Storage.getPostings(term);
	}

	/**
	 * Function adds a document by adding the terms one by one.
	 * 
	 * @param Document
	 *            document Document instance with terms.
	 */
	public void addDocument(Document document) {
		HashMap<String, ArrayList<Integer>> map = this.m_Preprocessor.process(document.getContent());
		/**
		 * Store the document.
		 */
		this.m_Storage.addDocument(document);

		/**
		 * Store the terms.
		 */
		this.m_Storage.addMap(map, document);
	}

	public DocumentImpl getDocument(String document_id) {
		return this.m_Storage.getDocument(document_id);
	}

	public Preprocessor getPreprocessor() {
		return m_Preprocessor;
	}

	public void setPreprocessor(Preprocessor mPreprocessor) {
		m_Preprocessor = mPreprocessor;
	}

}
