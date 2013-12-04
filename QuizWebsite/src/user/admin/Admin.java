package user.admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.JSONObject;

import customObjects.SelfRefreshingConnection;

public class Admin {
	
	// Deletes all scores on a particular quiz
	public static boolean clearHistory(long quiz_id, SelfRefreshingConnection con) {
		try {
			Statement stmt = con.createStatement();
			String deleteHistory =  "DELETE FROM QuizResults WHERE quiz_id = '"+quiz_id+"'";
			stmt.execute(deleteHistory);
			Statement stmt2 = con.createStatement();
			String deleteFrequency = "UPDATE Quizzes SET frequency = 0 WHERE id = " + quiz_id;
			stmt2.executeUpdate(deleteFrequency);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean removeUser(long user_id, SelfRefreshingConnection con) {
		try {
			Statement stmt = con.createStatement();
			String deleteUser = "DELETE FROM Users WHERE id = '"+user_id+"'";
			stmt.execute(deleteUser);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean removeQuiz(long quiz_id, SelfRefreshingConnection con) {
		try {
			Statement stmt = con.createStatement();
			String deleteQuiz = "DELETE FROM Quizzes WHERE id = '"+quiz_id+"'";
			stmt.execute(deleteQuiz);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public static boolean promoteAccount(long user_id, SelfRefreshingConnection con) {
		try {
			Statement stmt = con.createStatement();
			String promote = "UPDATE Users SET is_admin = TRUE WHERE id = '"+user_id+"'";
			stmt.execute(promote);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static JSONObject getSiteStatistics(SelfRefreshingConnection con) {
		JSONObject stats = new JSONObject();
		stats.put("num_quizzes_made", countNumQuizzesMade(con));
		stats.put("num_users", countNumUsers(con));
		stats.put("num_quizzes_taken", countNumQuizzesTaken(con));
		return stats;
	}
	
	private static int countNumQuizzesMade(SelfRefreshingConnection con) {
		return countResultsFromTable("Quizzes", con);
	}
	
	private static int countNumUsers(SelfRefreshingConnection con) {
		return countResultsFromTable("Users", con);
	}
	
	private static int countNumQuizzesTaken(SelfRefreshingConnection con) {
		return countResultsFromTable("QuizResults", con);
	}
	
	private static int countResultsFromTable(String tableName, SelfRefreshingConnection con) {
		try {
			int count = 0;
			PreparedStatement stmt = con.prepareStatement("SELECT id FROM " + tableName);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				count++;
			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
