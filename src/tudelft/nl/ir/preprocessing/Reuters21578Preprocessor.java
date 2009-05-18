package tudelft.nl.ir.preprocessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import tudelft.nl.ir.index.Posting;

public class Reuters21578Preprocessor implements Preprocessor{
	
	// List that'll contain all stemmers for this preprocessor.
	private List<Stemmer> m_Stemmers = new ArrayList<Stemmer>();
	// List that'll contain all stopwordremovers for this preprocessor.
	private List<StopwordRemover> m_StopwordRemovers = new ArrayList<StopwordRemover>();
	// List that'll contain all stopwordremovers for this preprocessor.
	private List<TermProcessor> m_TermProcessor = new ArrayList<TermProcessor>();
	private Tokenizer m_Tokenizer = new StandardTokenizer();
	
	public Reuters21578Preprocessor(){
		this.addStemmer(new PorterStemmer());
		this.addStopwordRemover(new MultiLingualStopwordRemover());
		this.addTermProcessor(new PunctuationRemover());
		this.addTermProcessor(new LowercaseMaker());
	}
	/**
	 * Adds a stemmer to the list of the stemmers.
	 * @param Stemmer stemmer
	 */
	public void addStemmer(Stemmer stemmer) {
		this.m_Stemmers.add(stemmer);
	}
	/**
	 * Adds a stopword remover to the list of the preprocessor.
	 * @param StopwordRemover remover
	 */
	public void addStopwordRemover(StopwordRemover remover) {
		this.m_StopwordRemovers.add(remover);
	}
	/**
	 * Adds a termprocessor to the list of termprocessors.
	 * @param TermProcessor processor
	 */
	public void addTermProcessor(TermProcessor processor) {
		this.m_TermProcessor.add(processor);		
	}
	public HashMap<String, ArrayList<Integer>> process(String content){
		HashMap<String, ArrayList<Integer>> result = new HashMap<String, ArrayList<Integer>>();
		String[] tokens = this.m_Tokenizer.tokenize(content);
		String term;
		ArrayList<Integer> positions;
		
		for(int i = 0, ln = tokens.length; i < ln; i++){
			term = this.processToken(tokens[i]);
			if(term == null) continue;
			
			if (result.containsKey(term)) {
				positions = (ArrayList<Integer>) result.get(term);
				positions.add(i);
			} else {
				positions = new ArrayList<Integer>();
				positions.add(i);
				result.put(term, positions);
			}
		}
		return result;
	}
	public String processToken(String token){
		if(token == null || token.length() == 0)
			return null;
		
		for(int i = 0; i < this.m_TermProcessor.size(); i++){
			token = this.m_TermProcessor.get(i).process(token);
		}
		
		for(int j = 0; (token == null || token.length() == 0) && (j < this.m_StopwordRemovers.size()); j++){
			if(this.m_StopwordRemovers.get(j).isStopword(token)){
				return null;
			}
		}
		
		for(int k = 0; (token == null || token.length() == 0) && (k < this.m_Stemmers.size()); k++){
			token = this.m_Stemmers.get(k).getStem(token);
		}
		
		return token;
	}
}
