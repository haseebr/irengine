package tudelft.nl.ir.index;

import tudelft.nl.ir.docs.Document;

public class PostingWithSkips implements Posting{

	Document m_Document;
	
	public Document getDocument() {
		return m_Document;
	}

	public void setDocument(Document document) {
		m_Document = document;
		
	}
}
