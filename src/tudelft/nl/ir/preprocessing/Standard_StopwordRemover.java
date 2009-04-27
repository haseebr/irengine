package tudelft.nl.ir.preprocessing;

import java.util.Set;

public class Standard_StopwordRemover {

	Set<String> m_Stopwords;
	
	public void loadStopwordsFromFile(String file) {
		
		// open the file and read the stopwords
		// assumes a one per line format
	}
	
	boolean isStopword(String term) {
		if (m_Stopwords.contains(term)) return true;
		else return false;
	}
	
}
