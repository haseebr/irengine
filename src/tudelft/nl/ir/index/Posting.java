package tudelft.nl.ir.index;

import tudelft.nl.ir.docs.Document;

public interface Posting extends Comparable<Posting>{
	
	// getters
	
	public Document getDocument();
	
	// settes
	
	public void setDocument(Document document);
	public double getScore();
	public void setScore(double score);
}
