package user.history;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import quiz.QuizResults;
import customObjects.SelfRefreshingConnection;

public class History {

	/**
	 * Returns all results for all quizzes for user, ordered by date (0th index is latest quiz-taking instance)
	 * @param con
	 * @param user_id
	 * @return
	 */
	public static ArrayList<QuizResults> getUserHistory(SelfRefreshingConnection con, long user_id) {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT id, quiz_id FROM QuizResults WHERE user_id = " + user_id + " ORDER BY date_taken DESC");
			ResultSet rs = stmt.executeQuery();
			
			ArrayList<QuizResults> allResults = new ArrayList<QuizResults>();
			while (rs.next()) {
				long thisID = rs.getLong("id");
				long quizID = rs.getLong("quiz_id");
				QuizResults thisResult = new QuizResults(thisID, user_id, quizID, con);
				allResults.add(thisResult);
			}
			return allResults;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
