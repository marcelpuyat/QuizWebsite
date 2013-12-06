package quiz;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

import user.User;
import customObjects.RankAndTotal;
import customObjects.SelfRefreshingConnection;

/**
 * Contains information on how a user did on a particular quiz.
 * 
 * @author marcelp
 *
 */
public class QuizResults {

	private long id;
	private long user_id;
	private long quizID;
	private SelfRefreshingConnection con;
	
	/**
	 * Use this to create a new QuizResults instance in database.
	 * @param user_id
	 * @param quizID
	 * @param userScore
	 * @param timeTaken
	 */
	public QuizResults(long user_id, long quizID, double userPercentageScore, double timeTaken, SelfRefreshingConnection con) {
		this.user_id = user_id;
		this.quizID = quizID;
		this.con = con;
		createNewQuizResultsInstance(user_id, quizID, userPercentageScore, timeTaken);
	}
	
	/**
	 * Use this when simply querying a QuizResults instance from database.
	 * @param user_id
	 * @param quizID
	 */
	public QuizResults(long id, long user_id, long quizID, SelfRefreshingConnection con) {
		this.id = id;
		this.user_id = user_id;
		this.quizID = quizID;
		this.con = con;
	}
	
	private void createNewQuizResultsInstance(long user_id, long quizID, double userPercentageScore, double timeTaken) {
		try {
			PreparedStatement stmt;
			stmt = con.prepareStatement("INSERT INTO QuizResults (user_id, quiz_id, time_duration, user_percentage_score) VALUES(?, ?, ?, ?)");
			stmt.setLong(1, user_id);
			stmt.setLong(2, quizID);
			stmt.setDouble(3, timeTaken);
			stmt.setDouble(4, userPercentageScore);
			stmt.executeUpdate();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	/**
	 * Returns username for user who took the quiz
	 * @return String username
	 */
	public long getUserID() {
		return user_id;
	}
	
	// TODO Use Users database to get username
	public String getUsername() throws ClassNotFoundException {
		User user = new User(this.user_id, this.con);
		return user.getUserName();
	}
	
	public long getID() {
		return this.id;
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
	public double getUserPercentageScore() {
		try {
			Statement stmt = con.createStatement();
			String getNameQuery = "SELECT user_percentage_score FROM QuizResults WHERE quiz_id = " + this.quizID + 
			" AND user_id = " + this.user_id + " AND id = " + this.id;
			ResultSet rs = stmt.executeQuery(getNameQuery);
			rs.next();
			return rs.getDouble("user_percentage_score");
		} catch (Exception e) { e.printStackTrace(); } return -1;
	}	

	/**
	 * Returns a Date object representing when
	 * these results were recorded.
	 * @return
	 */
	public Calendar getDateTaken() {
		try {
			Statement stmt = con.createStatement();
			String getNameQuery = "SELECT date_taken FROM QuizResults WHERE quiz_id = " + this.quizID + 
			" AND user_id = " + this.user_id + " AND id = " + this.id;
			ResultSet rs = stmt.executeQuery(getNameQuery);
			rs.next();
			Timestamp ts = rs.getTimestamp("date_taken");
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(ts.getTime());
			return calendar;
		} catch (Exception e) { e.printStackTrace(); } return null;
	}
	
	/**
	 * Returns the time it took for the user
	 * to complete this quiz.
	 * @return
	 */
	public double getTimeTaken() {
		try {
			Statement stmt = con.createStatement();
			String getNameQuery = "SELECT time_duration FROM QuizResults WHERE quiz_id = " + this.quizID + 
			" AND user_id = " + this.user_id + " AND id = " + this.id;
			ResultSet rs = stmt.executeQuery(getNameQuery);
			rs.next();
			return rs.getDouble("time_duration");
		} catch (Exception e) { e.printStackTrace(); } return -1;
	}
	
	/**
	 * Returns an object holding the rank of this result out of all quiz taking instances of this quiz, and the total number of quiz taking instances
	 * @return
	 */
	public RankAndTotal getRankAndTotal() {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT id, @curRank := @curRank + 1 AS rank FROM QuizResults, (SELECT @curRank := 0)r WHERE quiz_id = "+quizID+" ORDER BY user_percentage_score DESC, time_duration ASC");
			ResultSet rs = stmt.executeQuery();
			int count = 0;
			int rank = -1;
			while (rs.next()) {
				count++;
				long rowID = rs.getLong("id");
				if (this.id == rowID) rank = rs.getInt("rank");
			}
			
			return new RankAndTotal(rank, count);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
