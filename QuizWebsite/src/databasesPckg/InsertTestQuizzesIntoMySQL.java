package databasesPckg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

import javax.sql.rowset.serial.SerialBlob;

import questionPckg.MultChoiceMultAnswerQuestion;
import questionPckg.MultipleChoiceQuestion;
import questionPckg.PictureQuestion;
import questionPckg.Question;
import questionPckg.SingleAnswerQuestion;

/**
 * Contains a bunch of test Quizzes. Run this to insert these Quizzes into the SQL Database.
 * @author marcelp
 *
 */
public class InsertTestQuizzesIntoMySQL {
	
	private Connection con;
	
	/**
	 * Database stub that contains a bunch of test quizzes
	 * @throws IOException 
	 */
	public InsertTestQuizzesIntoMySQL() throws IOException {
		con = createConnection();
		initializeStubDatabase();
		test();
		closeConnection();
	}
	
	private Connection createConnection() {
    	try { 
			Class.forName("com.mysql.jdbc.Driver"); 

			Connection con = DriverManager.getConnection 
					( "jdbc:mysql://" + DBInfo.MYSQL_DATABASE_SERVER, DBInfo.MYSQL_USERNAME ,DBInfo.MYSQL_PASSWORD);
			Statement stmt = con.createStatement();
			stmt.executeQuery("USE " + DBInfo.MYSQL_DATABASE_NAME);
			return con;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
	
	private void closeConnection() {
		try {
			con.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds quiz to MySQL Database given all the info
	 * @param id
	 * @param name
	 * @param creator
	 * @param description
	 * @param questions
	 * @param maxScore
	 * @param isRandomizable
	 * @param isMultiplePage
	 * @param isPracticable
	 * @throws IOException 
	 */
	private void addQuiz(String id, String name, String creator, String description, ArrayList<Question> questions, int maxScore, 
			boolean isRandomizable, boolean isMultiplePage, boolean isPracticable, boolean isImmediatelyCorrected) throws IOException
	{
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement("INSERT INTO Quizzes VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setString(1, id);
			stmt.setString(2, name);
			stmt.setString(3, creator);
			stmt.setString(4, description);			
			byte[] questionBytes = serialize(questions);
			Blob questionBlob = new SerialBlob(questionBytes);
			stmt.setBlob(5, questionBlob);
			stmt.setInt(6, maxScore);
			stmt.setBoolean(7, isRandomizable);
			stmt.setBoolean(8, isMultiplePage);
			stmt.setBoolean(9, isPracticable);
			stmt.setBoolean(10, isImmediatelyCorrected);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private byte[] serialize(ArrayList<Question> questions) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(questions);
	    return out.toByteArray();
	}
	
	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
	    ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return is.readObject();
	}
	
	/**
	 * Adds all test quizzes to quiz map
	 * @throws IOException 
	 */
	private void initializeStubDatabase() throws IOException {
		addMultipleChoiceQuiz(); 				// ID "000000"
		addSingleAnswerAndPictureQuiz(); 		// ID "000001"
		//addMultipleChoiceMultipleAnswerQuiz(); 	// ID "000002"
	}
	
	/**
	 * Adds a test quiz for this type
	 * @throws IOException 
	 */
	private void addMultipleChoiceQuiz() throws IOException {
		
		/* Question 1 */
    	ArrayList<String> options = new ArrayList<String>(2);
		options.add("Stanford");
		options.add("Cal");
		
		int answer = 0;
		
		String prompt = "Which University would you rather attend?";
		
		MultipleChoiceQuestion question = new MultipleChoiceQuestion(prompt, options, answer);
		
		/* Another question */
		ArrayList<String> options2 = new ArrayList<String>(4);
		options2.add("CS110");
		options2.add("CS108");
		options2.add("CS12035");
		
		int answer2 = 1;
		
		String prompt2 = "What class is this assignment for?";
		
		MultipleChoiceQuestion question2 = new MultipleChoiceQuestion(prompt2, options2, answer2);
		
		/* 3rd question */
		ArrayList<String> options3 = new ArrayList<String>(4);
		options3.add("O(n)");
		options3.add("O(n log(n))");
		options3.add("O(log(n))");
		
		int answer3 = 1;
		
		String prompt3 = "Merge-sort runs in...";
		
		MultipleChoiceQuestion question3 = new MultipleChoiceQuestion(prompt3, options3, answer3);
		
		ArrayList<Question> questions = new ArrayList<Question>();
		questions.add(question);
		questions.add(question2);
		questions.add(question3);
		
		String id = "000000";
		String name = "Multiple-Choice Quiz";
		String description = "This is our 1st quiz";
		int maxScore = 3;
		boolean isRandomizable = true;
		boolean isMultiplePage = true;
		boolean isPracticable = false;
		boolean isImmediatelyCorrected = false;

		addQuiz(id, name, "Nobody", description, questions, maxScore, isRandomizable, isMultiplePage, isPracticable, isImmediatelyCorrected);
	}
	
	/**
	 * Adds a test quiz for this type
	 * @throws IOException 
	 */
	private void addSingleAnswerAndPictureQuiz() throws IOException {
		ArrayList<Question> questions = new ArrayList<Question>();
		
		HashSet<String> possibleAnswers = new HashSet<String>(3);
		possibleAnswers.add("Barack");
		possibleAnswers.add("Obama");
		possibleAnswers.add("Barack Obama");
		SingleAnswerQuestion question = new SingleAnswerQuestion("Who is the president of the United States?", possibleAnswers);
		questions.add(question);
		
		/* Image question test */
		HashSet<String> possibleAnswers2 = new HashSet<String>(2);
		possibleAnswers2.add("Google");
		possibleAnswers2.add("GOOGLE");
		PictureQuestion question2 = new PictureQuestion("What company invented a web-browser with this logo?", possibleAnswers2, "https://lh3.ggpht.com/7O3H3V0fEBumwJlqDLD03t1fmwl8fH9YoBsPwB2UQ_aiBilM7OAOe2gkFB3wrojJqbM=w300");
		questions.add(question2);
		
		
		String id = "000001";
		String name = "Single Answer And Picture Quiz";
		String description = "This is our 2nd quiz";
		int maxScore = 2;
		boolean isRandomizable = false;
		boolean isMultiplePage = true;
		boolean isPracticable = false;
		boolean isImmediatelyCorrected = false;
		
		addQuiz(id, name, "Nobody", description, questions, maxScore, isRandomizable, isMultiplePage, isPracticable, isImmediatelyCorrected);
	}
	
	/**
	 * Adds a test quiz for this type
	 */
	private void addMultipleChoiceMultipleAnswerQuiz() {
		ArrayList<Question> questions = new ArrayList<Question>();

		/* Multiple Choice Multiple Answer test */
		String correctAnswers = "0110";
		String prompt = "Which 2 out of these 4 algorithms run in O(log (n)) time?";
		
		ArrayList<String> options = new ArrayList<String>();
		
		options.add("Quicksort");
		options.add("Binary search");
		options.add("Inserting into a Binary Heap");
		options.add("Bubble-sort");
		MultChoiceMultAnswerQuestion question = new MultChoiceMultAnswerQuestion(prompt, options, correctAnswers);
		questions.add(question);
		
		String id = "000002";
	//	Quiz quiz = new Quiz("MultChoiceMultAnswer Quiz", "This is our 3rd quiz.", questions, id, false, true, true, false);
		//quizzes.put(id, quiz);
	}
	
	private void test() {
		try {
			Statement stmt = con.createStatement();
			String query = "SELECT questions FROM Quizzes";
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next())
			{
				Blob blob = rs.getBlob("questions");
				byte[] bytes = blob.getBytes(1, (int)blob.length());
				@SuppressWarnings("unchecked")
				ArrayList<Question> questions = (ArrayList<Question>)deserialize(bytes);
				System.out.println(questions.get(0).getPrompt());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public static void main (String[] args) {
		try {
			InsertTestQuizzesIntoMySQL test = new InsertTestQuizzesIntoMySQL();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
