package question;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Has a single prompt, a picture URL, and a set of possible answers.
 * @author marcelp
 *
 */
@SuppressWarnings("serial")
public class PictureQuestion extends Question {

	private String pictureURL;
	
	/**
	 * Takes in a question string, possible set of answers, and a URL string for the picture
	 * @param questionString
	 * @param answers
	 * @param pictureURL
	 */
	public PictureQuestion(String questionString, HashSet<String> answers, String pictureURL, int score) {
		this.prompt = questionString;
		this.possibleAnswers = answers;
		this.pictureURL = pictureURL;
		this.score = score;
	}
	
	@Override
	public int getQuestionType() {
		return QuestionTypes.PICTURE_RESPONSE;
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
	
	/**
	 * Return URL of picture as a string.
	 * @return URL string
	 */
	public String getPictureURL() {
		return this.pictureURL;
	}
}
