package tudelft.nl.ir.query;

import java.util.List;

import tudelft.nl.ir.docs.Document;
import tudelft.nl.ir.index.Index;

public interface Query {

	List<Document> evaluate(Index index);
	
}
