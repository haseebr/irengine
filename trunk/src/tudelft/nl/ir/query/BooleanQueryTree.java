package tudelft.nl.ir.query;

import java.util.ArrayList;
import java.util.List;

import tudelft.nl.ir.docs.Document;
import tudelft.nl.ir.index.Index;
import tudelft.nl.ir.index.Posting;

public class BooleanQueryTree {

	QueryTreeNode m_Root;

	// setters
	public void setRoot(QueryTreeNode root)
	{
		m_Root = root;
	}
	
	
	// getters
	QueryTreeNode getRoot()
	{
		return m_Root;
	}
	
	List <Document> evaluate(Index index)
	{
		List<Document> documents = new ArrayList<Document>();
		
		Document document;
		
		List<Posting> postings;
		
		postings =  m_Root.evaluate(index);
		
		for (int i=0; i < postings.size(); i++)
		{
			document = (Document) postings.get(i);
			documents.add(document);
		}
		return documents;
	}
	
	public String toString()
	{
		return m_Root.toString();
	}
	
}
