package quiz;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import customObjects.SelfRefreshingConnection;

/**
 * Stores info to be used when displaying quiz profile page.
 * @author marcelp
 *
 */
public class QuizInfo {

	private long quiz_id;
	private SelfRefreshingConnection con;
	
	/**
	 * Simply keeps track of each of these and has getters for each.
	 * @param quiz_id
	 * @param connection
	 */
	public QuizInfo(long quiz_id, SelfRefreshingConnection con) {
		this.quiz_id = quiz_id;
		this.con = con;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		try {
			Statement stmt = con.createStatement();
			String getNameQuery = "SELECT description FROM Quizzes WHERE id = \"" + this.quiz_id + "\"";
			ResultSet rs = stmt.executeQuery(getNameQuery);
			rs.next();
			return rs.getString("description");
		}
		catch (Exception e) { e.printStackTrace(); return null; }
	}

	public String getQuizName() {
		try {
			Statement stmt = con.createStatement();
			String getNameQuery = "SELECT name FROM Quizzes WHERE id = \"" + this.quiz_id + "\"";
			ResultSet rs = stmt.executeQuery(getNameQuery);
			rs.next();
			return rs.getString("name");
		}
		catch (Exception e) { e.printStackTrace(); return null; }
	}
	
	public boolean isPracticable() {
		try {
			Statement stmt = con.createStatement();
			String getNameQuery = "SELECT is_practicable FROM Quizzes WHERE id = \"" + this.quiz_id + "\"";
			ResultSet rs = stmt.executeQuery(getNameQuery);
			rs.next();
			return rs.getBoolean("is_practicable");
		}
		catch (Exception e) { e.printStackTrace(); return false; }
		
	}
	/**
	 * @return the usernameOfCreator
	 */
	public String getUsernameOfCreator() {
		try {
			Statement stmt = con.createStatement();
			String getNameQuery = "SELECT creator FROM Quizzes WHERE id = \"" + this.quiz_id + "\"";
			ResultSet rs = stmt.executeQuery(getNameQuery);
			rs.next();
			return rs.getString("creator");
		}
		catch (Exception e) { e.printStackTrace(); return null; }
	}

	/**
	 * @return the bestScoresAllTime
	 */
	public ArrayList<QuizResults> getBestScoresAllTime() {
		try {
			Statement stmt = con.createStatement();
			String getTop5TodayQuery = "select id, user_id, quiz_id" +
			" from QuizResults where quiz_id = " + this.quiz_id +
			" ORDER BY user_percentage_score desc, time_duration asc " +
			"LIMIT 5";			
			ResultSet rs = stmt.executeQuery(getTop5TodayQuery);
			
			ArrayList<QuizResults> top5AllTime = new ArrayList<QuizResults>();
			
			while(rs.next()) {
				top5AllTime.add(new QuizResults(rs.getLong("id"), rs.getLong("user_id"), rs.getLong("quiz_id"), this.con));
			}
			
			return top5AllTime;
		}
		catch (Exception e) { e.printStackTrace(); return null; }
	}

	/**
	 * @return the bestScoresToday
	 */
	public ArrayList<QuizResults> getBestScoresToday() {
		
		try {
			Statement stmt = con.createStatement();
			String getTop5TodayQuery = "select id, user_id, quiz_id" +
			" from QuizResults where quiz_id = " + this.quiz_id + " AND " +
			"DATEDIFF(NOW(), date_taken) <= 1 " +
			"ORDER BY user_percentage_score desc, time_duration asc " +
			"LIMIT 5";			
			ResultSet rs = stmt.executeQuery(getTop5TodayQuery);
			
			ArrayList<QuizResults> top5TodayResults = new ArrayList<QuizResults>();
			
			while(rs.next()) {
				top5TodayResults.add(new QuizResults(rs.getLong("id"), rs.getLong("user_id"), rs.getLong("quiz_id"), this.con));
			}
			
			return top5TodayResults;
		}
		catch (Exception e) { e.printStackTrace(); return null; }
		
	}

	/**
	 * @return the recentScores
	 */
	public ArrayList<QuizResults> getRecentScores() {
		try {
			Statement stmt = con.createStatement();
			String getTop5TodayQuery = "select id, user_id, quiz_id" +
			" from QuizResults where quiz_id = " + this.quiz_id +
			" ORDER BY date_taken desc " +
			"LIMIT 5";			
			ResultSet rs = stmt.executeQuery(getTop5TodayQuery);
			
			ArrayList<QuizResults> mostRecentResults = new ArrayList<QuizResults>();
			
			while(rs.next()) {
				mostRecentResults.add(new QuizResults(rs.getLong("id"), rs.getLong("user_id"), rs.getLong("quiz_id"), this.con));
			}
			
			return mostRecentResults;
		}
		catch (Exception e) { e.printStackTrace(); return null; }
	}
	
	public ArrayList<QuizResults> getUserHistory(long userID) {
		try {
			Statement stmt = con.createStatement();
			String getUserHistory = "select id, user_id, quiz_id" +
			" from QuizResults where quiz_id = " + this.quiz_id + " AND user_id = " + userID +
			" ORDER BY date_taken desc " +
			"LIMIT 5";			
			ResultSet rs = stmt.executeQuery(getUserHistory);
			
			ArrayList<QuizResults> mostRecentResults = new ArrayList<QuizResults>();
			
			while(rs.next()) {
				mostRecentResults.add(new QuizResults(rs.getLong("id"), rs.getLong("user_id"), rs.getLong("quiz_id"), this.con));
			}
			
			return mostRecentResults;
		}
		catch (Exception e) { e.printStackTrace(); return null; }
	}

	/**
	 * @return the averageScore
	 */
	public double getAverageScore() {
		try {
			Statement stmt = con.createStatement();
			String getAverageQuery = "select avg(user_percentage_score) from QuizResults where quiz_id = " + this.quiz_id;		
			ResultSet rs = stmt.executeQuery(getAverageQuery);			
			rs.next();
			return rs.getDouble(1);
		}
		catch (Exception e) { e.printStackTrace(); return -1; }
	}

}
