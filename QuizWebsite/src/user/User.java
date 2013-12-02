package user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import user.achievement.Achievement;
import customObjects.CustomDate;
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
	
	public int getNumQuizzesCreated() {
		int count = 0;
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT id FROM Quizzes WHERE creator = \"" + this.getUserName() + "\"");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) count++;
			return count;
		} catch (Exception e) { e.printStackTrace(); return -1;}
	}
	
	public int getNumQuizzesTaken() {
		int count = 0;
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT id FROM QuizResults WHERE user_id = " + this.user_id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) count++;
			return count;
		} catch (Exception e) { e.printStackTrace(); return -1;}
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
	
	public JSONArray getRecentQuizResultsInJSONArray() {
		try {
			String get5MostRecentQuizTakingInstances = "SELECT Quizzes.name, Quizzes.id, QuizResults.date_taken, QuizResults.time_duration, QuizResults.user_percentage_score " +
					"FROM Quizzes INNER JOIN QuizResults ON QuizResults.quiz_id = Quizzes.id WHERE QuizResults.user_id = " + this.user_id + " ORDER BY QuizResults.date_taken DESC LIMIT 5";
			PreparedStatement stmt = con.prepareStatement(get5MostRecentQuizTakingInstances);
			ResultSet rs = stmt.executeQuery();
			
			JSONArray resultsArray = new JSONArray();
			while (rs.next()) {
				JSONObject result = new JSONObject();
				result.put("quiz_name", rs.getString(1));
				result.put("quiz_id", rs.getLong(2));
				
				JSONObject date = new JSONObject();
					Timestamp ts = rs.getTimestamp(3);
					Calendar dateTaken = Calendar.getInstance();
					dateTaken.setTimeInMillis(ts.getTime());
					int year = dateTaken.get(Calendar.YEAR);
					int month = dateTaken.get(Calendar.MONTH) + 1; // MONTHS ARE INDEXED FROM 0!
					int day = dateTaken.get(Calendar.DATE);
					date.put("year", year);
					date.put("month", month);
					date.put("date", day);
				
				result.put("date", date);
				result.put("time_taken", rs.getDouble(4));
				result.put("user_percentage_score", rs.getDouble(5));
				resultsArray.put(result);
			}
			
			return resultsArray;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public JSONArray getCreatedQuizzes() {
		JSONArray createdQuizzes = new JSONArray();
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT name, id FROM Quizzes WHERE creator = \"" + this.getUserName() + "\"");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				JSONObject quiz = new JSONObject();
				quiz.put("quiz_name", rs.getString(1));
				quiz.put("quiz_id", rs.getLong(2));
				createdQuizzes.put(quiz);
			}
			return createdQuizzes;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
				resultJSON.accumulate("quizzes_created", this.getNumQuizzesCreated());
				resultJSON.accumulate("quizzes_taken", this.getNumQuizzesTaken());
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
	
	public JSONArray getFriendsLatestResults() {		
		String get5MostRecentFriendResults = "SELECT Users.id AS user_id, QuizResults.date_taken, Quizzes.name AS quiz_name, Quizzes.id AS quiz_id, QuizResults.user_percentage_score," +
				" Users.username FROM QuizResults INNER JOIN Users INNER JOIN Quizzes INNER JOIN Relations WHERE" +
				" Relations.user_a_id = " + this.user_id + " AND Relations.status = \"FRD\" AND Relations.user_b_id = Users.id AND Quizzes.id" +
				" = QuizResults.quiz_id AND QuizResults.user_id = Users.id ORDER BY date_taken DESC LIMIT 5";
		
		try {
			PreparedStatement stmt = con.prepareStatement(get5MostRecentFriendResults);
			ResultSet rs = stmt.executeQuery();
			JSONArray results = new JSONArray();
			
			while (rs.next()) {
				JSONObject result = new JSONObject();
				User user = new User(rs.getLong("user_id"), con);
				result.put("quiz_name", rs.getString("quiz_name"));
				result.put("quiz_id", rs.getLong("quiz_id"));
				result.put("user_percentage_score", rs.getDouble("user_percentage_score"));
					Calendar dateTakenCalendar = Calendar.getInstance();
					Timestamp ts = rs.getTimestamp("date_taken");
					dateTakenCalendar.setTimeInMillis(ts.getTime());
					CustomDate dateTaken = new CustomDate(dateTakenCalendar);
					JSONObject date = dateTaken.toJSON();
				result.put("date", date);
				result.put("user", user.getPublicJSONSummary());
				results.put(result);
			}
			
			return results;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
	
	/* Achievement Related */
	
	/**
	 * Return all achievements associated with this user
	 * @return ArrayList of achievement objects
	 */
	public ArrayList<Achievement> getAllAchievements() {
		try {
			Statement stmt = con.createStatement();
			String achievementsQuery = "SELECT id, user_id FROM Achievements WHERE user_id = " + user_id;
			ResultSet rs = stmt.executeQuery(achievementsQuery);
			
			ArrayList<Achievement> achievements = new ArrayList<Achievement>();
			
			while (rs.next()) {
				long curr_user_id = rs.getLong("user_id");
				long id = rs.getLong("id");
				achievements.add(new Achievement(id, curr_user_id, con));
			}
			
			return achievements;
		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}
	
	/**
	 * Returns boolean of whether passed in User has the achievement of that id (compared by title)
	 * @param achievement_id
	 * @param user
	 * @param con
	 * @return
	 */
	public static boolean hasAchievement(long achievement_id, User user, SelfRefreshingConnection con) {
		try {
			Statement stmt = con.createStatement();
			String title = Achievement.getTitleForAchievementID(con, achievement_id);
			String query = "SELECT Achievements.id FROM Achievements INNER JOIN Users ON Achievements.title = \"" + title + "\" AND Achievements.user_id = Users.id";
			ResultSet rs = stmt.executeQuery(query);
			boolean searchEmpty = true;
			
			if (rs.next()) searchEmpty = false;
			
			return (!searchEmpty);
		} catch (Exception e) { e.printStackTrace(); return false; }
	}
	
	/**
	 * Returns boolean of whether passed in user has the achievement of that title
	 * @param title
	 * @param user
	 * @param con
	 * @return
	 */
	public static boolean hasAchievement(String title, User user, SelfRefreshingConnection con) {
		try {
			Statement stmt = con.createStatement();
			String query = "SELECT Achievements.id FROM Achievements INNER JOIN Users ON Achievements.title = \"" + title + "\" AND Achievements.user_id = " + user.getUserId();
			ResultSet rs = stmt.executeQuery(query);
			boolean searchEmpty = true;
			
			if (rs.next()) searchEmpty = false;
			
			return (!searchEmpty);
		} catch (Exception e) { e.printStackTrace(); return false; }
	}
	
	/* End of achievement methods */
	
	
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
