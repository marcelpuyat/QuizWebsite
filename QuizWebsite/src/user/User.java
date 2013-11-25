package user;

import java.sql.*;

public class User {
	
	private long user_id = -1;
	private Connection con;

	public User(Connection conn) {
		
	}
	
	public User(long user_id, Connection con) {
		this.user_id = user_id;
		this.con = con;
	}
	
	public User(String username, Connection con) {
		this.con = con;
		try {
			Statement stmt = con.createStatement();
			String getNameQuery = "SELECT id FROM Users WHERE username = \"" + username + "\"";
			ResultSet rs = stmt.executeQuery(getNameQuery);
			if (rs.next()) {
				user_id = rs.getLong("id");
			}
		}
		catch (Exception e) { e.printStackTrace();}
	}
	
	public boolean existsInDB() {
		if (this.user_id == -1) return false;
		try {
			String statement = "SELECT id FROM Users WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(statement);
			pstmt.setLong(1, this.user_id);
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public long getUserId() {
		return user_id;
	}

	
	public boolean matchesPassword(String password) {
		byte[] hashedPass = Hasher.hashPassword(password, this.getSalt());
		try {
			String statement = "SELECT password FROM Users WHERE (id = ? AND password = ?)";
			PreparedStatement pstmt = this.con.prepareStatement(statement);
			pstmt.setLong(1, this.user_id);
			pstmt.setBytes(2, hashedPass);
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getName() {
		return "";
	}
	
	public String getUserName() {
		try {
			String statement = "SELECT username FROM Users WHERE (id = ?)";
			PreparedStatement pstmt = this.con.prepareStatement(statement);
			pstmt.setLong(1, this.user_id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString("username");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	
	/* PRIVATE */
	
	private byte[] getSalt() {
		try {
			String statement = "SELECT salt FROM Users WHERE id = (?)";
			PreparedStatement pstmt = con.prepareStatement(statement);
			pstmt.setLong(1, this.user_id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getBytes("salt");
			} else {
				System.out.println("dead here");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
