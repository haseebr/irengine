package tudelft.nl.ir.docs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import tudelft.nl.ir.index.Index;

public class DocumentImpl implements Document {

	private HashMap<String, List<Integer>> m_Term_2_Position_Map;
	private HashMap<Integer, String> m_Position_2_Term_Map;

	private HashMap<String, Integer> m_Term_2_TF_Map;
	private HashMap<String, Object> m_Metadata;

	private String m_FilePath;
	private String m_Title;
	private String m_URI;
	private int m_ID;
	private int m_Score;

	private int m_DocLength;

	public DocumentImpl() {
		m_Term_2_Position_Map = new HashMap<String, List<Integer>>();
		m_Position_2_Term_Map = new HashMap<Integer, String>();
		m_Term_2_TF_Map = new HashMap<String, Integer>();
		m_Metadata = new HashMap<String, Object>();
	}

	public DocumentImpl(String filepath, int id) {
		this();
		this.setFilePath(filepath);
		this.setID(id);
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
	public void processBody() {
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
		if (m_Metadata.containsKey("BODY"))
			return (String) m_Metadata.get("BODY");
		return "";
		// return getContent(1, m_DocLength);
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

	public int getID() {
		return m_ID;
	}

	public List<Integer> getTermPositions() {
		// uses the m_Term_2_Position_Map to return the positions
		return null;
	}

	// setters

	public void setID(int id) {
		// MessageDigest digest;
		// try {
		// digest = MessageDigest.getInstance("MD5");
		// digest.update(this.getFilePath().getBytes());
		// digest.update(id.getBytes());
		// id = new BigInteger(1, digest.digest()).toString(16);
		// if (id.length() == 31) {
		// id = "0" + id;
		// }
		// } catch (NoSuchAlgorithmException e) {
		// }
		// m_ID = id;

		this.m_ID = id;
	}

	public void setTitle(String title) {
		m_Title = title;
	}

	public void setURI(String uri) {
		m_URI = uri;
	}

	public String getTitle() {
		return (this.m_Title != null ? this.m_Title : "");
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

		for (Iterator<String> i = m_Metadata.keySet().iterator(); i.hasNext();) {

			field = (String) i.next();

			value = (Object) m_Metadata.get(field);

			string += "Field:" + field + "/Value:" + value + "\n";

		}

		return string;
	}

	public List<Integer> getTermPositions(String term) {
		if (this.m_Term_2_Position_Map.containsKey(term))
			return this.m_Term_2_Position_Map.get(term);
		else
			return null;
	}

	public Set<String> getTerms() {
		return this.m_Term_2_Position_Map.keySet();
	}

	public String getFilePath() {
		return m_FilePath;
	}

	public void setFilePath(String filePath) {
		try {
			m_FilePath = URLEncoder.encode(filePath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getSnippet(String term, Index index) {
		String[] tokens = index.getPreprocessor().getTokens(term);
		String token, snippet = "";
		ArrayList<Integer> positions = new ArrayList<Integer>();
		for(int i = 0; i < tokens.length; i++){
			token = tokens[i];
			if(token != null){
				positions.addAll(this.getTermPositions(token));
			}
		}
		
		Collections.sort(positions);
		String[] textTokens = index.getPreprocessor().tokenizeText(this.getContent());
		for(int i = 0, p1, p2, from, to; i < positions.size(); i++){
			p1 = positions.get(i);
			from = p1 - 4;
			to = p1 + 4;
			for(int j = i; j < positions.size(); j++){
				p2 = positions.get(j);
				if(Math.abs(from - p2) == 4 && p2 < from){
					from = p2 - 4;
					positions.remove(j--);
				}else if(Math.abs(to - p2) == 4 && p2 > to){
					to = p2 + 4;
					positions.remove(j--);
				}
			}
			
			from = Math.max(from, 0);
			to = Math.min(to, textTokens.length);
			for(;from < to; from++){
				snippet += textTokens[from] + " ";
			}
			snippet += " ...";
		}
		
		return snippet;
	}

	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setScore() {
		// TODO Auto-generated method stub
		
	}
}
