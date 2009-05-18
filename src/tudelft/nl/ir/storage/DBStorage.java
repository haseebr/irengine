package tudelft.nl.ir.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
		 				"WHERE term='"+ term +"'";
		
		if(document != null){
			sql += " AND document_id='"+ document.getID() +"'";
		}
		sql += " LIMIT 1;";
		//System.out.println(sql);
		try {
			s = conn.createStatement();			
			s.executeQuery(sql);
			rs = s.getResultSet();
			return rs.first() ? rs : null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection();
		}
		return null;	
	}
	
	/**
	 * Adds a term to the database if it does not yet exist.
	 * @param term
	 * @param document
	 * @param positions
	 */
	public void addTerm(String term, Document document, List<Integer> positions){
		Connection conn = this.getConnection();
		Statement s;
		ResultSet rs;
		String sql;
		String term_id;
		
		if(term.length() >0 && positions.size() > 0){
			try{
				s = conn.createStatement();
//				s.executeQuery (
//						"SELECT term_id "+
//						 "FROM Reuters21578 "+
//						 "WHERE term='"+ term +"' " +
//						 "AND document_id ="+ document.getID() +";"
//				);
//				rs = s.getResultSet();
				
//				rs = this.getTerm(term, document);
				s.executeQuery (
						"SELECT term_id "+
						 "FROM Reuters21578 "+
						 "WHERE term='"+ term +"' " +
						 "AND document_id ='"+ document.getID() +"' LIMIT 1;"
				);
				rs = s.getResultSet();
				rs.first();
				
				if(!rs.next()){
					s.executeUpdate (
						"INSERT INTO `irengine`.`reuters21578` (`term_id`, `document_id` , `filepath` , `term`)"+
						"VALUES (NULL, '"+ document.getID() +"', '"+ document.getFilePath() +"', '"+ term +"');"
					);
					
					s.executeQuery (
							"SELECT term_id "+
							 "FROM Reuters21578 "+
							 "WHERE term='"+ term +"' " +
							 "AND document_id ='"+ document.getID() +"' LIMIT 1;"
					);
					rs = s.getResultSet();
					rs.first();
				}
				
				if(rs == null) System.out.println("error sql");
				term_id = rs.getString(1);				
				sql = "INSERT INTO `irengine`.`reuters21578_position` (" +
					"`term_id` , `position` ) VALUES ";
				
				for(int i = 0, size = positions.size(); i < size; i++){
//					sql += " ("+ term_id +","+ positions.get(i) +")";
					
					try{
						s.executeUpdate(
							"INSERT INTO `irengine`.`reuters21578_position` (" +
							"`term_id` , `position` ) VALUES " +
							" ("+ term_id +","+ positions.get(i) +")"
						);
					}catch(Exception e){}
//					if(i == (size-1)){
//						sql += ";";
//					}else{
//						sql += ",";
//					}
				}				
//				s.executeUpdate(sql);							
			} catch (SQLException e) {
				System.out.println("TERM:"+ term);
				e.printStackTrace();
			}finally{
				this.closeConnection();
			}
		}
		/*
		for(int i = 0; i < positions.size(); i++){			
			try {
				s = conn.createStatement();
//				s.executeQuery (
//						"SELECT * "+
//						 "FROM Reuters21578 "+
//						 "WHERE term='"+ term +"' " +
//						 "AND document_id ="+ document.getID() +" " +
//						 "AND position="+ positions.get(i) +";"
//				);
//				rs = s.getResultSet();
//				if(!rs.next()){
					s.executeUpdate (
						"INSERT INTO `irengine`.`reuters21578` (`document_id` , `filepath` , `term` , `position`)"+
						"VALUES ('"+ document.getID() +"', '"+ document.getFilePath() +"', '"+ term +"', '"+positions.get(i)+"');"
					);
//				}
				
					
				s = conn.createStatement();
				s.executeQuery (
						"SELECT term_id "+
						 "FROM Reuters21578 "+
						 "WHERE term='"+ term +"' " +
						 "AND document_id ="+ document.getID() +";"
				);
				rs = s.getResultSet();
				if(!rs.next()){
					s.executeUpdate (
						"INSERT INTO `irengine`.`reuters21578` (`document_id` , `filepath` , `term` , `position`)"+
						"VALUES ('"+ document.getID() +"', '"+ document.getFilePath() +"', '"+ term +"', '"+positions.get(i)+"');"
					);
				}
			} catch (SQLException e) {
				//System.out.println("duplicate:"+term);
				//e.printStackTrace();
			}	
		}
		this.closeConnection();*/
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
