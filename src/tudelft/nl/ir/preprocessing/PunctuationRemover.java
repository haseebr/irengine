package tudelft.nl.ir.preprocessing;

public class PunctuationRemover implements TermProcessor{
	/**
	 * Function removes punctuation from the given term and returns the result.
	 */
	public String process(String term) {	
		return term.replaceAll("(;|-|\\+|,|&|\\(|\\)|\\.|:|\\'|\\\")", "");
	}
}
