package tudelft.nl.ir.query;

import java.util.ArrayList;
import java.util.List;

import tudelft.nl.ir.docs.Document;
import tudelft.nl.ir.index.Index;
import tudelft.nl.ir.index.Posting;

public class TermQuery implements Query {

	String[] m_Terms;

	public TermQuery(String query) {
		m_Terms = query.split("\\s");
	}

	public List<Document> evaluate(Index index) {
		List<Document> results = new ArrayList<Document>();
		List<Posting> postings = this.intersect(index);
		System.out.println(postings.size() + " postings found");
		for (int i = 0; i < postings.size(); i++) {
			results.add((Document) postings.get(i).getDocument());
		}

		return results;
	}

	private List<Posting> intersect(Index index) {
		assert this.m_Terms != null;

		List<Posting> postings = new ArrayList<Posting>();
		List<Posting> termPostings = new ArrayList<Posting>();
		for (int i = 0; i < this.m_Terms.length; i++) {
			termPostings = index.getPostings(this.m_Terms[i]);
			if (termPostings != null && termPostings.size() > 0) {
				postings.addAll(termPostings);
			}
		}

		return postings;
	}
}
