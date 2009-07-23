package tudelft.nl.ir.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import tudelft.nl.ir.docs.Document;
import tudelft.nl.ir.index.Index;
import tudelft.nl.ir.index.Posting;

public class SimpleAndQuery implements Query{

	String[] m_Terms;
	Index m_Index;
	public SimpleAndQuery(String query) {
		m_Terms = query.split("\\s");
	}
	
	//@Override
	public List<Document> evaluate(Index index) {
		this.m_Index = index;		
		List<Document> results = new ArrayList<Document>();
		List<Posting> postings = this.intersect();

		Collections.sort(postings);
		
		Document doc = null;
		for (int i = 0; i < postings.size(); i++) {
			doc = postings.get(i).getDocument();
			results.add(doc);
		}
		return results;
	}
	
	private List<Posting> intersect(List<Posting> termPostings1, List<Posting> termPostings2){
		int id1, id2;
		List<Posting> result = new ArrayList<Posting>();
		Posting p1, p2;
		int j = 0, k = 0;
		while((j<termPostings1.size()) && (k<termPostings2.size())){
			p1 = termPostings1.get(j);
			id1 = p1.getDocument().getID();
			p2 = termPostings2.get(k);
			id2 = p2.getDocument().getID();
			if(id1 == id2){
				result.add(p1);
				j++; k++;				
			}else if(id1 < id2){
				j++;
			}else{
				k++;
			}
		}
		//System.out.println("result size:"+result.size());
		return result;
	}
	
	private List<Posting> intersect(){
		List<Posting> termPostings;
		List<Posting> result = new ArrayList<Posting>();
		for (int i = 0; i < this.m_Terms.length; i++) {
			termPostings = this.m_Index.getPostings(this.m_Terms[i]);

			if (termPostings != null && termPostings.size() > 0) {
				if(result.size()==0){
					// First time, just add everything.
					result.addAll(termPostings);
				}else{
					result = this.intersect(result, termPostings);
				}
			}
		}
		return result;		
	}
}