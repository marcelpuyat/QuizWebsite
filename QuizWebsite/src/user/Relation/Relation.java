package user.Relation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import user.User;

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
			
			long user_a_id = userA.getUserId();
			long user_b_id = userB.getUserId();
			
			PreparedStatement stmt;
			
			// If no relationship exists yet, insert
			
			if (Relation.getStatus(userA, userB, con).equals(RelationConstants.NOTHING)) {
				stmt = con.prepareStatement("INSERT INTO Relations (user_a_id, user_b_id, status) VALUES (?, ?, ?)");
				stmt.setLong(1, user_a_id);
				stmt.setLong(2, user_b_id);
				stmt.setString(3, RelationConstants.BLOCKED);
			}
			
			// else, update
			else {
				stmt = con.prepareStatement("UPDATE Relations SET status = \"" + RelationConstants.BLOCKED + "\" WHERE user_a_id = " + user_a_id + " AND user_b_id = " + user_b_id);
			}
			
			stmt.executeUpdate();

			// Delete relationship from other direction
			PreparedStatement stmt2 = con.prepareStatement("DELETE FROM Relations WHERE user_a_id = " + user_b_id + " AND user_b_id = " + user_a_id);
			stmt2.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void rejectRequest(User userA, User userB, SelfRefreshingConnection con) {
		deleteFriend(userA, userB, con);
	}
	
	/**
	 * Unblock user b
	 * @param userA
	 * @param userB
	 * @param con
	 */
	public static void unblockUser(User userA, User userB, SelfRefreshingConnection con) {
		unblockOrDelete(userA, userB, con, false);
	}
	
	/**
	 * Delete user b as friend of user a
	 * @param userA
	 * @param userB
	 * @param con
	 */
	public static void deleteFriend(User userA, User userB, SelfRefreshingConnection con) {
		unblockOrDelete(userA, userB, con, true);
	}
	
	/**
	 * Use this to unblock or delete friend. Set deleteFriend to true if deleting, false if unblocking
	 * @param userA
	 * @param userB
	 * @param con
	 */
	private static void unblockOrDelete(User userA, User userB, SelfRefreshingConnection con, boolean deleteFriend) {
		try {
			long user_a_id = userA.getUserId();
			long user_b_id = userB.getUserId();
			PreparedStatement stmt = con.prepareStatement("DELETE FROM Relations WHERE user_a_id = " + user_a_id + " AND user_b_id = " + user_b_id); 

			// Remove request from opposite direction
			if (!deleteFriend) {
				PreparedStatement stmt2 = con.prepareStatement("DELETE FROM Relations WHERE user_a_id = " + user_b_id + " AND user_b_id = " + user_a_id + " AND status = \"" + RelationConstants.FRIEND_REQUESTED + "\""); 
				stmt2.executeUpdate();
			}
			
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
			if (rs.next()) {
				return rs.getString(1);
			} else return RelationConstants.NOTHING;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return "Error";
	}
	
	/**
	 * Return list of users that argument-user has requested frienship with
	 * @param user
	 * @param con
	 * @return
	 */
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
	
	/**
	 * Return list of users that have requested friendship with argument-user
	 * @param user
	 * @param con
	 * @return
	 */
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
	
	/**
	 * Return list of friends of argument-user
	 * @param user
	 * @param con
	 * @return
	 */
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
	
	/**
	 * Return list of users that argument-user has blocked
	 * @param user
	 * @param con
	 * @return
	 */
	public static ArrayList<User> getAllBlockedOutward(User user, SelfRefreshingConnection con) {
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
	
	/**
	 * Return list of users that have blocked argument-user
	 * @param user
	 * @param con
	 * @return
	 */
	public static ArrayList<User> getAllBlockedInward(User user, SelfRefreshingConnection con) {
		try {
			Statement stmt = con.createStatement();
			String queryStatus = "SELECT user_a_id FROM Relations WHERE user_b_id = " + user.getUserId() + " AND status = \"" + RelationConstants.BLOCKED + "\"";
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
