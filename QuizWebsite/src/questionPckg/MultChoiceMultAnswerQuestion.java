package questionPckg;

import java.util.ArrayList;

/**
 * Question with choices and can have multiple numbers of them selected.
 * Answer is a combination of selected choices.
 * @author marcelp
 *
 */
public class MultChoiceMultAnswerQuestion extends Question {

	// In form "01010" where 0 means dont select and 1 means select
	private String answer;
	
	/**
	 * Constructor.
	 * @param questionString prompt
	 * @param options choices
	 * @param correctOptions String in the form of 0s and 1s, 1 number for each choice. 1 if it is selected, 0 if not.
	 */
	public MultChoiceMultAnswerQuestion(String questionString, ArrayList<String> options,
			String correctOptions) {
		this.prompt = questionString;
		this.options = options;
		this.answer = correctOptions;
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

	@Override
	public boolean isCorrect(String answer) {
		return this.answer.equals(answer);
	}
	
	public String getAnswer() {
		return this.answer;
	}
	
	@Override
	public int getScore() {
		return 1;
	}

	@Override
	/**
	 * Not to be used by this class
	 */
	public ArrayList<String> getPossibleAnswers() {
		return null;
	}

}
