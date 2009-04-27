package tudelft.nl.ir.index;

import java.util.List;

import tudelft.nl.ir.docs.Document;

public interface Posting {
	
	// getters
	
	public Document getDocument();
	
	// settes
	
	public void setDocument(Document document);

}
