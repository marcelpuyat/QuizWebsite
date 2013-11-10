package quizPckg;

import java.util.ArrayList;

/**
 * Question with only one correct answer. Right now, only implemented
 * to take the EXACT string to be correct... consider extending to accept
 * "similar" strings.
 * 
 * @author marcelp
 *
 */
public class SingleAnswerQuestion extends Question {

	public SingleAnswerQuestion(String questionString, String answer) {
		this.prompts.add(questionString);
		this.possibleAnswers.add(answer);
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
	public ArrayList<String> getPrompts() {
		return this.prompts;
	}

	@Override
	public boolean isCorrect(String answer) {
		return this.possibleAnswers.contains(answer);
	}

	@Override
	public int getScore() {
		return 1; // CAN BE EXTENDED TO SUPPORT DIFFERENT SCORES
	}

	@Override
	public String toString() {
		return "Question: " + this.prompts.get(0) + "\nAnswer: " + this.possibleAnswers.toString();
	}
	
	/// Simple test to print out questions in a quiz.
	public static void main(String[] args) {
		SingleAnswerQuestion question = new SingleAnswerQuestion("WHO IS THE PRESIDENT OF THE US?", "OBAMA");
		SingleAnswerQuestion question2 = new SingleAnswerQuestion("WHO IS THE PRESIDENT OF STANFORD?", "HENNESSY");
		ArrayList<Question> questions = new ArrayList<Question>();
		questions.add(question);
		questions.add(question2);
		
		Quiz quiz = new Quiz(questions, 1);
		
		System.out.println(quiz);
	}
	
	/**
	 * Returns list of possible answers
	 * @return
	 */
	public ArrayList<String> getPossibleAnswers() {
		ArrayList<String> answers = new ArrayList<String>();
		for (String string : this.possibleAnswers) {
			answers.add(string);
		}
		return answers;
	}
}
