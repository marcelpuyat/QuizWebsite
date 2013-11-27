package question;

import java.util.ArrayList;

import customObjects.StringBooleanPair;


/**
 * Question with choices and can have multiple numbers of them selected.
 * Answer is a combination of selected choices.
 * @author marcelp
 *
 */
@SuppressWarnings("serial")
public class MultChoiceMultAnswerQuestion extends Question {

	private ArrayList<StringBooleanPair> pairs;
	private boolean partialCredit;
	
	/**
	 * Constructor.
	 * @param questionString prompt
	 * @param options choices
	 * @param correctOptions String in the form of 0s and 1s, 1 number for each choice. 1 if it is selected, 0 if not.
	 */
	public MultChoiceMultAnswerQuestion(String questionString, ArrayList<StringBooleanPair> choicesAndAnswers, int score, boolean partialCredit) {
		this.prompt = questionString;
		this.pairs = choicesAndAnswers;
		this.score = score;
		this.partialCredit = partialCredit;
	}

	public boolean isPartialCredit() {
		return this.partialCredit;
	}
	
	@Override
	public int getQuestionType() {
		return QuestionTypes.MULT_CHOICE_MULT_ANSWER;
	}

	@Override
	public String getPrompt() {
		return this.prompt;
	}

	@Override
	public ArrayList<String> getOptions() {
		return this.options;
	}
	
	public ArrayList<StringBooleanPair> getPairs() {
		return this.pairs;
	}
	
	@Override
	public int getScore() {
		return this.score;
	}

	@Override
	/**
	 * Not to be used by this class
	 */
	public ArrayList<String> getPossibleAnswers() {
		return null;
	}

}
