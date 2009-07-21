package tudelft.nl.ir.importer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.sun.org.apache.xerces.internal.impl.io.MalformedByteSequenceException;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import tudelft.nl.ir.docs.DocumentImpl;
import tudelft.nl.ir.index.Index;

public class Reuters21578_Importer implements DocImporter {
	/**
	 * Keeps track of the number of imported documents.
	 */
	private int m_docCount = 0;
	/**
	 * The Index that indexes and stores the document.
	 */
	private Index m_Index;

	/**
	 * Adds all documents that are in a Reuters xml file. The documents are
	 * stored in the database. At this moment only the documents BODY and TITLE
	 * are stored in the database.
	 * 
	 * @param File
	 *            file - Reuters file to parse and store.
	 */
	public void addDocumentFromFile(File file) {
		DOMParser parser = new DOMParser();
		String filepath = file.getAbsolutePath();
		try {
			parser.parse("file:" + filepath);
		} catch (MalformedByteSequenceException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Document doc = parser.getDocument();

		NodeList nodes = doc.getElementsByTagName("REUTERS");
		NodeList children, subchildren;
		Node child, subchild;
		String name, id;
		DocumentImpl document;

		System.out.println("There are " + nodes.getLength() + "  documents in " + file.getAbsolutePath());

		for (int i = 0, ln = nodes.getLength(); i < ln; i++) {
			// NEWID contains the document_id that's stored in the database.
			id = nodes.item(i).getAttributes().getNamedItem("NEWID").getNodeValue();
			document = new DocumentImpl(filepath, Integer.valueOf(id).intValue());
			
			children = nodes.item(i).getChildNodes();
			for (int j = 0; j < children.getLength(); j++) {
				child = children.item(j);
				name = child.getNodeName();

				if (name.equals("DATE")) {
					document.addMetaData("DATA", getValue(child));
				} else if (name.equals("TOPICS") || name.equals("PLACES") || name.equals("PEOPLE") || name.equals("ORGS") || name.equals("EXCHANGES") || name.equals("COMPANIES")) {
					document.addMetaData(name, this.getListOfValues(child));
				} else if (name.equals("TEXT")) {
					subchildren = child.getChildNodes();
					for (int k = 0; k < subchildren.getLength(); k++) {
						subchild = subchildren.item(k);
						name = subchild.getNodeName();
						if (name.equals("TITLE")) {
							document.setTitle(this.getValue(subchild));
						} else if (name.equals("DATELINE") || name.equals("BODY")) {
							document.addMetaData(name, this.getValue(subchild));
						}
					}
				}
			}

			this.m_Index.addDocument(document);
		}

		this.m_docCount += nodes.getLength();
	}

	/**
	 * Converts a node into an arraylist of the values of the child nodes of the
	 * node that's passed.
	 * 
	 * @param Node
	 *            node - Node to get the child node values from.
	 * @return ArrayList<String> An arraylist of child node values.
	 */
	private ArrayList<String> getListOfValues(Node node) {
		ArrayList<String> values = new ArrayList<String>();
		NodeList nodes = node.getChildNodes();

		for (int i = 0; i < nodes.getLength(); i++) {
			values.add(this.getValue(nodes.item(i)));
		}

		return values;
	}

	/**
	 * Returns the value of the first child of the node that's passed, or an
	 * empty string when the node doesn't contain any child nodes.
	 * 
	 * @param Node
	 *            node - Node to get the value from.
	 * @return The value of the first child of the node, or an empty String.
	 */
	private String getValue(Node node) {
		return node.getFirstChild() != null ? node.getFirstChild().getNodeValue() : "";
	}

	/**
	 * Function tries to parse all files in the directory that is passed. When
	 * there are any visible subfolders, these folders are also added. So the
	 * process is recursive.
	 * 
	 * @param File
	 *            directory - Directory to recursively add files from.
	 */
	public void addDocumentsFromDirectory(File directory) {
		String[] children = directory.list();

		if (children != null) {
			for (int i = 0, j = 0; i < children.length/* && j < 2 */; i++) {
				File child = new File(directory + "//" + children[i]);

				if (child.isDirectory() && !child.isHidden()) {
					// A visible folder, add its children.
					this.addDocumentsFromDirectory(child);
				} else {
					// A file, try to parse when the extension is .xml.
					System.out.println("Adding doc " + child.getAbsolutePath());
					String[] name = child.getName().split("\\.");
					System.out.println("Extension is " + name[name.length - 1]);

					if (name[name.length - 1].equals("xml")) {
						this.addDocumentFromFile(child);
						j++;
					}
				}
			}
		}
		
		System.out.print("There were " + this.m_docCount + " documents in " + directory.getName() + " folder.\n");
	}

	/**
	 * Sets the index that indexes the reuters corpse.
	 * 
	 * @param Index
	 *            index
	 */
	public void setIndex(Index index) {
		this.m_Index = index;
	}
}
