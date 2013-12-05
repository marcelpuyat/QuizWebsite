package quiz;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.rowset.serial.SerialBlob;

import org.json.JSONArray;
import org.json.JSONObject;

import question.Question;
import customObjects.SelfRefreshingConnection;

/**
 * Quiz object that holds a list of questions. Supports score checking
 * when passed in a list of answers (which is assumed to be the same length
 * as the list of questions.) Has a unique id.
 * @author marcelp
 *
 */
public class Quiz {

	public static int AUTO_INCREMENT_OFFSET = 1;
	private long quiz_id;
	private SelfRefreshingConnection con;

	/**
	 * Initializes Quiz object given list of questions and a quiz id
	 * @param questions
	 * @param id
	 * @param isRandom
	 * @param isMultiplePage
	 * @param isImmediatelyCorrected
	 * @param isPracticable
	 */
	public Quiz(long quiz_id, SelfRefreshingConnection con) {
		this.quiz_id = quiz_id;
		this.con = con;
	}
	
	/**
	 * Use this to create new quiz, passing in all necessary fields
	 * @param con
	 * @throws ClassNotFoundException 
	 */
	public Quiz(SelfRefreshingConnection con, String name, String creator, String description, ArrayList<Question> questions, int maxScore, 
			boolean isRandomizable, boolean isMultiplePage, boolean isPracticable, boolean isImmediatelyCorrected) throws ClassNotFoundException {
		this.quiz_id = Quiz.getNextAvailableID(con);
		this.con = con;
		try {
			addQuiz(name, creator, description, questions, maxScore, isRandomizable, isMultiplePage, isPracticable,
					isImmediatelyCorrected);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public double getAverageRating() {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT rating FROM Ratings WHERE quiz_id = " + this.quiz_id);
			ResultSet rs = stmt.executeQuery();
			double total_rating = 0;
			double count = 0;
			while (rs.next()) {
				count++;
				double this_rating = rs.getDouble(1);
				total_rating += this_rating;
			}
			if (count == 0) return -1;
			
			double average_rating = total_rating / count;
			
			return average_rating;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public int getNumberOfRatings() {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT rating FROM Ratings WHERE quiz_id = " + this.quiz_id);
			ResultSet rs = stmt.executeQuery();
			int count = 0;
			while (rs.next()) {
				count++;
			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public void rateQuiz(int rating, long user_id) {
		String rateStatement;
		if (hasRatedThisQuizAlready(user_id)) {
			rateStatement = "UPDATE Ratings SET rating = " + rating + " WHERE quiz_id = " + this.quiz_id + " AND user_id = " + user_id;
		}
		else {
			rateStatement = "INSERT INTO Ratings (ratings, quiz_id, user_id, id) VALUES("+rating+", "+quiz_id+", "+user_id+", id)";
		}
		try {
			PreparedStatement stmt = con.prepareStatement(rateStatement);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteRating(long user_id) {
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement("DELETE FROM Ratings WHERE quiz_id = " + quiz_id + " AND user_id = " + user_id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private boolean hasRatedThisQuizAlready(long user_id) {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT id FROM Ratings WHERE user_id = " + user_id + " AND quiz_id = " + this.quiz_id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) return true;
			else return false;
		} catch (Exception e) { e.printStackTrace(); return false; }
	}
	
	public static void deleteQuiz(long quiz_id, SelfRefreshingConnection con) throws SQLException, ClassNotFoundException {
		Statement stmt = con.createStatement();
		String deleteUpdateString = "DELETE FROM Quizzes WHERE id = " + quiz_id;
		stmt.executeUpdate(deleteUpdateString);
	}
	
	/**
	 * Use this to edit quiz
	 * @param con
	 * @param name
	 * @param creator
	 * @param description
	 * @param questions
	 * @param maxScore
	 * @param isRandomizable
	 * @param isMultiplePage
	 * @param isPracticable
	 * @param isImmediatelyCorrected
	 * @param quiz_id
	 */
	public static void editQuiz(long quiz_id, SelfRefreshingConnection con, String name, String creator, String description, ArrayList<Question> questions, int maxScore, 
			boolean isRandomizable, boolean isMultiplePage, boolean isPracticable, boolean isImmediatelyCorrected) {
		String editUpdate = "UPDATE Quizzes SET name = ?, creator = ?, description = ?, questions = ?, max_score = ?, is_randomizable = ?, is_multiple_page = ?, is_practicable = ?, is_immediately_corrected = ? " +
		"WHERE id = " + quiz_id;
		try {
			PreparedStatement stmt = con.prepareStatement(editUpdate);
			stmt.setString(1, name);
			stmt.setString(2, creator);
			stmt.setString(3, description);			
			byte[] questionBytes = serialize(questions);
			Blob questionBlob = new SerialBlob(questionBytes);
			stmt.setBlob(4, questionBlob);
			stmt.setInt(5, maxScore);
			stmt.setBoolean(6, isRandomizable);
			stmt.setBoolean(7, isMultiplePage);
			stmt.setBoolean(8, isPracticable);
			stmt.setBoolean(9, isImmediatelyCorrected);
			stmt.executeUpdate();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void incrementFrequency() {
		try {
			PreparedStatement stmt = con.prepareStatement("UPDATE Quizzes SET frequency = frequency + 1 WHERE id = " + this.quiz_id);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static JSONArray getMostPopularQuizzes(SelfRefreshingConnection con) {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT creator, name, id, frequency FROM Quizzes ORDER BY frequency DESC LIMIT 5");
			ResultSet rs = stmt.executeQuery();
			JSONArray quizzes = new JSONArray();
			
			while (rs.next()) {
				JSONObject quiz = new JSONObject();
				quiz.put("quiz_name", rs.getString("name"));
				quiz.put("creator", rs.getString("creator"));
				quiz.put("quiz_id", rs.getLong("id"));
				quiz.put("frequency", rs.getLong("frequency"));
				quizzes.put(quiz);
			}
			return quizzes;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static JSONArray getMostRecentQuizzes(SelfRefreshingConnection con){
		try{
			PreparedStatement stmt = con.prepareStatement("SELECT creator, name, id FROM Quizzes ORDER BY id DESC LIMIT 5");
			ResultSet rs = stmt.executeQuery();
			JSONArray quizzes = new JSONArray();
			
			while(rs.next()){
				JSONObject quiz = new JSONObject();
				quiz.put("quiz_name", rs.getString("name"));
				quiz.put("creator", rs.getString("creator"));
				quiz.put("quiz_id", rs.getLong("id"));
				quizzes.put(quiz);
			}
			return quizzes;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns next available ID
	 * @param con
	 * @return
	 * @throws ClassNotFoundException 
	 */
	public static int getNextAvailableID(SelfRefreshingConnection con) throws ClassNotFoundException {
		try {
			Statement stmt = con.createStatement();
			String getNextIDQuery = "SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = 'Quizzes' AND table_schema = 'c_cs108_marcelp'";
			ResultSet rs = stmt.executeQuery(getNextIDQuery);
			rs.next();
			int nextID = rs.getInt(1);
			System.out.println(nextID);
			return nextID + AUTO_INCREMENT_OFFSET;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("-1");
			return -1;
		}
	}
	
	/**
	 * Adds quiz to DB
	 * @param name
	 * @param creator
	 * @param description
	 * @param questions
	 * @param maxScore
	 * @param isRandomizable
	 * @param isMultiplePage
	 * @param isPracticable
	 * @param isImmediatelyCorrected
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	private void addQuiz(String name, String creator, String description, ArrayList<Question> questions, int maxScore, 
			boolean isRandomizable, boolean isMultiplePage, boolean isPracticable, boolean isImmediatelyCorrected) throws IOException, ClassNotFoundException
	{
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement("INSERT INTO Quizzes (name, creator, description, questions, max_score, is_randomizable, is_multiple_page, is_practicable, is_immediately_corrected, frequency) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, 0)");
			stmt.setString(1, name);
			stmt.setString(2, creator);
			stmt.setString(3, description);			
			byte[] questionBytes = serialize(questions);
			Blob questionBlob = new SerialBlob(questionBytes);
			stmt.setBlob(4, questionBlob);
			stmt.setInt(5, maxScore);
			stmt.setBoolean(6, isRandomizable);
			stmt.setBoolean(7, isMultiplePage);
			stmt.setBoolean(8, isPracticable);
			stmt.setBoolean(9, isImmediatelyCorrected);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static byte[] serialize(ArrayList<Question> questions) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(questions);
	    return out.toByteArray();
	}
	
	public String getName() {
		try {
			Statement stmt = con.createStatement();
			String getNameQuery = "SELECT name FROM Quizzes WHERE id = \"" + this.quiz_id + "\"";
			ResultSet rs = stmt.executeQuery(getNameQuery);
			rs.next();
			return rs.getString("name");
		}
		catch (Exception e) { e.printStackTrace(); return null; }
	}
	
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
	
	/**
	 * Get list of this quiz's questions, randomized if quiz is set to random.
	 * @return ArrayList of Question objects
	 */
	public ArrayList<Question> getQuestions() {
		
		try {
			Statement stmt = con.createStatement();
			String getNameQuery = "SELECT questions FROM Quizzes WHERE id = \"" + this.quiz_id + "\"";
			ResultSet rs = stmt.executeQuery(getNameQuery);
			rs.next();
			Blob questionBlob = rs.getBlob("questions");
			byte[] bytes = questionBlob.getBytes(1, (int)questionBlob.length());
			@SuppressWarnings("unchecked")
			ArrayList<Question> questions = (ArrayList<Question>) deserialize(bytes);
//			if (this.isRandom()) {
//				Collections.shuffle(questions);
//			}
			return questions;
		}
		catch (Exception e) { e.printStackTrace(); return null; }
		
	}
	
	/**
	 * Used for turning BLOBs into Question Arrays
	 * @param data
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
	    ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return is.readObject();
	}
	
	/**
	 * Returns max possible score
	 * @return max score
	 */
	public int getMaxScore() {
		try {
			Statement stmt = con.createStatement();
			String getNameQuery = "SELECT max_score FROM Quizzes WHERE id = \"" + this.quiz_id + "\"";
			ResultSet rs = stmt.executeQuery(getNameQuery);
			rs.next();
			return rs.getInt("max_score");
		}
		catch (Exception e) { e.printStackTrace(); return 0; }
	}
	
	/**
	 * Returns quiz id
	 * @return id
	 */
	public int getID() {
		try {
			Statement stmt = con.createStatement();
			String getNameQuery = "SELECT id FROM Quizzes WHERE id = \"" + this.quiz_id + "\"";
			ResultSet rs = stmt.executeQuery(getNameQuery);
			rs.next();
			return rs.getInt("id");
		}
		catch (Exception e) { e.printStackTrace(); return 0; }
	}
	
	public String getCreator() {
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
	 * True if quiz is shown on multiple pages
	 * @return
	 */
	public boolean isMultiplePage() {
		try {
			Statement stmt = con.createStatement();
			String getNameQuery = "SELECT is_multiple_page FROM Quizzes WHERE id = \"" + this.quiz_id + "\"";
			ResultSet rs = stmt.executeQuery(getNameQuery);
			rs.next();
			return rs.getBoolean("is_multiple_page");
		}
		catch (Exception e) { e.printStackTrace(); return false; }
	}
	
	/**
	 * True if questions are shown randomly
	 * @return
	 */
	public boolean isRandom() {
		try {
			Statement stmt = con.createStatement();
			String getNameQuery = "SELECT is_randomizable FROM Quizzes WHERE id = \"" + this.quiz_id + "\"";
			ResultSet rs = stmt.executeQuery(getNameQuery);
			rs.next();
			return rs.getBoolean("is_randomizable");
		}
		catch (Exception e) { e.printStackTrace(); return false; }
	}
	
	/**
	 * True if answer is shown to user per question
	 * @return
	 */
	public boolean isImmediatelyCorrected() {
		try {
			Statement stmt = con.createStatement();
			String getNameQuery = "SELECT is_immediately_corrected FROM Quizzes WHERE id = \"" + this.quiz_id + "\"";
			ResultSet rs = stmt.executeQuery(getNameQuery);
			rs.next();
			return rs.getBoolean("is_immediately_corrected");
		}
		catch (Exception e) { e.printStackTrace(); return false; }
	}
	
	/**
	 * True if quiz can be practiced
	 * @return
	 */
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

	
	@Override
	public String toString() {
		return "not supported";
	}
	
}
