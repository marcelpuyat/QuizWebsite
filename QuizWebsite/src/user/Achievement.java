package user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import customObjects.SelfRefreshingConnection;

public class Achievement {

	private long id;
	private long user_id;
	private SelfRefreshingConnection con;
	public static final int AUTO_INCREMENT_OFFSET = 1;
	
	/**
	 * Use this to add a new achievement to a user
	 * @param title
	 * @param description
	 * @param user_id
	 * @param con
	 */
	public Achievement(String title, String description, long user_id, SelfRefreshingConnection con) {
		this.user_id = user_id;
		this.con = con;
		try {
			this.id = Achievement.getNextAvailableID(con);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Use this to access fields of an achievement with ORM
	 * @param id
	 * @param user_id
	 * @param con
	 */
	public Achievement(long id, long user_id, SelfRefreshingConnection con) {
		this.id = id;
		this.user_id = user_id;
		this.con = con;
	}
	
	/**
	 * Return title using ORM
	 * @return
	 */
	public String getTitle() {
		try {
			Statement stmt = con.createStatement();
			String titleQuery = "SELECT title FROM Achievements WHERE user_id = " + this.user_id + " AND id = " + this.id;
			ResultSet rs = stmt.executeQuery(titleQuery);
			
			rs.next();
			return rs.getString(1);
			
		} catch (Exception e) { e.printStackTrace(); return "SQL Error"; }
	}
	
	/**
	 * Return description using ORM
	 * @return
	 */
	public String getDescription() {
		try {
			Statement stmt = con.createStatement();
			String descriptionQuery = "SELECT description FROM Achievements WHERE user_id = " + this.user_id + " AND id = " + this.id;
			ResultSet rs = stmt.executeQuery(descriptionQuery);
			
			rs.next();
			return rs.getString(1);
			
		} catch (Exception e) { e.printStackTrace(); return "SQL Error"; }
	}

	/**
	 * Gets all users with the same achievement as the id passed in
	 * @param id
	 * @param con
	 * @return
	 */
	public static ArrayList<User> getUsersWithAchievement(long id, SelfRefreshingConnection con) {
		try {
			Statement stmt = con.createStatement();
			String title = Achievement.getTitleForAchievementID(con, id);
			String userQuery = "SELECT Users.id FROM Users INNER JOIN Achievements ON Achievements.title = \"" + title + "\" AND Users.id = Achievements.user_id";
			ResultSet rs = stmt.executeQuery(userQuery);
			
			ArrayList<User> users = new ArrayList<User>();
			
			while (rs.next()) {
				long user_id = rs.getLong("id");
				users.add(new User(user_id, con));
			}
			
			return users;
		} catch (Exception e) { e.printStackTrace(); return null; }
	}
	
	/**
	 * Gets all users with an achievement of that title
	 * @param title
	 * @param con
	 * @return
	 */
	public static ArrayList<User> getUsersWithAchievement(String title, SelfRefreshingConnection con) {
		try {
			Statement stmt = con.createStatement();
			String userQuery = "SELECT Users.id FROM Users INNER JOIN Achievements ON Achievements.title = \"" + title + "\" AND Users.id = Achievements.user_id";
			ResultSet rs = stmt.executeQuery(userQuery);
			
			ArrayList<User> users = new ArrayList<User>();
			
			while (rs.next()) {
				long user_id = rs.getLong("id");
				users.add(new User(user_id, con));
			}
			
			return users;
		} catch (Exception e) { e.printStackTrace(); return null; }
	}
	
	/**
	 * Return title given id
	 * @param con
	 * @param id
	 * @return
	 */
	public static String getTitleForAchievementID(SelfRefreshingConnection con, long id) {
		try {
			Statement stmt = con.createStatement();
			String titleQuery = "SELECT title FROM Achievements WHERE id = " + id;
			ResultSet rs = stmt.executeQuery(titleQuery);
			
			rs.next();
			
			return rs.getString(1);
		} catch (Exception e) { e.printStackTrace(); return "SQL Error"; }
	}
	
	/**
	 * Get id of next achievement
	 * @param con
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static int getNextAvailableID(SelfRefreshingConnection con) throws ClassNotFoundException {
		try {
			Statement stmt = con.createStatement();
			String getNextIDQuery = "SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = 'Achievements' AND table_schema = 'c_cs108_marcelp'";
			ResultSet rs = stmt.executeQuery(getNextIDQuery);
			rs.next();
			int nextID = rs.getInt(1);
			System.out.println(nextID);
			return nextID + AUTO_INCREMENT_OFFSET;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
