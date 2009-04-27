package tudelft.nl.ir.query;

import java.util.ArrayList;
import java.util.List;

import tudelft.nl.ir.docs.Document;
import tudelft.nl.ir.index.Index;
import tudelft.nl.ir.index.Posting;

public class TermQuery implements Query{

	String[] m_Terms;
	
	public TermQuery(String query)
	{
		m_Terms = query.split("\\s");
	}
	
	public List<Document> evaluate(Index index) {
	
		List<Document> results = new ArrayList<Document>();
		
		List<Posting> postingList = intersect(index);
		
		return results;
		
	}

	private List<Posting> intersect(Index index) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
