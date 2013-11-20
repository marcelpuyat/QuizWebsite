package quiz;

import java.util.Calendar;

/**
 * Contains information on how a user did on a particular quiz.
 * 
 * @author marcelp
 *
 */
public class QuizResults {

	private String username;
	private int quizID;
	private Calendar dateTaken;
	private long timeTaken;
	private int userScore;
	private int maxPossibleScore;
	
	/**
	 * Simply stores all info passed in as a Quiz Results object.
	 * Has a getter method for each field.
	 * @param username
	 * @param quizID
	 * @param userScore
	 * @param maxScore
	 */
	public QuizResults(String username, int quizID, int userScore,
			int maxScore, Calendar dateTaken, long timeTaken) {
		this.username = username;
		this.quizID = quizID;
		this.userScore = userScore;
		this.maxPossibleScore = maxScore;
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
	public int getQuizID() {
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
	 * Returns a Date object representing when
	 * these results were recorded.
	 * @return
	 */
	public Calendar getDateTaken() {
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
