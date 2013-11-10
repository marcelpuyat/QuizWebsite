package quizPckg;

import java.util.ArrayList;

/**
 * Question with single answer, which is an index out of the number of
 * options available.
 * 
 * @author marcelp
 *
 */
public class MultipleChoiceQuestion extends Question {

	private int answer;
	/**
	 * Takes in a single prompt, a list of options, and the index of the correct answer
	 * among the options.
	 * @param prompt Single string
	 * @param options List of strings
	 * @param answer Index (as an integer)
	 */
	public MultipleChoiceQuestion(String prompt, ArrayList<String> options, int answer) {
		possibleAnswers.add(String.valueOf(answer));
		this.answer = answer;
		this.options = options;
		this.prompts.add(prompt);
	}
	
	@Override
	/**
	 * Value of 1.
	 */
	public int getQuestionType() {
		return QuestionTypes.MULTIPLE_CHOICE;
	}

	
	@Override
	/**
	 * Returns List with 1 string.
	 */
	public ArrayList<String> getPrompts() {
		return prompts;
	}

	@Override
	public ArrayList<String> getOptions() {
		return options;
	}

	@Override
	/**
	 * Returns bool that says if passed in answer is right or wrong.
	 */
	public boolean isCorrect(String answer) {
		return (possibleAnswers.contains(answer));
	}
	
	@Override
	/**
	 * Returns score worth
	 */
	public int getScore() {
		return 1;
	}
	
	/**
	 * Returns answer
	 * @return
	 */
	public int getAnswer() {
		return answer;
	}
}
