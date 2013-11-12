package quizPckg;

import java.util.Date;

/**
 * Contains information on how a user did on a particular quiz.
 * 
 * @author marcelp
 *
 */
public class QuizResults {

	private String username;
	private String quizID;
	private Date dateTaken;
	private long timeTaken;
	private int userScore;
	private int maxPossibleScore;
	private double percentageScore;
	
	/**
	 * Simply stores all info passed in as a Quiz Results object.
	 * Has a getter method for each field.
	 * @param username
	 * @param quizID
	 * @param userScore
	 * @param maxScore
	 */
	public QuizResults(String username, String quizID, int userScore,
			int maxScore, Date dateTaken, long timeTaken) {
		this.username = username;
		this.quizID = quizID;
		this.userScore = userScore;
		this.maxPossibleScore = maxScore;
		this.percentageScore = (double)userScore / (double)maxScore;
		this.dateTaken = dateTaken;
		this.timeTaken = timeTaken;
	}
	
	/**
	 * Returns username for user who took the quiz
	 * @return String username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Returns the quizID
	 * @return quiz ID
	 */
	public String getQuizID() {
		return quizID;
	}
	
	/**
	 * Returns the user's score
	 * @return user's score
	 */
	public int getUserScore() {
		return userScore;
	}
	
	/**
	 * Returns max possible score
	 * @return max possible score
	 */
	public int getMaxScore() {
		return maxPossibleScore;
	}
	
	/**
	 * Percentage of questions user got right.
	 * @return percentage of correct answers.
	 */
	public double getPercentageScore() {
		return this.percentageScore;
	}
	
	/**
	 * Returns a Date object representing when
	 * these results were recorded.
	 * @return
	 */
	public Date getDateTaken() {
		return this.dateTaken;
	}
	
	/**
	 * Returns the time it took for the user
	 * to complete this quiz.
	 * @return
	 */
	public long getTimeTaken() {
		return this.timeTaken;
	}
}
