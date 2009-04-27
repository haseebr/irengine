package tudelft.nl.ir.preprocessing;

import java.util.HashMap;

import tudelft.nl.ir.index.Posting;

public Preprocessor {

	HashMap<String,Posting> process(String content);
	
	public void addStemmer(Stemmer stemmer);
	
	public void addStopwordRemover(StopwordRemover remover);
	
	public void addTermProcessor(TermProcessor processor);
	
}
