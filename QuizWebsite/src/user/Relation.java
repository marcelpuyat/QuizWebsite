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
	 * Both will be friends with each other after this is used
	 * @param userA 
	 * @param userB
	 * @param con
	 */
	public static void makeFriends(User userA, User userB, SelfRefreshingConnection con) {
		try {
			PreparedStatement stmt = con.prepareStatement("INSERT INTO Relations (user_a_id, user_b_id, status) VALUES (?, ?, ?)");
			long user_a_id = userA.getUserId();
			long user_b_id = userB.getUserId();
			stmt.setLong(1, user_a_id);
			stmt.setLong(2, user_b_id);
			stmt.setString(3, RelationConstants.IS_FRIEND);
			stmt.executeUpdate();
			
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
	 * Use this to unblock or delete friend
	 * @param userA
	 * @param userB
	 * @param con
	 */
	public static void unblockOrDelete(User userA, User userB, SelfRefreshingConnection con) {
		try {
			PreparedStatement stmt = con.prepareStatement("INSERT INTO Relations (user_a_id, user_b_id, status) VALUES (?, ?, ?)");
			long user_a_id = userA.getUserId();
			long user_b_id = userB.getUserId();
			stmt.setLong(1, user_a_id);
			stmt.setLong(2, user_b_id);
			stmt.setString(3, RelationConstants.NOTHING);
			stmt.executeUpdate();
			
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
		// TODO
		return null;
	}
	
	public static ArrayList<User> getAllRequestsInward(User user, SelfRefreshingConnection con) {
		// TODO
		return null;
	}
	
	public static ArrayList<User> getAllFriends(User user, SelfRefreshingConnection con) {
		// TODO
		return null;
	}
	
	public static ArrayList<User> getAllBlocked(User user, SelfRefreshingConnection con) {
		// TODO
		return null;
	}
}
