package user.achievement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import user.User;
import customObjects.SelfRefreshingConnection;

public class Achievement {

	private long id;
	private long user_id;
	private SelfRefreshingConnection con;
	public static final int AUTO_INCREMENT_OFFSET = 1;
	public static final int TITLE = 0;
	public static final int ACHIEVEMENT = 1;
	
	public static final String ONE_QUIZ_ACHV = AchievementConstants.achievements[0][TITLE];
	public static final String ONE_QUIZ_ACHV_DESC = AchievementConstants.achievements[0][ACHIEVEMENT];

	public static final String FIVE_QUIZ_ACHV = AchievementConstants.achievements[1][TITLE];
	public static final String FIVE_QUIZ_ACHV_DESC = AchievementConstants.achievements[1][ACHIEVEMENT];

	public static final String TEN_QUIZ_ACHV = AchievementConstants.achievements[2][TITLE];
	public static final String TEN_QUIZ_ACHV_DESC = AchievementConstants.achievements[2][ACHIEVEMENT];

	public static final String TEN_QUIZ_TAKEN_ACHV = AchievementConstants.achievements[3][TITLE];
	public static final String TEN_QUIZ_TAKEN_ACHV_DESC = AchievementConstants.achievements[3][ACHIEVEMENT];
	
	public static final String HIGH_SCORE_ACHV = AchievementConstants.achievements[4][TITLE];
	public static final String HIGH_SCORE_ACHV_DESC = AchievementConstants.achievements[4][ACHIEVEMENT];

	public static final String PRACTICE_ACHV = AchievementConstants.achievements[5][TITLE];
	public static final String PRACTICE_ACHV_DESC = AchievementConstants.achievements[5][ACHIEVEMENT];



	
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
	 * Use this to add a new achievement to a user
	 * @param title
	 * @param description
	 * @param user_id
	 * @param con
	 */
	public static void addAchievement(String title, String description, long user_id, SelfRefreshingConnection con) {
		try {
			PreparedStatement stmt = con.prepareStatement("INSERT INTO Achievements VALUES(id, ?, ?, ?)");
			stmt.setLong(1, user_id);
			stmt.setString(2, title);
			stmt.setString(3, description);
			stmt.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
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
			return nextID + AUTO_INCREMENT_OFFSET;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static ArrayList<Achievement> getUserAchievements(SelfRefreshingConnection con, long user_id) {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT id FROM Achievements WHERE user_id = " + user_id);
			ResultSet rs = stmt.executeQuery();
			
			ArrayList<Achievement> achievements = new ArrayList<Achievement>();
			while (rs.next()) {
				achievements.add(new Achievement(rs.getLong(1), user_id, con));
			}
			return achievements;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static JSONArray getUserAchievementsNotEarned(SelfRefreshingConnection con, long user_id) {
		ArrayList<Achievement> earned = getUserAchievements(con, user_id);
		
		JSONArray notEarned = new JSONArray();
		
		for (int i = 0; i < AchievementConstants.achievements.length; i++) {
			boolean found = false;
			for (int j = 0; j < earned.size(); j++) {
				if (earned.get(j).getTitle().equals(AchievementConstants.achievements[i][0])) found = true;
			}
			if (found) continue;
			JSONObject achv = new JSONObject();
			achv.put("title", AchievementConstants.achievements[i][0]);
			achv.put("description", AchievementConstants.achievements[i][1]);
			notEarned.put(achv);
		}
		return notEarned;
	}
	
	public static void updateQuizCreatedAchievements(SelfRefreshingConnection con, long user_id) {
		User user = new User(user_id, con);
		int quizzesCreated = user.getNumQuizzesCreated();
		
		if (!User.hasAchievement(ONE_QUIZ_ACHV, user, con)) {
			if (quizzesCreated >= 1) {
				Achievement.addAchievement(ONE_QUIZ_ACHV, ONE_QUIZ_ACHV_DESC, user_id, con);
			}
		}
		
		if (!User.hasAchievement(FIVE_QUIZ_ACHV, user, con)) {
			if (quizzesCreated >= 5) {
				Achievement.addAchievement(FIVE_QUIZ_ACHV, FIVE_QUIZ_ACHV_DESC, user_id, con);
			}
		}
		
		if (!User.hasAchievement(TEN_QUIZ_ACHV, user, con)) {
			if (quizzesCreated >= 10) {
				Achievement.addAchievement(TEN_QUIZ_ACHV, TEN_QUIZ_ACHV_DESC, user_id, con);
			}
		}
		
	}
	
	public static void updateQuizzesTakenAchievement(SelfRefreshingConnection con, long user_id) {
		User user = new User(user_id, con);
		int quizzesTaken = user.getNumQuizzesTaken();
		
		if (!User.hasAchievement(TEN_QUIZ_TAKEN_ACHV, user, con)) {
			if (quizzesTaken >= 10) {
				Achievement.addAchievement(TEN_QUIZ_TAKEN_ACHV, TEN_QUIZ_TAKEN_ACHV_DESC, user_id, con);
			}
		}
	}
	
	public static void updateHighScorerAchievement(SelfRefreshingConnection con, long user_id) {
		User user = new User(user_id, con);
		ArrayList<Integer> quiz_ids = getIDsOfQuizzes(con);
		boolean hasHighScore = hasHighScore(user_id, con, quiz_ids);
		if (hasHighScore) {
			
			if (!User.hasAchievement(HIGH_SCORE_ACHV, user, con)) {
				Achievement.addAchievement(HIGH_SCORE_ACHV, HIGH_SCORE_ACHV_DESC, user_id, con);
			}
			
		}
	}
	
	private static boolean hasHighScore(long user_id, SelfRefreshingConnection con, ArrayList<Integer> ids) {
		
		for (int i = 0; i < ids.size(); i++) {
			
			int quiz_id = ids.get(i);
			
			try {
				PreparedStatement stmt = con.prepareStatement("SELECT user_id FROM QuizResults WHERE quiz_id = " + quiz_id + " ORDER BY user_percentage_score DESC, time_duration LIMIT 1");
				ResultSet rs = stmt.executeQuery();
				rs.next();
				if (rs.getInt(1) == (int)user_id) return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			
		}
		return false;
	}
	
	private static ArrayList<Integer> getIDsOfQuizzes(SelfRefreshingConnection con) {
		try {
			ArrayList<Integer> ids = new ArrayList<Integer>();
			PreparedStatement stmt = con.prepareStatement("SELECT id FROM Quizzes");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ids.add(new Integer(rs.getInt(1)));
			}
			return ids;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
