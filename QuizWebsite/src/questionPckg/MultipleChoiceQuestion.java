package questionPckg;

import java.util.ArrayList;


/**
 * Question with single answer, which is an index out of the list of
 * options available.
 * 
 * @author marcelp
 *
 */
public class MultipleChoiceQuestion extends Question {

	// Index of the answer in the options list.
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
	public int getQuestionType() {
		return QuestionTypes.MULTIPLE_CHOICE;
	}

	
	@Override
	public ArrayList<String> getPrompts() {
		return prompts;
	}

	@Override
	public ArrayList<String> getOptions() {
		return options;
	}

	@Override
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
	 * Returns index of the answer out of the choices
	 * @return Index of answer
	 */
	public int getAnswer() {
		return answer;
	}
}
