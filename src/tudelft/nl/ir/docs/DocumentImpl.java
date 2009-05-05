package tudelft.nl.ir.docs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DocumentImpl implements Document {

	HashMap<String, List<Integer>> m_Term_2_Position_Map;
	HashMap<Integer, String> m_Position_2_Term_Map;
	HashMap<String, Integer> m_Term_2_TF_Map;
	HashMap<String, Object> m_Metadata;

	String m_ID;
	String m_Title;
	String m_URI;

	int m_DocLength;

	public DocumentImpl() {
		m_Term_2_Position_Map = new HashMap<String, List<Integer>>();
		m_Position_2_Term_Map = new HashMap<Integer, String>();
		m_Term_2_TF_Map = new HashMap<String, Integer>();
		m_Metadata = new HashMap<String, Object>();
	}

	public DocumentImpl(String id) {
		this();
		m_ID = id;
	}

	public void addTerm(String term, int position) {

		List<Integer> posList;
		Integer tf;

		if (m_Term_2_Position_Map.containsKey(term)) {
			posList = m_Term_2_Position_Map.get(term);
			posList.add(new Integer(position));
		} else {
			posList = new ArrayList<Integer>();
			posList.add(new Integer(position));
			m_Term_2_Position_Map.put(term, posList);
		}

		m_Position_2_Term_Map.put(new Integer(position), term);

		if (m_Term_2_TF_Map.containsKey(term)) {
			tf = m_Term_2_TF_Map.get(term);

			m_Term_2_TF_Map.put(term, new Integer(tf.intValue() + 1));
		} else {
			m_Term_2_TF_Map.put(term, new Integer(1));
		}

		m_DocLength++;
	}

	// add metadata

	public void addMetaData(String field, Object object) {
		m_Metadata.put(field, object);
	}
	
	/**
	 * @author Bas Wenneker, 05-05-2009
	 */
	public void processBody(){
		if (m_Metadata.containsKey("BODY")) {
			String body = (String) m_Metadata.get("BODY");
			String[] tokens = body.split("\\s");
			
			for (int i = 0; i < tokens.length; i++) {
				this.addTerm(tokens[i], i);
			}
			
			m_DocLength = tokens.length;
		}
	}

	// sets

	public String getContent() {
		return getContent(1, m_DocLength);
	}

	public String getContent(int start, int end) {

		String content = "";

		if (start < 1)
			start = 1;

		if (end > m_DocLength)
			end = m_DocLength;

		for (int i = start; i <= end; i++) {
			content += m_Position_2_Term_Map.get(new Integer(i)) + " ";
		}

		return content;

	}

	public String getID() {
		return m_ID;
	}

	public List<Integer> getTermPositions() {
		// uses the m_Term_2_Position_Map to return the positions
		return null;
	}

	// setters

	public void setID(String id) {
		m_ID = id;
	}

	public void setTitle(String title) {
		m_Title = title;
	}

	public void setURI(String uri) {
		m_URI = uri;
	}

	public String getTitle() {
		return m_Title;
	}

	public boolean contains(String term) {

		if (this.m_Term_2_Position_Map.containsKey(term))
			return true;
		else
			return false;
	}

	public String toString() {
		String string = "";

		String field;

		Object value;

		string += this.m_ID + " (" + this.m_Title + "):" + this.getContent();

		for (Iterator i = m_Metadata.keySet().iterator(); i.hasNext();) {

			field = (String) i.next();

			value = (Object) m_Metadata.get(field);

			string += "Field:" + field + "/Value:" + value + "\n";

		}

		return string;
	}

	public List<Integer> getTermPositions(String term) {
		if (this.m_Term_2_Position_Map.containsKey(term))
			return this.m_Term_2_Position_Map.get(term);
		else return null;
	}

	public Set<String> getTerms() {
		return this.m_Term_2_Position_Map.keySet();
	}

}
