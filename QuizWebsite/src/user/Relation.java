package user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import customObjects.SelfRefreshingConnection;

public class Relation {

	/**
	 * 
	 * @param userA Requester
	 * @param userB
	 * @param con
	 */
	public static void requestFriendship(User userA, User userB, SelfRefreshingConnection con) {
		try {
			PreparedStatement stmt = con.prepareStatement("INSERT INTO Relations (user_a_id, user_b_id, status) VALUES (?, ?, ?)");
			long user_a_id = userA.getUserId();
			long user_b_id = userB.getUserId();
			stmt.setLong(1, user_a_id);
			stmt.setLong(2, user_b_id);
			stmt.setString(3, RelationConstants.FRIEND_REQUESTED);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * UserA accepts UserB's request
	 * @param userA 
	 * @param userB
	 * @param con
	 */
	public static void acceptFriendship(User userA, User userB, SelfRefreshingConnection con) {
		try {
			PreparedStatement stmt = con.prepareStatement("INSERT INTO Relations (user_a_id, user_b_id, status) VALUES (?, ?, ?)");
			long user_a_id = userA.getUserId();
			long user_b_id = userB.getUserId();
			stmt.setLong(1, user_a_id);
			stmt.setLong(2, user_b_id);
			stmt.setString(3, RelationConstants.IS_FRIEND);
			stmt.executeUpdate();
			
			Statement stmt2 = con.createStatement();
			String updateReqToFrd = "UPDATE Relations SET status = \"" + RelationConstants.IS_FRIEND + "\" WHERE user_a_id = " + user_b_id + " AND user_b_id = " + user_a_id + " AND status = \"" + RelationConstants.FRIEND_REQUESTED + "\"";
			stmt2.executeUpdate(updateReqToFrd);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param userA Blocker
	 * @param userB
	 * @param con
	 */
	public static void blockUser(User userA, User userB, SelfRefreshingConnection con) {
		try {
			PreparedStatement stmt = con.prepareStatement("INSERT INTO Relations (user_a_id, user_b_id, status) VALUES (?, ?, ?)");
			long user_a_id = userA.getUserId();
			long user_b_id = userB.getUserId();
			stmt.setLong(1, user_a_id);
			stmt.setLong(2, user_b_id);
			stmt.setString(3, RelationConstants.BLOCKED);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Use this to unblock or delete friend. Set deleteFriend to true if deleting, false if unblocking
	 * @param userA
	 * @param userB
	 * @param con
	 */
	public static void unblockOrDelete(User userA, User userB, SelfRefreshingConnection con, boolean deleteFriend) {
		try {
			long user_a_id = userA.getUserId();
			long user_b_id = userB.getUserId();
			PreparedStatement stmt = con.prepareStatement("DELETE FROM Relations WHERE user_a_id = " + user_a_id + " AND user_b_id = " + user_b_id); 

			stmt.executeUpdate();
			
			// Update other direction
			if (deleteFriend) {
				PreparedStatement stmt2 = con.prepareStatement("DELETE FROM Relations WHERE user_a_id = " + user_b_id + " AND user_b_id = " + user_a_id); 
				
				stmt2.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns "REQ", "BLK", "FRD", or "NTH"
	 * @param userA
	 * @param userB
	 * @param con
	 * @return
	 */
	public static String getStatus(User userA, User userB, SelfRefreshingConnection con) {
		try {
			Statement stmt = con.createStatement();
			String queryStatus = "SELECT status FROM Relations WHERE user_a_id = " + userA.getUserId() + " AND user_b_id = " + userB.getUserId();
			ResultSet rs = stmt.executeQuery(queryStatus);
			rs.next();
			return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return "Error";
	}
	
	public static ArrayList<User> getAllRequestsOutward(User user, SelfRefreshingConnection con) {
		try {
			Statement stmt = con.createStatement();
			String queryStatus = "SELECT user_b_id FROM Relations WHERE user_a_id = " + user.getUserId() + " AND status = \"" + RelationConstants.FRIEND_REQUESTED + "\"";
			ResultSet rs = stmt.executeQuery(queryStatus);
			
			ArrayList<User> users = new ArrayList<User>();
			while (rs.next()) {
				long user_b_id = rs.getLong(1);
				users.add(new User(user_b_id, con));
			}
			return users;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<User> getAllRequestsInward(User user, SelfRefreshingConnection con) {
		try {
			Statement stmt = con.createStatement();
			String queryStatus = "SELECT user_a_id FROM Relations WHERE user_b_id = " + user.getUserId() + " AND status = \"" + RelationConstants.FRIEND_REQUESTED + "\"";
			ResultSet rs = stmt.executeQuery(queryStatus);
			
			ArrayList<User> users = new ArrayList<User>();
			while (rs.next()) {
				long user_a_id = rs.getLong(1);
				users.add(new User(user_a_id, con));
			}
			return users;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<User> getAllFriends(User user, SelfRefreshingConnection con) {
		try {
			Statement stmt = con.createStatement();
			String queryStatus = "SELECT user_b_id FROM Relations WHERE user_a_id = " + user.getUserId() + " AND status = \"" + RelationConstants.IS_FRIEND + "\"";
			ResultSet rs = stmt.executeQuery(queryStatus);
			
			ArrayList<User> users = new ArrayList<User>();
			while (rs.next()) {
				long user_b_id = rs.getLong(1);
				users.add(new User(user_b_id, con));
			}
			return users;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<User> getAllBlocked(User user, SelfRefreshingConnection con) {
		try {
			Statement stmt = con.createStatement();
			String queryStatus = "SELECT user_b_id FROM Relations WHERE user_a_id = " + user.getUserId() + " AND status = \"" + RelationConstants.BLOCKED + "\"";
			ResultSet rs = stmt.executeQuery(queryStatus);
			
			ArrayList<User> users = new ArrayList<User>();
			while (rs.next()) {
				long user_b_id = rs.getLong(1);
				users.add(new User(user_b_id, con));
			}
			return users;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
