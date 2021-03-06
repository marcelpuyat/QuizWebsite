package question;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Question with only one correct answer. Right now, only implemented
 * to take the EXACT string to be correct... consider extending to accept
 * "similar" strings.
 * 
 * @author marcelp
 *
 */
@SuppressWarnings("serial")
public class SingleAnswerQuestion extends Question {

	public SingleAnswerQuestion(String questionString, HashSet<String> answers, int score) {
		this.prompt = questionString;
		this.possibleAnswers = answers;
		this.score = score;
	}
	
	@Override
	public ArrayList<String> getOptions() {
		return this.options;
	}
	
	@Override
	public int getQuestionType() {
		return QuestionTypes.SINGLE_ANSWER;
	}

	@Override
	public String getPrompt() {
		return this.prompt;
	}

	@Override
	public int getScore() {
		return score;
	}

	@Override
	public String toString() {
		return "Question: " + this.prompt + "\nAnswer: " + this.possibleAnswers.toString();
	}
	
	@Override
	public ArrayList<String> getPossibleAnswers() {
		ArrayList<String> answers = new ArrayList<String>();
		for (String string : this.possibleAnswers) {
			answers.add(string);
		}
		return answers;
	}
}
