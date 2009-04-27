package tudelft.nl.ir.query;

import java.util.List;

import tudelft.nl.ir.index.Index;
import tudelft.nl.ir.index.Posting;

public class Or_Node implements QueryTreeNode{

	QueryTreeNode m_Left;
	QueryTreeNode m_Right;
	

	public List<Posting> evaluate(Index index) {
		List<Posting> leftPostings = m_Left.evaluate(index);
		List<Posting> rightPostings = m_Right.evaluate(index);
		
		if (m_Left.getClass().equals(""))
			return PostingEvaluator.complement(rightPostings,leftPostings);
		
		if (m_Right.getClass().equals(""))
			return PostingEvaluator.complement(leftPostings,rightPostings);
		
		
		return PostingEvaluator.merge(leftPostings,rightPostings);
	}
	
	// setters
	public void setLeft(QueryTreeNode left)
	{
		m_Left = left;
	}
	
	public void setRight(QueryTreeNode right)
	{
		m_Right = right;
	}
	
	
	// getters
	QueryTreeNode getLeft()
	{
		return m_Left;
	}
	
	QueryTreeNode getRight()
	{
		return m_Right;
	}
	
	public String toString()
	{
		return "OR( \n" + m_Left +"\n" + m_Right+")";
	}

}
