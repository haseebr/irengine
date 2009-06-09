package tudelft.nl.ir.preprocessing;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

public class MultiLingualStopwordRemover implements StopwordRemover {
	private HashSet<String> m_Stopwords;
	
	public MultiLingualStopwordRemover() {
		this.m_Stopwords = new HashSet<String>();
		this.loadStopwordsFromFile("stopwords//multilingual.txt");
	}
	/**
	 * Loads multilingual stopwords from a file. 
	 */
	public void loadStopwordsFromFile(String file) {
		try {
			FileInputStream fstream = new FileInputStream(file);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = br.readLine()) != null) {
				this.m_Stopwords.add(line);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Returns true when term is a stopword.
	 */
	public boolean isStopword(String term) {
		return this.m_Stopwords.contains(term);
	}
}
