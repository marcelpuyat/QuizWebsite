package question;

import java.util.ArrayList;
import java.util.HashSet;

@SuppressWarnings("serial")
public class FillBlankQuestion extends Question {

	private String optionalPrompt;
	private String firstPrompt;
	private String secondPrompt;
	
	public FillBlankQuestion(String optionalPrompt, String firstPrompt, String secondPrompt, HashSet<String> answers, int score) {
		possibleAnswers = answers;
		this.optionalPrompt = optionalPrompt;
		this.firstPrompt = firstPrompt;
		this.secondPrompt = secondPrompt;
		this.score = score;
	}
	
	@Override
	public int getQuestionType() {
		return QuestionTypes.FILL_BLANK;
	}

	@Override
	public String getPrompt() {
		// NOT USED!
		return null;
	}

	@Override
	public ArrayList<String> getOptions() {
		// NOT USED!
		return null;
	}

	@Override
	public int getScore() {
		return score;
	}

	@Override
	public ArrayList<String> getPossibleAnswers() {
		ArrayList<String> answers = new ArrayList<String>();
		for (String string : this.possibleAnswers) {
			answers.add(string);
		}
		return answers;
	}
	
	public String getOptionalPrompt() {
		return this.optionalPrompt;
	}
	
	public String getFirstPrompt() {
		return this.firstPrompt;
	}

	public String getSecondPrompt() {
		return this.secondPrompt;
	}
	
}
