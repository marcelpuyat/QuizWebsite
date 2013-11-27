package user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import customObjects.SelfRefreshingConnection;

public class User {
	
	private long user_id = -1;
	private SelfRefreshingConnection con;
	
	public User(long user_id, SelfRefreshingConnection con) {
		this.user_id = user_id;
		this.con = con;
	}
	
	public User(String username, SelfRefreshingConnection con) {
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
	
	public boolean existsInDB() throws ClassNotFoundException {
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
	
	public boolean matchesPassword(String password) throws ClassNotFoundException {
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
	
	public String getDisplayName() throws ClassNotFoundException {
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
	
	public String getFirstName() throws ClassNotFoundException {
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
	
	public String getLastName() throws ClassNotFoundException {
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
	
	public String getUserName() throws ClassNotFoundException {
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
	
	public String getProfilePicture() throws ClassNotFoundException {
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
	
	public boolean hasCompletedProfile() {
		try {
			String statement = "SELECT first_name, last_name FROM Users WHERE (id = ?)";
			PreparedStatement pstmt = this.con.prepareStatement(statement);
			pstmt.setLong(1, this.user_id);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) return false;
			String first_name = rs.getString("first_name");
			if (first_name == null || first_name.equals("")) return false;
			String last_name = rs.getString("last_name");
			if (last_name == null || last_name.equals("")) return false;
			return true;
		}
		catch (SQLException e) {e.printStackTrace();}
		catch(ClassNotFoundException e){e.printStackTrace();}
		return false;
	}
	
	
	public JSONObject getPublicJSONSummary() throws ClassNotFoundException {
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
	
	public boolean isAdmin() {
		try {
			String statement = "SELECT is_admin FROM Users WHERE (id = ?)";
			PreparedStatement pstmt = this.con.prepareStatement(statement);
			pstmt.setLong(1, this.user_id);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) return false;
			return rs.getBoolean("is_admin");
		}
		catch (SQLException e) {e.printStackTrace();}
		catch(ClassNotFoundException e){e.printStackTrace();}
		return false;
	}
	
	
	
	
	/* SETTERS */
	public void setFirstName(String first_name) {
		try {
			String statement = "UPDATE Users SET first_name = (?) WHERE (id = ?)";
			PreparedStatement pstmt = this.con.prepareStatement(statement);
			pstmt.setString(1, first_name);
			pstmt.setLong(2, this.user_id);
			pstmt.execute();
		}
		catch (SQLException e) {e.printStackTrace();}
		catch(ClassNotFoundException e){e.printStackTrace();}
	}
	public void setLastName(String last_name) {
		try {
			String statement = "UPDATE Users SET last_name = (?) WHERE (id = ?)";
			PreparedStatement pstmt = this.con.prepareStatement(statement);
			pstmt.setString(1, last_name);
			pstmt.setLong(2, this.user_id);
			pstmt.execute();
		}
		catch (SQLException e) {e.printStackTrace();}
		catch(ClassNotFoundException e){e.printStackTrace();}
	}
	public void setProfilePicture(String profile_picture_url) {
		try {
			String statement = "UPDATE Users SET profile_picture = (?) WHERE (id = ?)";
			PreparedStatement pstmt = this.con.prepareStatement(statement);
			pstmt.setString(1, profile_picture_url);
			pstmt.setLong(2, this.user_id);
			pstmt.execute();
		}
		catch (SQLException e) {e.printStackTrace();}
		catch(ClassNotFoundException e){e.printStackTrace();}
	}
	public void setAdminStatus(boolean isAdmin) {
		
	}
	
	
	
	/* PRIVATE */
	
	private byte[] getSalt() throws ClassNotFoundException {
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
