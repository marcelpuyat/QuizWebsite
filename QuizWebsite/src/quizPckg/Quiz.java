package quizPckg;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import questionPckg.Question;

/**
 * Quiz object that holds a list of questions. Supports score checking
 * when passed in a list of answers (which is assumed to be the same length
 * as the list of questions.) Has a unique id.
 * @author marcelp
 *
 */
public class Quiz {

	private String id;
	private ArrayList<Question> questions;
	private int maxScore;
	
	// True if questions are to be given in random order
	private boolean isRandom;
	
	// True if questions are to be shown on multiple pages
	private boolean isMultiplePage;
	
	// True if answers are displayed per question to the user
	private boolean isImmediatelyCorrected;
	
	/**
	 * Initializes Quiz object given list of questions and a quiz id
	 * @param questions
	 * @param id
	 * @param isRandom
	 * @param isMultiplePage
	 * @param isImmediatelyCorrected
	 */
	public Quiz(ArrayList<Question> questions, String id, boolean isRandom,
			boolean isMultiplePage, boolean isImmediatelyCorrected) {
		this.questions = questions;
		this.maxScore = 0;
		this.id = id;
		this.isRandom = isRandom;
		this.isMultiplePage = isMultiplePage;
		this.isImmediatelyCorrected = isImmediatelyCorrected;
		
		for (Question question : questions) {
			this.maxScore += question.getScore();
		}
	}
	
	/**
	 * Get list of this quiz's questions, randomized if quiz is set to random.
	 * @return ArrayList of Question objects
	 */
	public ArrayList<Question> getQuestions() {
		if (this.isRandom) {
			Collections.shuffle(this.questions);
		}
		return this.questions;
	}
	
	/**
	 * Assumes ArrayList of answers is same size as ArrayList
	 * of questions. Returns user score given answers.
	 * @param answers List of answers to questions.
	 * @return score (int)
	 */
	public int checkAnswers(ArrayList<String> answers) {
		int userScore = 0;
		
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
		return this.maxScore;
	}
	
	/**
	 * Returns quiz id
	 * @return id
	 */
	public String getID() {
		return this.id;
	}
	
	/**
	 * True if quiz is shown on multiple pages
	 * @return
	 */
	public boolean isMultiplePage() {
		return this.isMultiplePage;
	}
	
	/**
	 * True if questions are shown randomly
	 * @return
	 */
	public boolean isRandom() {
		return this.isRandom;
	}
	
	/**
	 * True if answer is shown to user per question
	 * @return
	 */
	public boolean isImmediatelyCorrected() {
		return this.isImmediatelyCorrected;
	}
	
	/** NOT SURE IF THIS METHOD IS NEEDED
	 * 
	 * Return map of answers to questions. Useful for displaying results to user that just took quiz.
	 * 
	 * i.e. Question 1: You answered x, which was correct!
	 *      Question 2: You answered y, which was wrong.
	 *      
	 * @param answers List of answers to this quiz
	 * @return Map of answers (which are strings) to question
	 */
	public HashMap<String, Question> getMapOfAnswersToQuestions(ArrayList<String> answers) {
		HashMap<String, Question> map = new HashMap<String, Question>();
		
		for (int i = 0; i < questions.size(); i++) {
			map.put(answers.get(i), questions.get(i));
		}
		
		return map;
	}
	
	@Override
	public String toString() {
		String quizString = "";
		for (int i = 0; i < this.questions.size(); i++) {
			quizString += "Question " + i + ": " + this.questions.get(i).toString() + "\n";
		}
		return quizString;
	}
	
}
