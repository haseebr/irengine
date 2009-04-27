package tudelft.nl.ir.query;

import java.util.List;

import tudelft.nl.ir.index.Index;
import tudelft.nl.ir.index.Posting;

public class TermNode implements QueryTreeNode{

	String m_Term;
	
	public TermNode(String term)
	{
		m_Term = term;
	}
	
	// getters
	public String getTerm()
	{
		return m_Term;
	}

	public List<Posting> evaluate(Index index) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString()
	{
		return m_Term;
	}
}
