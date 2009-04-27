package tudelft.nl.ir.query;

import java.util.List;

import tudelft.nl.ir.index.Index;
import tudelft.nl.ir.index.Posting;

public class Not_Node implements QueryTreeNode{

	QueryTreeNode m_Argument;
	
	public List<Posting> evaluate(Index index) {
		return m_Argument.evaluate(index);
	}
	
	// setters
	public void setArgument(QueryTreeNode arg)
	{
		m_Argument = arg;
	}
	
	
	// getters
	QueryTreeNode getArgument()
	{
		return m_Argument;
	}	
	
	public String toString()
	{
		return "NOT" + m_Argument+"\n"; 
	}
	
}
