package tudelft.nl.ir.query;

import java.util.List;

import tudelft.nl.ir.index.Index;
import tudelft.nl.ir.index.Posting;

public interface QueryTreeNode {
	
	public List<Posting> evaluate(Index index);
	
	public String toString();
}
