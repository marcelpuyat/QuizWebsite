package quizPckg;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

import questionPckg.Question;

/**
 * Quiz object that holds a list of questions. Supports score checking
 * when passed in a list of answers (which is assumed to be the same length
 * as the list of questions.) Has a unique id.
 * @author marcelp
 *
 */
public class Quiz {

	private String quiz_id;
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
	public Quiz(String quiz_id, Connection con) {
		this.quiz_id = quiz_id;
		this.con = con;
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
			if (this.isRandom()) {
				Collections.shuffle(questions);
			}
			return questions;
		}
		catch (Exception e) { e.printStackTrace(); return null; }
		
	}
	
	private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
	    ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return is.readObject();
	}
	
	/**
	 * Assumes ArrayList of answers is same size as ArrayList
	 * of questions. Returns user score given answers.
	 * @param answers List of answers to questions.
	 * @return score (int)
	 */
	
	// IMPLEMENT RANDOM CHECKING USING INDEXING (PASS THROUGH GET REQUEST INFO)
	public int checkAnswers(ArrayList<String> answers) {
		int userScore = 0;
		
		ArrayList<Question> questions = this.getQuestions();
		
		for (int question = 0; question < questions.size(); question++) {
			Question currQuestion = questions.get(question);
			String currAnswer = answers.get(question);
			
			if (currQuestion.isCorrect(currAnswer)) userScore++;
		}
		
		return userScore;
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
	public String getID() {
		try {
			Statement stmt = con.createStatement();
			String getNameQuery = "SELECT id FROM Quizzes WHERE id = \"" + this.quiz_id + "\"";
			ResultSet rs = stmt.executeQuery(getNameQuery);
			rs.next();
			return rs.getString("id");
		}
		catch (Exception e) { e.printStackTrace(); return null; }
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
