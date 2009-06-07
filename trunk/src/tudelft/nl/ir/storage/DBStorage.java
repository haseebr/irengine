package tudelft.nl.ir.storage;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
		try {
			s = conn.createStatement();
			s.executeQuery ("SHOW TABLES LIKE 'Reuters21572';");
			ResultSet rs = s.getResultSet();
			if(!rs.next()){
				// Table not present, so create!
//				s.executeUpdate (
//			               "CREATE TABLE animal ("
//			               + "id INT UNSIGNED NOT NULL AUTO_INCREMENT,"
//			               + "PRIMARY KEY (id),"
//			               + "name CHAR(40), category CHAR(40))");
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			this.closeConnection();
		}	
	}
	
	private Connection getConnection(){
		try {
			String user = "root";
			String password = "";
			String url = "jdbc:mysql://localhost/irengine";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			this.connection = DriverManager.getConnection(url, user, password);
			this.connection.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.connection;
	}
	
	private void closeConnection(){
		if (this.connection != null) {
			try {
				connection.close();
			} catch (Exception e) {
			}
		}
	}
	
	public boolean hasTerm(String term){
		Connection conn = this.getConnection();
		Statement s;
		ResultSet rs;
		try {
			s = conn.createStatement();			
			s.executeQuery (
					"SELECT * "+
					 "FROM reuters_term "+
					 "WHERE term='"+ term +"' LIMIT 1;"
			);
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
	
	public ResultSet getTerm(String term){
		return this.getTerm(term, null);
	}
	
	public ResultSet getTerm(String term, Document document){
		Connection conn = this.getConnection();
		Statement s;
		ResultSet rs;
		String sql = "SELECT * "+
		 				"FROM reuters_term "+
		 				"WHERE term='"+ term;
		
		if(document != null){
			sql += " AND document_id="+ document.getID();
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
	 * Adds a hashmap of terms term to the database if the terms do not yet exist.
	 * @param HashMap map Map to add the entries of.
	 * @param Document document Document of the map.
	 */
	public void addMap(HashMap<String, ArrayList<Integer>> map, Document document){
		Connection conn = this.getConnection();
		ResultSet rs;
		List<Integer> positions;
		String term;
		int id = document.getID();
		
		try{
			PreparedStatement ps;
			Statement s1 = conn.createStatement();
			for( Map.Entry<String, ArrayList<Integer>> entry : map.entrySet()){
				term = entry.getKey();
				positions = entry.getValue();
				if(positions.size()==0)
					continue;
				
				try {					
					//s1 = conn.createStatement();
//					s1.executeQuery (
//						"SELECT term_id "+
//						 "FROM reuters_term "+
//						 "WHERE term='"+ term +"' " +
//						 "AND document_id='"+ id +"';"
//					);
//					
//					rs = s1.getResultSet();
//					if(!rs.next()){						
//						for(int i = 0, size = positions.size(); i < size; i++){
//							//s1.addBatch("INSERT INTO `irengine`.`reuters_term` (`document_id` , `term` , `position`) VALUES ("+ id +", '"+ document.getFilePath() +"', '"+ term +"', '"+positions.get(i)+"')");
//						}
//						s1.executeBatch();
						
						ps = conn.prepareStatement("INSERT INTO `irengine`.`reuters_term` (`document_id` , `term` , `position`) VALUES (?, ?, ?)");
						for(int i = 0, size = positions.size(); i < size; i++){
							ps.setInt(1, document.getID());
							ps.setString(2, term);
							ps.setInt(3, positions.get(i));
							ps.addBatch();
							//s.addBatch("INSERT INTO `irengine`.`reuters_term` (`document_id` , `filepath` , `term` , `position`) VALUES ("+ document.getID() +", '"+ document.getFilePath() +"', '"+ term +"', '"+positions.get(i)+"')");
						}
						ps.executeBatch();
//					}
				} catch(BatchUpdateException b) {
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
	 * @param String term - 
	 * @param Document document - Document of the term.
	 * @param ArrayList positions - Positions of the term in the document.
	 */
	public void addTerm(String term, Document document, List<Integer> positions){
		Connection conn = this.getConnection();
		Statement s;
		ResultSet rs;
		
		try {					
			s = conn.createStatement();
			s.executeQuery (
				"SELECT term_id "+
				 "FROM reuters_term "+
				 "WHERE term='"+ term +"' " +
				 "AND document_id="+ document.getID() +";"
			);
			
			rs = s.getResultSet();
			if(!rs.next() && positions.size() > 0){
				/**
				 * Build up the insert query, this is more efficient than 
				 * inserting all terms one by one.
				 */
				//s = conn.createStatement();
				//String sql = "INSERT INTO `irengine`.`reuters_term` (`document_id` , `filepath` , `term` , `position`) VALUES ";
				PreparedStatement ps = conn.prepareStatement("INSERT INTO `irengine`.`reuters_term` (`document_id` , `term` , `position`) VALUES (?, ?, ?, ?)");
				for(int i = 0, size = positions.size(); i < size; i++){
					ps.setInt(1, document.getID());
					ps.setString(2, term);
					ps.setInt(3, positions.get(i));
					ps.addBatch();
					//s.addBatch("INSERT INTO `irengine`.`reuters_term` (`document_id` , `filepath` , `term` , `position`) VALUES ("+ document.getID() +", '"+ document.getFilePath() +"', '"+ term +"', '"+positions.get(i)+"')");
				}
				s.executeBatch();
				conn.commit();
			}

			s.close();
		} catch(BatchUpdateException b) {

			b.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}	
		this.closeConnection();
	}
	public void commit(){
		try {
			this.getConnection().commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void addDocument(Document document){
		Connection conn = this.getConnection();
		Statement s;
		ResultSet rs;
		
		try {					
			s = conn.createStatement();
			s.executeQuery (
				"SELECT document_id "+
				 "FROM reuters_document "+
				 "WHERE document_id="+ document.getID() +";"
			);
			
			rs = s.getResultSet();
			if(!rs.next()){
				PreparedStatement ps = conn.prepareStatement("INSERT INTO `irengine`.`reuters_document` (`document_id` , `filepath`, `title` , `body`) VALUES (?, ?, ?, ?);");
				ps.setInt(1, document.getID());
				ps.setString(2, document.getFilePath());
				ps.setString(3, document.getTitle());
				ps.setString(4, document.getContent());
				ps.executeUpdate();
				//s.executeUpdate("INSERT INTO `irengine`.`reuters_document` (`document_id` , `title` , `body`) VALUES ("+ document.getID() +", '"+ document.getTitle() +"', '"+ document.getContent() +"')");
				conn.commit();
			}
			s.close();
		} catch(BatchUpdateException b) {

			b.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}	
		this.closeConnection();
	}
	public ArrayList<Posting> getPostings(String term){
		SortedMap<Integer, ArrayList<Integer>> map = new TreeMap<Integer, ArrayList<Integer>>();
		HashMap<Integer, DocumentImpl> docmap = new HashMap<Integer, DocumentImpl>();
		DocumentImpl doc;
		ArrayList<Integer> positions = new ArrayList<Integer>();
		ArrayList<Posting> result = new ArrayList<Posting>();
		
		Connection conn = this.getConnection();
		Statement s, s2;
		ResultSet rs, docrs;
		int document_id, position;
		try {
			s = conn.createStatement();			
			s.executeQuery (
					"SELECT document_id, position "+
					 "FROM reuters_term "+
					 "WHERE term='"+ term +"' " +
					 "ORDER BY document_id DESC;"
			);
			rs = s.getResultSet();
			while(rs.next()){
				document_id = rs.getInt("document_id");
				position = rs.getInt("position");
				if(!docmap.containsKey(document_id)){
					s2 = conn.createStatement();	
					s2.executeQuery (
							"SELECT filepath, title, body "+
							 "FROM reuters_document "+
							 "WHERE document_id="+ document_id +" LIMIT 1;"
					);
					docrs = s2.getResultSet();
					if(docrs.first()){
						doc = new DocumentImpl(docrs.getString("filepath"), document_id);
						doc.setTitle(docrs.getString("title"));
						doc.addMetaData("BODY", (String)docrs.getString("body"));
						docmap.put(document_id, doc);
					}
				}else{
					doc = docmap.get(document_id);
				}
									
				if(map.containsKey(document_id)){
					positions = map.get(document_id);
					positions.add(position);
				}else{
					positions = new ArrayList<Integer>();
					positions.add(position);
					map.put(document_id, positions);
				}	
			}	
			this.closeConnection();		
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection();
		}

		for(Map.Entry<Integer, ArrayList<Integer>> entry : map.entrySet()){
			document_id = entry.getKey();
			positions = entry.getValue();
			if(docmap.containsKey(document_id)){
				result.add(new InvertedPosting(docmap.get(document_id), positions));
			}
		}
		
		return result;		
	}
	/**
	 * Make sure to close the connection when this object's garbage collected.
	 */
	protected void finalize() throws Throwable {
		this.closeConnection();
		super.finalize();
	}
}
