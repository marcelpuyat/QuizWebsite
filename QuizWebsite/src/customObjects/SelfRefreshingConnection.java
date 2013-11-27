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
			Class.forName("com.mysql.jdbc.Driver"); 

			Connection newCon = DriverManager.getConnection 
					( "jdbc:mysql://" + DBInfo.MYSQL_DATABASE_SERVER, DBInfo.MYSQL_USERNAME ,DBInfo.MYSQL_PASSWORD);
			Statement stmt = newCon.createStatement();
			stmt.executeQuery("USE " + DBInfo.MYSQL_DATABASE_NAME);
			this.con = newCon;
		}
	}
	
	public void close() throws SQLException {
		this.con.close();
	}
}
