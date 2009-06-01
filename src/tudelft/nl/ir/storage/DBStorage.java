package tudelft.nl.ir.storage;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tudelft.nl.ir.docs.Document;
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
		} catch (SQLException e) {
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
					 "FROM Reuters21578 "+
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
		 				"FROM Reuters21578 "+
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
		String term, id = document.getID();
		
		try{
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
//						 "FROM Reuters21578 "+
//						 "WHERE term='"+ term +"' " +
//						 "AND document_id='"+ id +"';"
//					);
//					
//					rs = s1.getResultSet();
//					if(!rs.next()){						
						for(int i = 0, size = positions.size(); i < size; i++){
							s1.addBatch("INSERT INTO `irengine`.`reuters21578` (`document_id` , `filepath` , `term` , `position`) VALUES ('"+ id +"', '"+ document.getFilePath() +"', '"+ term +"', '"+positions.get(i)+"')");
						}
						s1.executeBatch();
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
				 "FROM Reuters21578 "+
				 "WHERE term='"+ term +"' " +
				 "AND document_id='"+ document.getID() +"';"
			);
			
			rs = s.getResultSet();
			if(!rs.next() && positions.size() > 0){
				/**
				 * Build up the insert query, this is more efficient than 
				 * inserting all terms one by one.
				 */
				//s = conn.createStatement();
				//String sql = "INSERT INTO `irengine`.`reuters21578` (`document_id` , `filepath` , `term` , `position`) VALUES ";
				for(int i = 0, size = positions.size(); i < size; i++){
					s.addBatch("INSERT INTO `irengine`.`reuters21578` (`document_id` , `filepath` , `term` , `position`) VALUES ('"+ document.getID() +"', '"+ document.getFilePath() +"', '"+ term +"', '"+positions.get(i)+"')");
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
	public ArrayList<Posting> getPostings(String term){
		ArrayList<Posting> result = new ArrayList<Posting>();
		Connection conn = this.getConnection();
		Statement s;
		ResultSet rs;
		try {
			s = conn.createStatement();			
			s.executeQuery (
					"SELECT position "+
					 "FROM Reuters21578 "+
					 "WHERE term='"+ term +"';"
			);
			rs = s.getResultSet();
			this.closeConnection();
			while(rs.next()){
				//result.add()
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection();
		}
		return null;		
	}
	/**
	 * Make sure to close the connection when this object's garbage collected.
	 */
	protected void finalize() throws Throwable {
		this.closeConnection();
		super.finalize();
	}
}
