package tudelft.nl.ir.storage;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import tudelft.nl.ir.docs.Document;
import tudelft.nl.ir.docs.DocumentImpl;
import tudelft.nl.ir.index.InvertedPosting;
import tudelft.nl.ir.index.Posting;

public class DBStorage {
	private Connection connection;

	public DBStorage() {
		Connection conn = this.getConnection();
		Statement s;
		ResultSet rs;
		try {
			s = conn.createStatement();
			s.executeQuery("SHOW TABLES LIKE 'reuters_document';");
			rs = s.getResultSet();
			if (!rs.next()) {
				// Table not present, so create!
				s.executeUpdate(
					"CREATE TABLE `reuters_document` (" + 
					"  `document_id` int(10) unsigned NOT NULL," + 
					"  `filepath` varchar(128) NOT NULL," + 
					"  `title` varchar(128) NOT NULL," + 
					"  `body` text NOT NULL," + 
					"  PRIMARY KEY  (`document_id`)" + 
					") ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;"
				);
			}

			s = conn.createStatement();
			s.executeQuery("SHOW TABLES LIKE 'reuters_term';");
			rs = s.getResultSet();
			if (!rs.next()) {
				// Table not present, so create!
				s.executeUpdate(
					"CREATE TABLE `reuters_term` ( "+
					"`term_id` int(11) NOT NULL auto_increment," +
					"`document_id` int(10) unsigned NOT NULL," +
					"`term` varchar(128) NOT NULL," +
					"`position` int(10) unsigned NOT NULL," +
					"`tf` mediumint(8) unsigned NOT NULL," +
					"`df` mediumint(8) unsigned NOT NULL," +
					"`tfidf` float unsigned NOT NULL," +
					"PRIMARY KEY  (`term_id`)," +
					"KEY `term` (`term`)," +
					"KEY `document_id` (`document_id`)," +
					"KEY `document_id_2` (`document_id`,`term`)" +
					") ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;"
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection();
		}
	}

	private Connection getConnection() {
		try {
			String user = "root";
			String password = "root";
			String url = "jdbc:mysql://localhost:8889/irengine";
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			this.connection = DriverManager.getConnection(url, user, password);
			this.connection.setAutoCommit(false);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.connection;
	}

	private void closeConnection() {
		if (this.connection != null) {
			try {
				connection.close();
			} catch (Exception e) {
			}
		}
	}

	public boolean hasTerm(String term) {
		Connection conn = this.getConnection();
		Statement s;
		ResultSet rs;
		try {
			s = conn.createStatement();
			s.executeQuery("SELECT * " + "FROM reuters_term " + "WHERE term='" + term + "' LIMIT 1;");
			rs = s.getResultSet();
			this.closeConnection();
			return !!(rs.next());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection();
		}
		return false;
	}

	public ResultSet getTerm(String term) {
		return this.getTerm(term, null);
	}

	public ResultSet getTerm(String term, Document document) {
		Connection conn = this.getConnection();
		Statement s;
		ResultSet rs;
		String sql = "SELECT * " + "FROM reuters_term " + "WHERE term='" + term;

		if (document != null) {
			sql += " AND document_id=" + document.getID();
		}
		sql += " LIMIT 1;";

		try {
			s = conn.createStatement();
			s.executeQuery(sql);
			rs = s.getResultSet();
			this.closeConnection();
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection();
		}
		return null;
	}

	/**
	 * Adds a hashmap of terms term to the database if the terms do not yet
	 * exist.
	 * 
	 * @param HashMap
	 *            map Map to add the entries of.
	 * @param Document
	 *            document Document of the map.
	 */
	public void addMap(HashMap<String, ArrayList<Integer>> map, Document document) {
		Connection conn = this.getConnection();
		List<Integer> positions;
		String term;
		int document_id = document.getID();

		try {
			PreparedStatement ps;
			Statement s1 = conn.createStatement();
			for (Map.Entry<String, ArrayList<Integer>> entry : map.entrySet()) {
				term = entry.getKey();
				positions = entry.getValue();
				if (positions.size() == 0)
					continue;

				try {
					// Here we use a prepared statement because it escapes the
					// term
					// string automatically.
					ps = conn.prepareStatement("INSERT INTO `irengine`.`reuters_term` (`document_id` , `term` , `position`, `tf`, `df`, `tfidf`) VALUES (?, ?, ?, 0, 0, 0)");
					for (int i = 0, size = positions.size(); i < size; i++) {
						ps.setInt(1, document_id);
						ps.setString(2, term);
						ps.setInt(3, positions.get(i));
						ps.addBatch();
					}
					// Execute in batch mode, which is really fast.
					ps.executeBatch();
				} catch (BatchUpdateException b) {
					b.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			conn.commit();
			s1.close();
			this.closeConnection();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Adds a term to the database if it does not yet exist.
	 * 
	 * @param String
	 *            term -
	 * @param Document
	 *            document - Document of the term.
	 * @param ArrayList
	 *            positions - Positions of the term in the document.
	 */
	public void addTerm(String term, Document document, List<Integer> positions) {
		Connection conn = this.getConnection();
		Statement s;
		ResultSet rs;
		PreparedStatement ps;

		try {
			s = conn.createStatement();
			s.executeQuery("SELECT term_id " + "FROM reuters_term " + "WHERE term='" + term + "' " + "AND document_id=" + document.getID() + ";");

			rs = s.getResultSet();
			if (!rs.next() && positions.size() > 0) {
				// Use a prepared statement because of the speed and it escapes
				// the inserted values.
				ps = conn.prepareStatement("INSERT INTO `irengine`.`reuters_term` (`document_id` , `term` , `position`) VALUES (?, ?, ?)");
				for (int i = 0, size = positions.size(); i < size; i++) {
					ps.setInt(1, document.getID());
					ps.setString(2, term);
					ps.setInt(3, positions.get(i));
					ps.addBatch();
				}
				s.executeBatch();
				conn.commit();
			}
			s.close();
		} catch (BatchUpdateException b) {
			b.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		this.closeConnection();
	}

	/**
	 * Adds a document to the db. It looks in the database if the id of the
	 * document already exists, if not, the document is added.
	 * 
	 * @param Document
	 *            document Document to add to the db.
	 */
	public void addDocument(Document document) {
		Connection conn = this.getConnection();
		Statement s;
		ResultSet rs;

		try {
			s = conn.createStatement();
			s.executeQuery("SELECT document_id " + "FROM reuters_document " + "WHERE document_id=" + document.getID() + ";");

			rs = s.getResultSet();
			if (!rs.next()) {
				PreparedStatement ps = conn.prepareStatement("INSERT INTO `irengine`.`reuters_document` (`document_id` , `filepath`, `title` , `body`) VALUES (?, ?, ?, ?);");
				ps.setInt(1, document.getID());
				ps.setString(2, document.getFilePath());
				ps.setString(3, document.getTitle());
				ps.setString(4, document.getContent());
				ps.executeUpdate();
				conn.commit();
			}
			s.close();
		} catch (BatchUpdateException b) {
			b.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.closeConnection();
	}

	/**
	 * Returns all postings for a given term. The positions of the term are
	 * looked up in the database.
	 * 
	 * @param String
	 *            term - Term to look up in the database.
	 * @return An arraylist of postings for the given term.
	 */
	public List<Posting> getPostings(String term) {
		SortedMap<Integer, ArrayList<Integer>> map = new TreeMap<Integer, ArrayList<Integer>>();
		SortedMap<Integer, DocumentImpl> docmap = new TreeMap<Integer, DocumentImpl>();
		DocumentImpl doc;
		ArrayList<Integer> positions = new ArrayList<Integer>();
		ArrayList<Posting> result = new ArrayList<Posting>();

		Connection conn = this.getConnection();
		Statement s1, s2;
		ResultSet rs, docrs;
		int document_id, position;
		try {
			/*
			SELECT * , SUM( tfidf ) AS sum
			FROM `reuters_term`
			WHERE term = 'crop'
			GROUP BY document_id
			ORDER BY sum DESC*/
			s1 = conn.createStatement();
			s1.executeQuery(
				"SELECT document_id, position " +
				"FROM reuters_term " +
				"WHERE term='" + term + "' " +
				"ORDER BY tfidf DESC;"
			);
			rs = s1.getResultSet();
			while (rs.next()) {
				document_id = rs.getInt("document_id");
				position = rs.getInt("position");
				if (!docmap.containsKey(document_id)) {
					s2 = conn.createStatement();
					s2.executeQuery(
						"SELECT filepath, title, body " +
						"FROM reuters_document " +
						"WHERE document_id=" + document_id + " LIMIT 1;"
					);
					docrs = s2.getResultSet();
					if (docrs.first()) {
						doc = new DocumentImpl(docrs.getString("filepath"), document_id);
						doc.setTitle(docrs.getString("title"));
						doc.addMetaData("BODY", (String) docrs.getString("body"));
						docmap.put(document_id, doc);
					}
				} else {
					doc = docmap.get(document_id);
				}

				if (map.containsKey(document_id)) {
					positions = map.get(document_id);
					positions.add(position);
				} else {
					positions = new ArrayList<Integer>();
					positions.add(position);
					map.put(document_id, positions);
				}
			}
			//this.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//this.closeConnection();
		}
		
		InvertedPosting p;
		for (Map.Entry<Integer, ArrayList<Integer>> entry : map.entrySet()) {
			document_id = entry.getKey();
			positions = entry.getValue();
			if (docmap.containsKey(document_id)) {
				
				p = new InvertedPosting(docmap.get(document_id), positions);
				try {

					Statement s = conn.createStatement();
					s.executeQuery(
						"SELECT tfidf " +
						"FROM reuters_term " +
						"WHERE term='"+ term +"' " +
						"AND document_id="+document_id +" " +
						"LIMIT 1;"	
					);

					rs = s.getResultSet();
					if (rs.first()) {
						p.setScore(rs.getDouble("tfidf"));
					}
					s.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				result.add(p);
			}
		}
		
		return result;
	}

	public DocumentImpl getDocument(String document_id) {
		Connection conn = this.getConnection();
		Statement s;
		ResultSet rs;
		DocumentImpl result = null;

		try {
			s = conn.createStatement();
			s.executeQuery("SELECT title, body FROM reuters_document " + "WHERE document_id=" + document_id + " LIMIT 1;");

			rs = s.getResultSet();
			if (rs.next()) {
				result = new DocumentImpl();
				result.setTitle(rs.getString("title"));
				result.addMetaData("BODY", rs.getString("body"));
			}
			s.close();
		} catch (Exception e) {
			this.closeConnection();
			return null;
		}

		if (result != null) {
			try {
				s = conn.createStatement();
				s.executeQuery("SELECT term, position FROM reuters_term WHERE document_id=" + document_id + ";");

				rs = s.getResultSet();
				// HashMap<String, List<Integer>> term2pos =
				// result.getTerm_2_Position_Map();
				// HashMap<Integer, String> pos2term =
				// result.getPosition_2_Term_Map();
				// HashMap<String, Integer> term2tf = result.getTerm_2_TF_Map();
				// String term;
				// int position;
				while (rs.next()) {
					result.addTerm(rs.getString("term"), rs.getInt("position"));

					// term = rs.getString("term");
					// position = rs.getInt("position");
					// if(!term2pos.containsKey(term)){
					// term2pos.put(term, new ArrayList<Integer>());
					// }
					// term2pos.get(term).add(position);
					// pos2term.put(position, term);
					// if(!term2tf.containsKey(term)){
					// term2tf.put(term, 1);
					// }else{
					// term2tf.put(term, term2tf.get(term)+1);
					// }
				}
				s.close();
			} catch (Exception e) {
				this.closeConnection();
				return null;
			}
		}
		this.closeConnection();
		return result;
	}


	public void computeTFIDF() {
		Connection conn = this.getConnection();
		Statement s;
		ResultSet rs;

		try {
			s = conn.createStatement();
			
			s.setQueryTimeout(0);
			s.executeUpdate(
				"UPDATE reuters_term SET tf = fn_get_tf(document_id, term) " +
				"WHERE reuters_term.document_id=document_id;"
			);

			s.close();
		} catch (BatchUpdateException b) {
			b.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			s = conn.createStatement();
			s.executeQuery(
				"SELECT term, COUNT( DISTINCT (document_id) ) AS df " +
				"FROM reuters_term " +
				"GROUP BY term"
			);

			rs = s.getResultSet();
			while (rs.next()) {
				s = conn.createStatement();
				s.setQueryTimeout(0);
				s.executeUpdate("UPDATE reuters_term SET df = "+ rs.getInt("df") +" WHERE term='"+rs.getString("term")+"';");
			}
			conn.commit();
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
			this.closeConnection();
		}
		
		try {
			s = conn.createStatement();
			s.executeQuery("SELECT COUNT(*) as document_cnt FROM reuters_document");
			rs = s.getResultSet();
			rs.first();
			int count = rs.getInt("document_cnt");
			s = conn.createStatement();
			s.executeUpdate("UPDATE reuters_term SET tfidf = tf * ( LOG( "+ count +" / df ));");
			conn.commit();// 
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
			this.closeConnection();
		}

		this.closeConnection();
	}

	/**
	 * Make sure to close the connection when this object's garbage collected.
	 */
	protected void finalize() throws Throwable {
		this.closeConnection();
		super.finalize();
	}
}
