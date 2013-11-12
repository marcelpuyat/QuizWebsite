package databasesPckg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import quizPckg.Quiz;
import quizPckg.QuizInfo;
import quizPckg.QuizResults;

/**
 * Passed around in context and is used to get QuizInfo objects
 * given a Quiz ID.
 * @author marcelp
 *
 */
public class QuizInfoDBHandler {

	private QuizDatabaseStub database;
	private QuizResultsDBHandler resultsHandler;
	
	public QuizInfoDBHandler() {
		database = new QuizDatabaseStub();
		resultsHandler = new QuizResultsDBHandler();
	}
	
	/**
	 * Sorts all quiz taking instances, and then
	 * returns Quiz Info object given a quiz id.
	 * @param quizID
	 * @return
	 */
	public QuizInfo getQuizInfoGivenID(String quizID) {
		QuizInfo quizInfo = stubSearch(quizID);
		sortQuizInfo(quizInfo);
		return quizInfo;
	}
	
	private QuizInfo stubSearch(String quizID) {
		Quiz quiz = database.getQuizForID(quizID);
		String description = quiz.getDescription();
		String creator = "No one. (not yet implemented)";
		ArrayList<QuizResults> bestFiveOfAllTime = resultsHandler.getBestFiveScoresAllTimeForQuizID(quizID);
		ArrayList<QuizResults> bestFiveOfToday = resultsHandler.getBestFiveScoresTodayForQuizID(quizID);
		ArrayList<QuizResults> mostRecent = resultsHandler.getFiveMostRecentResults(quizID);
		int averageScore = 1000;
		boolean isEditable = false;
		
		QuizInfo quizInfo = new QuizInfo(description, creator, bestFiveOfAllTime, bestFiveOfToday, mostRecent, averageScore, isEditable);
		return quizInfo;
	}
	/**
	 * Sorts according to how spec is in Wiki
	 * @param quizInfo
	 */
	private void sortQuizInfo(QuizInfo quizInfo) {
		sortByScore(quizInfo.getBestScoresAllTime());
		sortByScore(quizInfo.getBestScoresToday());
		sortByDate(quizInfo.getRecentScores());
	}
	
	/**
	 * Sorts by score (decreasing order). If score is equal, then sorts by time taken.
	 * If those are equal, then sets them to be equal.
	 * @param results
	 */
	private void sortByScore(ArrayList<QuizResults> results) {
		Collections.sort(results, new Comparator<QuizResults>() {

			@Override
			public int compare(QuizResults first, QuizResults second) {
				int firstScore = first.getUserScore();
				int secondScore = second.getUserScore();
				
				// These are flipped because this is sorted by decreasing order.
				if (secondScore > firstScore) return 1;
				else if (secondScore < firstScore) return -1;
				
				// If scores equal, compare by time.
				else {
					if (second.getTimeTaken() > first.getTimeTaken()) return 1;
					else if (second.getTimeTaken() < first.getTimeTaken()) return -1;
					else return 0;
				}
			}
		
		});
	}
	
	/**
	 * Sorts by date taken (decreasing order), by using compareTo of Calendar class
	 * @param results QuizResults object
	 */
	private void sortByDate(ArrayList<QuizResults> results) {
		Collections.sort(results, new Comparator<QuizResults>() {

			@Override
			public int compare(QuizResults first, QuizResults second) {
				return (second.getDateTaken().compareTo(first.getDateTaken()));
			}
			
		});
	}
}
