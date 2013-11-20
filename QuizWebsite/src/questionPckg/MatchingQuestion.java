package questionPckg;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class MatchingQuestion extends Question {

	private ArrayList<String> leftOptions;
	private ArrayList<String> rightOptions;
	private ArrayList<Integer> correctAnswers;
	
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
	 * @param leftChoices
	 * @param rightChoices
	 * @param correctAnswers
	 */
	public MatchingQuestion(String questionString, ArrayList<String> leftChoices, 
			ArrayList<String> rightChoices, ArrayList<Integer> correctAnswers) {
		this.prompt = questionString;
		this.leftOptions = leftChoices;
		this.rightOptions = rightChoices;
		this.correctAnswers = correctAnswers;
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
	
	/**
	 * Returns list of strings of left options
	 * @return
	 */
	public ArrayList<String> getLeftOptions() {
		return this.leftOptions;
	}
	
	/**
	 * Returns list of strings of right options
	 * @return
	 */
	public ArrayList<String> getRightOptions() {
		return this.rightOptions;
	}
	
	@Override
	public boolean isCorrect(String answer) {
		ArrayList<Integer> userAnswers = new ArrayList<Integer>(answer.length());
		for (int i = 0; i < answer.length(); i++) {
			// Convert ith char into an integer object
			Integer num = new Integer(Integer.parseInt(String.valueOf(answer.charAt(i))));
			userAnswers.add(num);
		}
		return userAnswers.equals(this.correctAnswers);
	}

	@Override
	public int getScore() {
		return 1;
	}

	/**
	 * Returns correct answer in the form of:
	 * 
	 * nth index is the nth number of the leftOptions array - 
	 * value of nth index is the index of the rightOptions it should match to.
	 * @return
	 */
	public ArrayList<Integer> getAnswer() {
		return this.correctAnswers;
	}
	
	@Override
	/**
	 * DO NOT USE!!!
	 */
	public ArrayList<String> getPossibleAnswers() {
		return null;
	}

}
