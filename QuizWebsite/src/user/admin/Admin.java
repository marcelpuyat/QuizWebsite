package user.admin;

import java.sql.SQLException;
import java.sql.Statement;
import customObjects.SelfRefreshingConnection;

public class Admin {
	
	// Deletes all scores on a particular quiz
	public static boolean clearHistory(long quiz_id, SelfRefreshingConnection con) throws ClassNotFoundException{
		try {
			Statement stmt = con.createStatement();
			String deleteHistory =  "DELETE FROM QuizResults WHERE quiz_id = '"+quiz_id+"'";
			stmt.execute(deleteHistory);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean removeUser(long user_id, SelfRefreshingConnection con) throws ClassNotFoundException{
		try {
			Statement stmt = con.createStatement();
			String deleteUser = "DELETE FROM Users WHERE id = '"+user_id+"'";
			stmt.execute(deleteUser);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean removeQuiz(long quiz_id, SelfRefreshingConnection con) throws ClassNotFoundException{
		try {
			Statement stmt = con.createStatement();
			String deleteQuiz = "DELETE FROM Quizzes WHERE id = '"+quiz_id+"'";
			stmt.execute(deleteQuiz);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public static boolean promoteAccount(long user_id, SelfRefreshingConnection con) throws ClassNotFoundException{
		try {
			Statement stmt = con.createStatement();
			String promote = "UPDATE Users SET is_admin = '"+true+"' WHERE id = '"+user_id+"'";
			stmt.execute(promote);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
