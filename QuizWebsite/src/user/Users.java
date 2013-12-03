package user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import customObjects.SelfRefreshingConnection;

public class Users {
	
	public static boolean createUser(String username, String password, SelfRefreshingConnection con) throws ClassNotFoundException {
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
	
	
	public static boolean usernameExists(String username, SelfRefreshingConnection con) throws ClassNotFoundException {
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
	
	public static boolean changePassword(String username, String currentPassword, String newPassword, SelfRefreshingConnection con) throws ClassNotFoundException{
		try {
			if(usernameExists(username, con)){
				User user = new User(username, con);
				if(user.matchesPassword(currentPassword)){
					String getSalt = "SELECT salt FROM Users WHERE id = (?)";
					PreparedStatement pstmt = con.prepareStatement(getSalt);
					pstmt.setLong(1, user.getUserId());
					ResultSet rs = pstmt.executeQuery();
					if (rs.next()){ //just like in usernameExists, uses this to make sure that it found the salt
						byte[] salt = rs.getBytes("salt");
						byte[] updatedPassword = Hasher.hashPassword(newPassword, salt);
						String changePassword = "UPDATE Users SET password = '"+updatedPassword+"' WHERE id = (?)";
						PreparedStatement pstmt2 = con.prepareStatement(changePassword);
						pstmt2.setLong(1, user.getUserId());
						pstmt2.execute();
						return true;
					}
				}
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
