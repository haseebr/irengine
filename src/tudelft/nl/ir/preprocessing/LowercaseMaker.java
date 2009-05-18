package tudelft.nl.ir.preprocessing;

public class LowercaseMaker implements TermProcessor{
	public String process(String term) {		
		return term.toLowerCase();
	}
}
