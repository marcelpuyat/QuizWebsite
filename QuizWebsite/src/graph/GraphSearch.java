package graph;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;

import customObjects.SelfRefreshingConnection;

public class GraphSearch {
	
	public static JSONObject simple_search(SelfRefreshingConnection db_connection, String text, int limit) throws ClassNotFoundException {
		int total_results = 10;
		int quiz_results = 5;
		
		JSONObject results = new JSONObject();
		try {
			Set<Integer> reapedQuizzes = new HashSet<Integer>();
			
			/* first search quizzes by name prefix */
			String getNameQuery = "SELECT name,id FROM Quizzes WHERE UPPER(name) LIKE UPPER(?) LIMIT 0, ?";
			PreparedStatement pstmt = db_connection.prepareStatement(getNameQuery);
			pstmt.setString(1, text);
			pstmt.setInt(2, min(quiz_results,total_results));
			ResultSet rs = pstmt.executeQuery();
			int quizes_reaped = 0;
			while(rs.next()) {
				JSONObject entry = new JSONObject();
				entry.accumulate("name", rs.getString("name"));
				int id = rs.getInt("id");
				entry.accumulate("id", id);
				entry.accumulate("type", "QUIZ");
				entry.accumulate("url", "/QuizWebsite/QuizPage.jsp?quiz_id="+id);
				results.append("results", entry);
				reapedQuizzes.add(id);
				quizes_reaped++;
			}
			if (quizes_reaped < limit) {
				/* then search quiz by name suffix */
				Statement stmt = db_connection.createStatement();
				rs = stmt.executeQuery("SELECT name,id FROM Quizzes WHERE UPPER(name) LIKE UPPER(\"%" + text + "%\") LIMIT 0, "+(limit - quizes_reaped));
				while(rs.next()) {
					int id = rs.getInt("id");
					if (!reapedQuizzes.contains(id)) {
						JSONObject entry = new JSONObject();
						entry.accumulate("id", id);
						entry.accumulate("name", rs.getString("name"));
						entry.accumulate("type", "QUIZ");
						entry.accumulate("url", "/QuizWebsite/QuizPage.jsp?quiz_id="+id);
						results.append("results", entry);
						reapedQuizzes.add(id);
					}
				}
			}
			
			/* Search by tag */
			try {
				Statement stmt2 = db_connection.createStatement();
				String tagMatchString = "SELECT Quizzes.name, Quizzes.id, Tags.name FROM Quizzes INNER JOIN Tags on Quizzes.id = Tags.quiz_id AND Tags.name LIKE UPPER(\"" + text + "%\")";
				ResultSet rs2 = stmt2.executeQuery(tagMatchString);
				
				while (rs2.next()) {
					int id = rs2.getInt("id");
					String tag_name = rs2.getString("Tags.name");
					if (!reapedQuizzes.contains(id)) {
						JSONObject entry = new JSONObject();
						entry.accumulate("id", id);
						entry.accumulate("name", rs2.getString("name"));
						entry.accumulate("type", tag_name);
						entry.accumulate("url", "/QuizWebsite/QuizPage.jsp?quiz_id="+id);
						results.append("results", entry);
						reapedQuizzes.add(id);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			/* End of search by tag */
			
			results.accumulate("status", 200);
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
		catch(java.lang.NullPointerException e) {
			e.printStackTrace(); 
		} finally {}
		return results;
	}
	
	private static int min(int a, int b) {
		if (a < b) return a;
		return b;
	}
}
