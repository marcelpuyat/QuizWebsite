package customObjects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import databases.DBInfo;

public class SelfRefreshingConnection {

	private static final int TIMEOUT_LENGTH = 3;
	private Connection con;
	
	public SelfRefreshingConnection(Connection con) {
		this.con = con;
	}
	
	public Statement createStatement() throws SQLException, ClassNotFoundException {
		refreshConnection();
		return con.createStatement();
	}
	
	public PreparedStatement prepareStatement(String statement) throws SQLException, ClassNotFoundException {
		refreshConnection();
		return con.prepareStatement(statement);
	}
	
	private void refreshConnection() throws SQLException, ClassNotFoundException {
		if (!con.isValid(TIMEOUT_LENGTH)) {
			con.close();
			this.con = this.generateNewConnection();
		}
	}
	
	private Connection generateNewConnection() {
		Connection newCon;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			newCon = DriverManager.getConnection 
					( "jdbc:mysql://" + DBInfo.MYSQL_DATABASE_SERVER, DBInfo.MYSQL_USERNAME ,DBInfo.MYSQL_PASSWORD);
			Statement stmt = newCon.createStatement();
			stmt.executeQuery("USE " + DBInfo.MYSQL_DATABASE_NAME);
			return newCon;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void close() throws SQLException {
		this.con.close();
	}
}
