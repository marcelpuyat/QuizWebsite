package questionPckg;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Abstract class of question type that will be subclassed off of.
 * 
 * @author marcelp
 *
 */
public abstract class Question {

	/** Question string */
	protected String prompt;
	
	/**
	 * List of options, which can be null for questions that dont have options
	 */
	protected ArrayList<String> options = new ArrayList<String>();
	
	/**
	 * Set of possible answers. Contains several answers for some questions
	 * (i.e. Washington & George Washington), or just one exact answer for
	 * multiple choice (which will be the index of the correct answer out
	 * of the list of options)
	 */
	protected HashSet<String> possibleAnswers = new HashSet<String>();
	
	/**
	 * Interpret/set this through QuestionTypes.java
	 */
	protected int questionType;
	
	/**
	 * How many points this question is worth.
	 */
	protected int score;
	
	/**
	 * Returns question type, which can be interpreted by QuestionTypes
	 * @return use QuestionTypes Class to interpret this.
	 */
	public abstract int getQuestionType();
	
	/**
	 * Returns a list of the prompts (which will be a single string in most cases)
	 * @return List of prompts
	 */
	public abstract String getPrompt();
	
	/**
	 * Returns a list of options
	 * @return List of options
	 */
	public abstract ArrayList<String> getOptions();
	
	/**
	 * Returns true if passed in answer is correct, false if not
	 * @param answer Answer
	 * @return True for correct, False for wrong
	 */
	public abstract boolean isCorrect(String answer);
	
	/**
	 * Returns the amount of points this question is worth
	 * @return points
	 */
	public abstract int getScore();
	
	/**
	 * Returns list of possible answers
	 * @return list of answers
	 */
	public abstract ArrayList<String> getPossibleAnswers();
}
