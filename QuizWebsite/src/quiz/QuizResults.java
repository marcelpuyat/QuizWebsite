package quiz;

import java.util.Calendar;

/**
 * Contains information on how a user did on a particular quiz.
 * 
 * @author marcelp
 *
 */
public class QuizResults {

	private long user_id;
	private long quizID;
	private long timeTaken;
	private int userScore;
	
	/**
	 * Simply stores all info passed in as a Quiz Results object.
	 * Has a getter method for each field.
	 * @param user_id
	 * @param quizID
	 * @param userScore
	 * @param timeTaken
	 */
	public QuizResults(long user_id, long quizID, int userScore, long timeTaken) {
		this.user_id = user_id;
		this.quizID = quizID;
		this.userScore = userScore;
		this.timeTaken = timeTaken;
	}
	
	/**
	 * Returns username for user who took the quiz
	 * @return String username
	 */
	public long getUserID() {
		return user_id;
	}
	
	public String getUsername() {
		return "To be implemented";
	}
	
	/**
	 * Returns the quizID
	 * @return quiz ID
	 */
	public long getQuizID() {
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
	 * Returns a Date object representing when
	 * these results were recorded.
	 * @return
	 */
	public Calendar getDateTaken() {
		return null; //TODO
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
