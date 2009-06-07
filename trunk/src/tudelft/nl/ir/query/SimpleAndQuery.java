package tudelft.nl.ir.query;

import java.util.ArrayList;
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
	
	@Override
	public List<Document> evaluate(Index index) {
		this.m_Index = index;		
		List<Document> results = new ArrayList<Document>();
		List<Posting> postings = this.intersect();
		for (int i = 0; i < postings.size(); i++) {
			results.add((Document) postings.get(i).getDocument());
		}
		return results;
	}

	private List<Posting> intersect() {
		assert this.m_Terms.length == 2;
		
		Posting p1, p2;
		int id1, id2;
		List<Posting> result = new ArrayList<Posting>();
		List<Posting> termPostings1 = this.m_Index.getPostings(this.m_Terms[0]);
		List<Posting> termPostings2 = this.m_Index.getPostings(this.m_Terms[1]);

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
//				tmp = ((id1.length() > id2.length()) ? id2 : id1);							
//				for(l = 0; l < tmp.length(); l++){
//					if(id1.charAt(l) < id2.charAt(l)){
//						j++; break;
//					}else if(id1.charAt(l) > id2.charAt(l)){
//						k++; break;
//					}
//				}
			}else{
				k++;
			}
		}
		System.out.println("result size:"+result.size());
		return result;
		
//		for (int i = 0, j = 0, k = 0; i < this.m_Terms.length; i++) {
//			termPostings = this.m_Index.getPostings(this.m_Terms[i]);
//			if (termPostings != null && termPostings.size() > 0) {
//				if(result.size()==0){
//					// First time, just add everything.
//					result.addAll(termPostings);
//				}else{
//					j = 0;
//					k = 0;
//					
//					while((result.get(j) != null) && (termPostings.get(k) != null)){
//						p1 = result.get(j);
//						id1 = p1.getDocument().getID();
//						p2 = termPostings.get(k);
//						id2 = p2.getDocument().getID();
//						
//						if(id1 == id2){
//							result.add(p1);
//							j++; k++;				
//						}else if(id1 < id2){
//							j++;
////							tmp = ((id1.length() > id2.length()) ? id2 : id1);							
////							for(l = 0; l < tmp.length(); l++){
////								if(id1.charAt(l) < id2.charAt(l)){
////									j++; break;
////								}else if(id1.charAt(l) > id2.charAt(l)){
////									k++; break;
////								}
////							}
//						}else{
//							k++;
//						}
//					}
//				}			
//			}
//		}
//
//		return result;
	}
}
