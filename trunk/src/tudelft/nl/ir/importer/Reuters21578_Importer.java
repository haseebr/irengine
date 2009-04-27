package tudelft.nl.ir.importer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

import tudelft.nl.ir.docs.DocumentImpl;
import tudelft.nl.ir.index.Index;
import tudelft.nl.ir.preprocessing.Preprocessor;
import tudelft.nl.ir.preprocessing.Stemmer;
import tudelft.nl.ir.preprocessing.StopwordRemover;

public class Reuters21578_Importer implements DocImporter{

	int m_docCount=0;
	
	public void addDocumentFromFile(File file) {
		DOMParser parser = new DOMParser();
        try {
			parser.parse("file:"+file.getAbsolutePath());
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        Document doc = parser.getDocument();
		
        NodeList nodes = doc.getElementsByTagName("REUTERS");
        NodeList children;
        NodeList subchildren;
        Node node;
        Node child;
        Node subchild;
        String name;
        String value;
        
        
        System.out.println("There are " + nodes.getLength() + 
        "  documents in "+file.getAbsolutePath());
        
        for (int i=0; i < nodes.getLength(); i++)
        {   
        	DocumentImpl document = new DocumentImpl();
        	
        	node = nodes.item(i);
        	children = node.getChildNodes();
        	
        	for (int j=0; j < children.getLength(); j++)
        	{
        		child = children.item(j);
        	
        		name = child.getNodeName();
        		value = child.getNodeValue();
        		
        		// System.out.print("Name: "+name+"/Value:"+value+"\n");
        		
        		if (name.equals("DATE")) document.addMetaData("DATA", getValue(child));
        		
        		if (name.equals("TOPICS")) document.addMetaData("TOPICS", getListOfValues(child));
        		
        		if (name.equals("PLACES")) document.addMetaData("PLACES", getListOfValues(child));
        		
        		if (name.equals("PEOPLE")) document.addMetaData("PEOPLE", getListOfValues(child));

        		if (name.equals("ORGS")) document.addMetaData("ORGS", getListOfValues(child));
        		
        		if (name.equals("EXCHANGES")) document.addMetaData("EXCHANGES", getListOfValues(child));
        		
        		if (name.equals("COMPANIES")) document.addMetaData("COMPANIES", getListOfValues(child));

        		
        		if (name.equals("TEXT"))
        		{
        			subchildren = child.getChildNodes();
        			for (int k=0; k < subchildren.getLength(); k++)
                	{
        				subchild = subchildren.item(k);
        				
        				name = subchild.getNodeName();
                		value = subchild.getNodeValue();
                		
                		// System.out.print("\t\tName: "+name+"/Value:"+value+"\n");
                		
                		if (name.equals("TITLE")) document.setTitle(getValue(subchild));
                		
                		if (name.equals("DATELINE")) document.addMetaData("DATELINE", getValue(subchild));
                		
                		if (name.equals("BODY")) document.addMetaData("BODY", getValue(subchild));
                		
                		
                	}
        		}
        	}
        	
        	System.out.print(document);
        	
        }
                
        m_docCount+=nodes.getLength();
        
	}

	private List<String> getListOfValues(Node node) {
		
		List<String> values = new ArrayList<String>();
		
		NodeList nodes;
		Node child;
		
		
		nodes = node.getChildNodes();
		
		 for (int i=0; i < nodes.getLength(); i++)
	     {
			 child = nodes.item(i);
			 
			 values.add(getValue(child));
	     }
	     
		
		return values;
		
	}

	private String getValue(Node node) {
		
		Node child;
		
		// String name = node.getNodeName();
		// String value = node.getNodeValue();
		
		child = node.getFirstChild();
		
		return child.getNodeValue();
		
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
					System.out.println("Adding doc "+child.getAbsolutePath());
					
					String[] name = child.getName().split("\\.");
				
					System.out.println("Extension is "+name[name.length-1]);
					
					// extension is "sgm"
					
					if (name[name.length-1].equals("xml"))
					{
						addDocumentFromFile(child);
					}
				}
			}
		}
		
		System.out.print("There are indeed "+this.m_docCount+" documents in the Reuters 21578 collection\n");
		
	}

	public void addPreprocessor(Preprocessor preprocessor) {
		// TODO Auto-generated method stub
		
	}

	public void addStemmer(Stemmer stemmer) {
		// TODO Auto-generated method stub
		
	}

	public void addStopwordRemover(StopwordRemover remover) {
		// TODO Auto-generated method stub
		
	}

	public void setIndex(Index index) {
		// TODO Auto-generated method stub
		
	}

}
