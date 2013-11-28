package quiz;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import customObjects.SelfRefreshingConnection;

public class Tag {

	private SelfRefreshingConnection con;
	private long quizID;
	
	/**
	 * Use this to add new tag
	 * @param con
	 * @param quizID
	 * @param tag
	 */
	public Tag(SelfRefreshingConnection con, long quizID, ArrayList<String> tags) {
		this.con = con;
		this.quizID = quizID;
		for (int i = 0; i < tags.size(); i++) {
			createNewTag(con, quizID, tags.get(i));
		}
	}
	
	/**
	 * Use this to get all tags with a quiz id
	 * @param con
	 * @param quizID
	 */
	public Tag(SelfRefreshingConnection con, long quizID) {
		this.quizID = quizID;
		this.con = con;
	}
	
	private static void createNewTag(SelfRefreshingConnection con, long quizID, String tag) {
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement("INSERT INTO Tags (name, quiz_id) VALUES (?, ?)");
			stmt.setString(1, tag);
			stmt.setLong(2, quizID);
			stmt.executeUpdate();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public ArrayList<String> getAllTags() {
		try {
			Statement stmt = con.createStatement();
			String getTagsQuery = "SELECT name FROM Tags WHERE quiz_id = " + this.quizID;
			ResultSet rs = stmt.executeQuery(getTagsQuery);
			
			ArrayList<String> tags = new ArrayList<String>();
			while (rs.next()) {
				tags.add(rs.getString(1));
			}
			return tags;
		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}
	
	public void deleteTags() {
		try {
			Statement stmt = con.createStatement();
			String deleteTagsUpdate = "DELETE from Tags WHERE quiz_id = " + this.quizID;
			stmt.executeUpdate(deleteTagsUpdate);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
