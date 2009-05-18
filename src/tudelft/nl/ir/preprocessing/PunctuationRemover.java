package tudelft.nl.ir.preprocessing;

public class PunctuationRemover implements TermProcessor{
	public String process(String term) {	
		return term.replaceAll("(,|\\.|:|\\'|\\\")", "");
	}
}
