package tudelft.nl.ir.index;
import java.util.ArrayList;
import java.util.List;
import tudelft.nl.ir.docs.Document;
import tudelft.nl.ir.index.Posting;

public class InvertedPosting implements Posting{

	private Document m_Document;
	private List<Integer> m_Positions;
	
	public InvertedPosting(){
		this.m_Positions = new ArrayList<Integer>();
	}
	
	public InvertedPosting(Document document, ArrayList<Integer> positions){
		this.setDocument(document);
		this.m_Positions = positions;
	}
	public InvertedPosting(ArrayList<Integer> positions){
		//this.setDocument(document);
		this.m_Positions = positions;
	}
	
	private boolean invariant(){
		return true;
		//return (this.m_Positions != null && this.m_Document != null);
	}
	
	//@Override
	public Document getDocument() {
		assert invariant();
		return this.m_Document;
	}

	//@Override
	public void setDocument(Document document) {
		this.m_Document = document;		
	}

	public List<Integer> getPositions() {
		return m_Positions;
	}

	public void setPositions(List<Integer> positions) {
		m_Positions = positions;
	}

}
