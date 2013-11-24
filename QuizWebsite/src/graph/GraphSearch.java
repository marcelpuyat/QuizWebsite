package graph;

import java.sql.*;
import java.util.*;

import org.json.*;

public class GraphSearch {
	
	public static JSONObject simple_search(Connection db_connection, String text, int limit) {
		JSONObject results = new JSONObject();
		try {
			Set<Integer> reapedQuizzes = new HashSet<Integer>();
			
			/* first search quizzes by name prefix */
			Statement stmt = db_connection.createStatement();
			String getNameQuery = "SELECT name,id FROM Quizzes WHERE UPPER(name) LIKE UPPER(\"" + text + "%\") LIMIT 0, "+limit;
			ResultSet rs = stmt.executeQuery(getNameQuery);
			int quizes_reaped = 0;
			while(rs.next()) {
				JSONObject entry = new JSONObject();
				entry.accumulate("name", rs.getString("name"));
				int id = rs.getInt("id");
				entry.accumulate("id", id);
				entry.accumulate("type", "QUIZ");
				entry.accumulate("url", "/QuizWebsite/Quiz.jsp?quiz_id="+id);
				results.append("results", entry);
				reapedQuizzes.add(id);
				quizes_reaped++;
			}
			if (quizes_reaped < limit) {
				/* then search quiz by name suffix */
				rs = stmt.executeQuery("SELECT name,id FROM Quizzes WHERE UPPER(name) LIKE UPPER(\"%" + text + "%\") LIMIT 0, "+(limit - quizes_reaped));
				while(rs.next()) {
					int id = rs.getInt("id");
					if (!reapedQuizzes.contains(id)) {
						JSONObject entry = new JSONObject();
						entry.accumulate("id", id);
						entry.accumulate("name", rs.getString("name"));
						entry.accumulate("type", "QUIZ");
						entry.accumulate("url", "/QuizWebsite/Quiz.jsp?quiz_id="+id);
						results.append("results", entry);
						reapedQuizzes.add(id);
					}
				}
			}
			results.accumulate("status", 200);
		} catch (SQLException e) {
			return null;
		}
		catch(java.lang.NullPointerException e) {
			return null;
		}
		return results;
	}
	
	
}
