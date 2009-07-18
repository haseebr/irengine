package tudelft.nl.ir.importer;

import java.io.File;
import tudelft.nl.ir.index.Index;

public interface DocImporter {
	public void addDocumentFromFile(File file);	
	public void addDocumentsFromDirectory(File directory);	
	public void setIndex(Index index);	
}
