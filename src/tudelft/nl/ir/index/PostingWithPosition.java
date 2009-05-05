package tudelft.nl.ir.index;

import java.util.ArrayList;
import java.util.List;

import tudelft.nl.ir.docs.Document;

public class PostingWithPosition implements Posting{

	Document m_Document;
	
	List<Integer> m_Positions;

	public PostingWithPosition()
	{
		m_Positions = new ArrayList<Integer>();
	}
	
	public PostingWithPosition(Document document)
	{
		m_Positions = new ArrayList<Integer>();
		m_Document = document;
	}
	
	public PostingWithPosition(Document document, List<Integer> positions)
	{
		m_Document = document;
		m_Positions = positions;
	}
	
	public Document getDocument() {
		return m_Document;
	}

	public void setDocument(Document document) {
		m_Document = document;		
	}	
}
