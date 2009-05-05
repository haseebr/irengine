package tudelft.nl.ir.importer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tudelft.nl.ir.docs.Document;
import tudelft.nl.ir.docs.DocumentImpl;
import tudelft.nl.ir.index.Index;
import tudelft.nl.ir.index.Posting;
import tudelft.nl.ir.preprocessing.Preprocessor;
import tudelft.nl.ir.preprocessing.Stemmer;
import tudelft.nl.ir.preprocessing.StopwordRemover;

public class DocImporterImpl implements DocImporter{
	
	int m_NumberDocs = 0;

	Index m_Index;

	Preprocessor m_Preprocessor;

	private HashMap<String, Posting> m_Preprocessors;

	private Stemmer m_Stemmer;

	private Object m_StopwordRemover;
	
	public DocImporterImpl()
	{
	}
	
	
	public void addDocumentFromFile(File file) {
		
		String term;
		
		m_NumberDocs++;
		
		String content = getContent(file);
		
		String tokens[] = content.split("\\s");
		
		String parts[] = file.toString().split("\\\\");
		
		Document doc = new DocumentImpl();
		
		doc.setID("id"+m_NumberDocs);
		
		doc.setURI(file.toString());
		
		doc.setTitle(parts[parts.length-1].replaceAll(".txt", ""));
		
		for (int i=0; i < tokens.length; i++)
		{
			term = tokens[i];
			
			Preprocessor preprocessor;
			
			for (int j=0; i < m_Preprocessors.size(); j++)
			{
				preprocessor = m_Preprocessors.get(j);
				term = preprocessor.process(term);
			}
				
			if (m_Stemmer != null) term = m_Stemmer.getStem(term); 
				
				
			
			if (this.m_StopwordRemover != null && !((StopwordRemover) m_StopwordRemover).isStopword(term)) 
				{
					doc.addTerm(term, i+1);
				}
		}
		
		if (m_Index != null) m_Index.addDocument(doc);
		
	}
	
	public void addDocumentsFromDirectory(File directory) {
		

		String[] children = directory.list();
		
		if (children != null)
		{
			for (int i=0; i<children.length; i++)
			{
				File child = new File (directory+"\\"+children[i]);
				
				if (child.isDirectory())
				{
					addDocumentsFromDirectory(child);
				}
				else
				{
					addDocumentFromFile(child);
				}
			}
		}
		
	}


	private String getContent(File file)
	{
		StringBuilder content = new StringBuilder();

		try 
		{
			BufferedReader input =  new BufferedReader(new FileReader(file));
		      try 
		      {
		        String line = null; //not declared within while loop
		       
		        while (( line = input.readLine()) != null)
		        {
		          content.append(line);
		          content.append(System.getProperty("line.separator"));
		        }
		      }
		      finally {
		        input.close();
		      }
		    }
		    catch (IOException ex){
		      ex.printStackTrace();
		    }
		    return content.toString();
	}


	public void addPreprocessor(Preprocessor preprocessor) {
		m_Preprocessors.add(preprocessor);
	}


	public void addStemmer(Stemmer stemmer) {
		m_Stemmer = stemmer;
	}


	public void addStopwordRemover(StopwordRemover remover) {
		m_StopwordRemover = remover;	
	}


	public void setIndex(Index index) {
		m_Index = index;
	}
	
}
