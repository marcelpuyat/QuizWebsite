package quiz;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;
import java.util.ArrayList;

import question.*;

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
	private Connection con;

	/**
	 * Initializes Quiz object given list of questions and a quiz id
	 * @param questions
	 * @param id
	 * @param isRandom
	 * @param isMultiplePage
	 * @param isImmediatelyCorrected
	 * @param isPracticable
	 */
	public Quiz(long quiz_id, Connection con) {
		this.quiz_id = quiz_id;
		this.con = con;
	}
	
	/**
	 * Use this to create new quiz, passing in all necessary fields
	 * @param con
	 */
	public Quiz(Connection con) {
		this.quiz_id = Quiz.getNextAvailableID(con);
		this.con = con;
	}
	
	public static int getNextAvailableID(Connection con) {
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
