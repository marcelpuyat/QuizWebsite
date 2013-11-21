package question;

import java.util.ArrayList;

import customObjects.StringPair;

@SuppressWarnings("serial")
public class MatchingQuestion extends Question {

	private ArrayList<StringPair> correctPairs;
	
	/**
	 * Correct answers are formatted like this:
	 * 
	 * For each position in the integer array, the nth leftChoice is the index, and
	 * the value of the nth index is what the index of its answer is in the rightChoices array.
	 * 
	 * For example:
	 * question = Capitals of countries
	 * leftChoices = {Rome, Washington D.C.}
	 * rightChoices = {USA, Italy}
	 * 
	 * The answer would be {1, 0} because Rome maps to the
	 * 1st index in rightChoices and Washington D.C. maps to
	 * the 0th index in rightChoices.
	 * 
	 * @param questionString
	 * @param pairs
	 * @param score
	 */
	public MatchingQuestion(String questionString, ArrayList<StringPair> pairs, 
			int score) {
		this.prompt = questionString;
		this.correctPairs = pairs;
		this.score = score;
	}
	
	@Override
	public int getQuestionType() {
		return QuestionTypes.MATCHING;
	}

	@Override
	public String getPrompt() {
		return this.prompt;
	}

	@Override
	/**
	 * DO NOT USE THIS!!!
	 */
	public ArrayList<String> getOptions() {
		return null;
	}

	@Override
	public int getScore() {
		return 1;
	}

	public ArrayList<StringPair> getPairs() {
		return correctPairs;
	}
	
	@Override
	/**
	 * DO NOT USE!!!
	 */
	public ArrayList<String> getPossibleAnswers() {
		return null;
	}

}
