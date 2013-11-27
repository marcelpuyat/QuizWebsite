package user;

<<<<<<< HEAD
import java.sql.*;
=======
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import customObjects.SelfRefreshingConnection;
>>>>>>> CustomConnectionBranch

public class Users {
	
	public static boolean createUser(String username, String password, Connection con) {
		//try to create user, if username already exists or fields are incorrect, return null
		try {
			if (usernameExists(username, con)) return false;
			String statement = "INSERT INTO Users (username,salt,password) VALUES (?,?,?)";
			PreparedStatement pstmt = con.prepareStatement(statement);
			pstmt.setString(1, username);
			
			byte[] salt = Hasher.getSalt();
			pstmt.setBytes(2, salt); //salt
			pstmt.setBytes(3, Hasher.hashPassword(password, salt)); //password
			pstmt.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	public static boolean usernameExists(String username, Connection con) {
		try {
			Statement stmt = con.createStatement();
			String getAccountByUsername = "SELECT username FROM Users WHERE username = '"+username+"'";
			ResultSet rs = stmt.executeQuery(getAccountByUsername);
			return rs.next(); //if returns true, a row is found containing the username, therefore username exists
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
}
