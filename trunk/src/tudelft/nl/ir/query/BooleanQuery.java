package tudelft.nl.ir.query;

import java.util.List;

import tudelft.nl.ir.docs.Document;
import tudelft.nl.ir.index.Index;

public class BooleanQuery implements Query {

	BooleanQueryTree m_Tree;

	public BooleanQuery(BooleanQueryTree tree)
	{
		m_Tree = tree;
	}
	
	public List<Document> evaluate(Index index) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
