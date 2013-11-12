package databasesPckg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import questionPckg.MultChoiceMultAnswerQuestion;
import questionPckg.MultipleChoiceQuestion;
import questionPckg.PictureQuestion;
import questionPckg.Question;
import questionPckg.SingleAnswerQuestion;
import quizPckg.Quiz;

/**
 * Contains a bunch of test Quizzes
 * @author marcelp
 *
 */
public class QuizDatabaseStub {
	
	private HashMap<String, Quiz> quizzes;
	
	/**
	 * Database stub that contains a bunch of test quizzes
	 */
	public QuizDatabaseStub() {
		quizzes = new HashMap<String, Quiz>();
		initializeStubDatabase();
	}
	
	/**
	 * Returns a quiz given an ID
	 * @param id
	 * @return
	 */
	public Quiz getQuizForID(String id) {
		return quizzes.get(id);
	}
	
	/**
	 * Adds all test quizzes to quiz map
	 */
	private void initializeStubDatabase() {
		addMultipleChoiceQuiz(); 				// ID "000000"
		addSingleAnswerAndPictureQuiz(); 		// ID "000001"
		addMultipleChoiceMultipleAnswerQuiz(); 	// ID "000002"
	}
	
	/**
	 * Adds a test quiz for this type
	 */
	private void addMultipleChoiceQuiz() {
		
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
		Quiz quiz = new Quiz("Multiple-Choice Quiz", "This is our 1st quiz.", questions, id, true, true, true, false);
		
		quizzes.put(id, quiz);
	}
	
	/**
	 * Adds a test quiz for this type
	 */
	private void addSingleAnswerAndPictureQuiz() {
		ArrayList<Question> questions = new ArrayList<Question>();
		
		HashSet<String> possibleAnswers = new HashSet<String>(3);
		possibleAnswers.add("Barack");
		possibleAnswers.add("Obama");
		possibleAnswers.add("Barack Obama");
		SingleAnswerQuestion question = new SingleAnswerQuestion("Who is the president of the United States?", possibleAnswers);
		questions.add(question);
		
		/* Image question test */
		HashSet<String> possibleAnswers2 = new HashSet<String>(2);
		possibleAnswers.add("Google");
		possibleAnswers.add("GOOGLE");
		PictureQuestion question2 = new PictureQuestion("What company invented a web-browser with this logo?", possibleAnswers2, "https://lh3.ggpht.com/7O3H3V0fEBumwJlqDLD03t1fmwl8fH9YoBsPwB2UQ_aiBilM7OAOe2gkFB3wrojJqbM=w300");
		questions.add(question2);
		
		
		String id = "000001";
		Quiz quiz = new Quiz("SingleAnswer & Image Quiz", "This is our 2nd quiz.", questions, id, false, true, true, false);
		quizzes.put(id, quiz);
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
		Quiz quiz = new Quiz("MultChoiceMultAnswer Quiz", "This is our 3rd quiz.", questions, id, false, true, true, false);
		quizzes.put(id, quiz);
	}
}
