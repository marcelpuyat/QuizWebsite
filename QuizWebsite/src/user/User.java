package user;

import java.sql.*;

import org.json.JSONObject;

public class User {
	
	private long user_id = -1;
	private Connection con;
	
	public User(long user_id, Connection con) {
		this.user_id = user_id;
		this.con = con;
	}
	
	public User(String username, Connection con) {
		this.con = con;
		try {
			String statement = "SELECT id FROM Users WHERE username = (?)";
			PreparedStatement pstmt = this.con.prepareStatement(statement);
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
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
			PreparedStatement pstmt = this.con.prepareStatement(statement);
			pstmt.setLong(1, this.user_id);
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
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
	
	public long getUserId() {
		return user_id;
	}
	
	public String getUserURL() {
		return "/QuizWebsite/User.jsp?user_id="+user_id;
	}
	
	public String getDisplayName() {
		try {
			String statement = "SELECT first_name, last_name FROM Users WHERE (id = ?)";
			PreparedStatement pstmt = this.con.prepareStatement(statement);
			pstmt.setLong(1, this.user_id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString("first_name") + " " + rs.getString("last_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String getFirstName() {
		try {
			String statement = "SELECT first_name FROM Users WHERE (id = ?)";
			PreparedStatement pstmt = this.con.prepareStatement(statement);
			pstmt.setLong(1, this.user_id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString("first_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String getLastName() {
		try {
			String statement = "SELECT last_name FROM Users WHERE (id = ?)";
			PreparedStatement pstmt = this.con.prepareStatement(statement);
			pstmt.setLong(1, this.user_id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString("last_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	
	public String getProfilePicture() {
		try {
			String statement = "SELECT profile_picture FROM Users WHERE (id = ?)";
			PreparedStatement pstmt = this.con.prepareStatement(statement);
			pstmt.setLong(1, this.user_id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString("profile_picture");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public JSONObject getPublicJSONSummary() {
		JSONObject resultJSON = new JSONObject();
		try {
			String statement = "SELECT username, profile_picture, first_name, last_name FROM Users WHERE (id = ?)";
			PreparedStatement pstmt = this.con.prepareStatement(statement);
			pstmt.setLong(1, this.user_id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				resultJSON.accumulate("username", rs.getString("username"));
				resultJSON.accumulate("profile_picture", rs.getString("profile_picture"));
				String first_name =  rs.getString("first_name");
				String last_name  = rs.getString("last_name");
				resultJSON.accumulate("first_name",first_name);
				resultJSON.accumulate("last_name", last_name);
				resultJSON.accumulate("display_name", first_name + " " + last_name);
				resultJSON.accumulate("status", "success");
			} else {
				resultJSON.accumulate("status", "missing data");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			resultJSON.accumulate("status", "sql error");
		}
		return resultJSON;
	}
	
	
	
	
	/* SETTERS */
	public void setFirstName(String first_name) {
		
	}
	public void setLastName(String last_name) {
		
	}
	public void setProfilePicture(String profile_picture_url) {
		
	}
	public void setAdminStatus(boolean isAdmin) {
		
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
