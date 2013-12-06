package graph;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import customObjects.SelfRefreshingConnection;

public class GraphSearch {
	
	public static JSONObject tag_search(SelfRefreshingConnection db_connection, String text, int limit) throws ClassNotFoundException {
		System.out.println("tag search: "+text);
		JSONObject results = new JSONObject();
		Set<Integer> reapedQuizzes = new HashSet<Integer>();
		try {
			String[] raw_tags = text.split("#");
			List<String> tags = new ArrayList<String>();
			
			for (int i = 0; i < raw_tags.length; i++) {
				if (raw_tags[i] != null && !raw_tags[i].equals("")) {
					tags.add(raw_tags[i]);
					System.out.println("#tag"+raw_tags[i]);
				}
			}
			
			String tagMatchString = "SELECT Quizzes.name, Quizzes.id, Tags.name FROM Quizzes JOIN Tags on Quizzes.id = Tags.quiz_id ";
			
			boolean enteredLoop = false;
			for (int i = 0; i < tags.size(); i++) {
				enteredLoop = true;
				if (i == 0) tagMatchString+= "AND (";
				if (i != 0) tagMatchString += " OR ";
				tagMatchString += "UPPER(Tags.name) LIKE UPPER(?)";
			}
			
			if (enteredLoop) tagMatchString += ")";
			
			tagMatchString += " ORDER BY avg_rating DESC LIMIT 0, ?";
			System.out.println("Query\n"+tagMatchString);
			PreparedStatement pstmt = db_connection.prepareStatement(tagMatchString);
			
			for (int i = 0; i < tags.size(); i++) {
				pstmt.setString(i + 1, tags.get(i) + "%");
			}
			pstmt.setInt(tags.size() + 1, limit);
			
			
			ResultSet rs2 = pstmt.executeQuery();
			while (rs2.next()) {
				int id = rs2.getInt("id");
				if (reapedQuizzes.contains(id)) continue;
				reapedQuizzes.add(id);
				String tag_name = rs2.getString("Tags.name");
				JSONObject entry = new JSONObject();
				entry.accumulate("id", id);
				entry.accumulate("name", rs2.getString("name"));
				entry.accumulate("type", "#"+tag_name);
				entry.accumulate("category", "tag");
				entry.accumulate("url", "/QuizWebsite/QuizPage.jsp?quiz_id="+id);
				results.append("results", entry);
			}
			results.accumulate("status", 200);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (results.length() == 1) {
			JSONObject emptyEntry = new JSONObject();
			emptyEntry.accumulate("id", 1);
			emptyEntry.accumulate("name", "");
			emptyEntry.accumulate("type", "No results found");
			emptyEntry.accumulate("category", "quiz");
			emptyEntry.accumulate("url", "");
			results.append("results", emptyEntry);
		}
		
		return results;
	}
	
	public static JSONObject simple_search(SelfRefreshingConnection db_connection, String text, int limit) throws ClassNotFoundException {
		int total_results = 10;
		int quiz_results = 5;
		int user_results = 5;
		
		JSONObject results = new JSONObject();
		if (text.isEmpty()) return results;
		try {
			Set<Integer> reapedQuizzes = new HashSet<Integer>();
			Set<Integer> reapedUsers = new HashSet<Integer>();

			/* first search quizzes by name prefix */
			String getNameQuery = "SELECT name,id FROM Quizzes WHERE UPPER(name) LIKE UPPER(?)  ORDER BY avg_rating DESC LIMIT 0, ?";
			PreparedStatement pstmt = db_connection.prepareStatement(getNameQuery);
			pstmt.setString(1, text+"%");
			pstmt.setInt(2, min(quiz_results,total_results));
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				JSONObject entry = new JSONObject();
				entry.accumulate("name", rs.getString("name"));
				int id = rs.getInt("id");
				entry.accumulate("id", id);
				entry.accumulate("type", "QUIZ");
				entry.accumulate("category", "quiz");
				entry.accumulate("url", "/QuizWebsite/QuizPage.jsp?quiz_id="+id);
				results.append("results", entry);
				reapedQuizzes.add(id);
				total_results--;
				quiz_results--;
			}
			
			/* Search by user */
			
			String userMatchString = "SELECT * FROM Users WHERE first_name LIKE UPPER(?) OR last_name LIKE UPPER(?) LIMIT ?";
			pstmt = db_connection.prepareStatement(userMatchString);
			pstmt.setString(1, text + "%");
			pstmt.setString(2, text + "%");
			pstmt.setInt(3, min(user_results,total_results));
			
			ResultSet rs3 = pstmt.executeQuery();
			while (rs3.next()) {
				int id = rs3.getInt("id");
				String display_name = rs3.getString("first_name") + " " + rs3.getString("last_name");
				if (!reapedUsers.contains(id)) {
					JSONObject entry = new JSONObject();
					entry.accumulate("id", id);
					entry.accumulate("name", display_name);
					entry.accumulate("category", "user");
					if (rs3.getBoolean("is_admin")) {
						entry.accumulate("type", "ADMIN");
					} else
					entry.accumulate("type", "USER");
					entry.accumulate("url", "/QuizWebsite/User.jsp?user_id="+id);
					results.append("results", entry);
					reapedUsers.add(id);
					total_results--;
					user_results--;
				}
			}
			
			String userEmailMatch = "SELECT * FROM Users Where email_address LIKE UPPER(?) LIMIT ?";
			pstmt = db_connection.prepareStatement(userEmailMatch);
			pstmt.setString(1, text + "%");
			pstmt.setInt(2, min(user_results, total_results));
			
			ResultSet rs4 = pstmt.executeQuery();
			while (rs4.next()) {
				int id = rs4.getInt("id");
				String display_name = rs4.getString("first_name") + " " + rs4.getString("last_name");
				if (!reapedUsers.contains(id)) {
					JSONObject entry = new JSONObject();
					entry.accumulate("id", id);
					entry.accumulate("name", display_name);
					entry.accumulate("category", "user");
					if (rs4.getBoolean("is_admin")) {
						entry.accumulate("type", "ADMIN");
					} else
					entry.accumulate("type", "USER");
					entry.accumulate("url", "/QuizWebsite/User.jsp?user_id="+id);
					results.append("results", entry);
					reapedUsers.add(id);
					total_results--;
					user_results--;
				}
			}
			
			/* End of search by user */
			
			
			/* then search quiz by name suffix */
			String query = "SELECT name,id FROM Quizzes WHERE UPPER(name) LIKE UPPER(?) ORDER BY avg_rating DESC LIMIT 0, ?";
			pstmt = db_connection.prepareStatement(query);
			pstmt.setString(1, "%"+text+"%");
			pstmt.setInt(2, min(quiz_results,total_results));
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("id");
				if (!reapedQuizzes.contains(id)) {
					JSONObject entry = new JSONObject();
					entry.accumulate("id", id);
					entry.accumulate("name", rs.getString("name"));
					entry.accumulate("type", "QUIZ");
					entry.accumulate("category", "quiz");
					entry.accumulate("url", "/QuizWebsite/QuizPage.jsp?quiz_id="+id);
					results.append("results", entry);
					reapedQuizzes.add(id);
					total_results--;
					quiz_results--;
				}
			}
			
			results.accumulate("status", 200);
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
		catch(java.lang.NullPointerException e) {
			e.printStackTrace(); 
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally {}

		if (results.length() == 1) {
			JSONObject emptyEntry = new JSONObject();
			emptyEntry.accumulate("id", 1);
			emptyEntry.accumulate("name", "");
			emptyEntry.accumulate("type", "No results found");
			emptyEntry.accumulate("category", "quiz");
			emptyEntry.accumulate("url", "");
			results.append("results", emptyEntry);
		}
		return results;
	}
	
	private static int min(int a, int b) {
		if (a < b) return a;
		return b;
	}
	
}
