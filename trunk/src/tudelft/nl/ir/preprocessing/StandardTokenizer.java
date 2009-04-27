package tudelft.nl.ir.preprocessing;

public class StandardTokenizer implements Tokenizer{

	public String[] tokenize(String text) {

		return text.split("\\s");
		
	}

	
	
}
