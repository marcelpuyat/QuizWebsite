package quizPckg;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Abstract class of question type that will be subclassed off of.
 * 
 * @author marcelp
 *
 */
public abstract class Question {

	protected ArrayList<String> prompts = new ArrayList<String>();
	protected ArrayList<String> options = new ArrayList<String>();
	protected HashSet<String> possibleAnswers = new HashSet<String>();
	protected int questionType;
	protected int score;
	
	public abstract int getQuestionType();
	public abstract ArrayList<String> getPrompts();
	public abstract ArrayList<String> getOptions();
	public abstract boolean isCorrect(String answer);
	public abstract int getScore();
}
