package tudelft.nl.ir.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tudelft.nl.ir.docs.Document;

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
			} catch (SQLException e) {
				//System.out.println("duplicate:"+term);
				//e.printStackTrace();
			}	
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
