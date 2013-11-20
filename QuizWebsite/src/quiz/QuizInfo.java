package quiz;

import java.util.ArrayList;

/**
 * Stores info to be used when displaying quiz profile page.
 * @author marcelp
 *
 */
public class QuizInfo {

	private String description;
	private String usernameOfCreator;
	private ArrayList<QuizResults> bestScoresAllTime;
	private ArrayList<QuizResults> bestScoresToday;
	private ArrayList<QuizResults> recentScores;
	private double averageScore;
	private boolean isEditable;
	
	/**
	 * Simply keeps track of each of these and has getters for each.
	 * @param description
	 * @param usernameOfCreator
	 * @param pastResults
	 * @param bestScoresAllTime
	 * @param bestScoresToday
	 * @param recentScores
	 * @param averageScore
	 * @param isEditable
	 */
	public QuizInfo(String description, String usernameOfCreator, ArrayList<QuizResults> bestScoresAllTime, 
			ArrayList<QuizResults> bestScoresToday, ArrayList<QuizResults> recentScores, 
			 double averageScore, boolean isEditable) {
		this.setDescription(description);
		this.setUsernameOfCreator(usernameOfCreator);
		this.setBestScoresAllTime(bestScoresAllTime);
		this.setBestScoresToday(bestScoresToday);
		this.setRecentScores(recentScores);
		this.setAverageScore(averageScore);
		this.setEditable(isEditable);
	}

	/**
	 * @param description the description to set
	 */
	private void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param usernameOfCreator the usernameOfCreator to set
	 */
	private void setUsernameOfCreator(String usernameOfCreator) {
		this.usernameOfCreator = usernameOfCreator;
	}

	/**
	 * @return the usernameOfCreator
	 */
	public String getUsernameOfCreator() {
		return usernameOfCreator;
	}

	/**
	 * @param bestScoresAllTime the bestScoresAllTime to set
	 */
	private void setBestScoresAllTime(ArrayList<QuizResults> bestScoresAllTime) {
		this.bestScoresAllTime = bestScoresAllTime;
	}

	/**
	 * @return the bestScoresAllTime
	 */
	public ArrayList<QuizResults> getBestScoresAllTime() {
		return bestScoresAllTime;
	}

	/**
	 * @param bestScoresToday the bestScoresToday to set
	 */
	private void setBestScoresToday(ArrayList<QuizResults> bestScoresToday) {
		this.bestScoresToday = bestScoresToday;
	}

	/**
	 * @return the bestScoresToday
	 */
	public ArrayList<QuizResults> getBestScoresToday() {
		return bestScoresToday;
	}

	/**
	 * @param recentScores the recentScores to set
	 */
	private void setRecentScores(ArrayList<QuizResults> recentScores) {
		this.recentScores = recentScores;
	}

	/**
	 * @return the recentScores
	 */
	public ArrayList<QuizResults> getRecentScores() {
		return recentScores;
	}

	/**
	 * @param averageScore the averageScore to set
	 */
	private void setAverageScore(double averageScore) {
		this.averageScore = averageScore;
	}

	/**
	 * @return the averageScore
	 */
	public double getAverageScore() {
		return averageScore;
	}

	/**
	 * @param isEditable the isEditable to set
	 */
	private void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	/**
	 * @return the isEditable
	 */
	public boolean isEditable() {
		return isEditable;
	}

}
