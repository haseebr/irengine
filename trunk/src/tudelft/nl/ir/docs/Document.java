package tudelft.nl.ir.docs;

import java.util.List;
import java.util.Set;

public interface Document {

	// setters
	
	void setID(int id);
	
	void setTitle(String string);
	
	void setURI(String string);

	void setFilePath(String filePath);
	
	// getters

	String getFilePath();
	
	int getID();
	
	String getContent();
	
	String getContent(int start, int end);
	
	Set<String> getTerms();
	
	String getTitle();
	
	List<Integer> getTermPositions(String term);
	
	
	boolean contains(String term);
	
	void addTerm(String term, int position);
}
