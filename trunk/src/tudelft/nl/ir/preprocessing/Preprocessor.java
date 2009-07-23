package tudelft.nl.ir.preprocessing;

import java.util.ArrayList;
import java.util.HashMap;

public interface Preprocessor {
	public HashMap<String, ArrayList<Integer>> process(String content);	
	public void addStemmer(Stemmer stemmer);	
	public void addStopwordRemover(StopwordRemover remover);	
	public void addTermProcessor(TermProcessor processor);
	public String processToken(String token);
	public String[] getTokens(String term);
	public String[] tokenizeText(String text);
}
