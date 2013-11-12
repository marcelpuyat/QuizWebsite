package databasesPckg;

import java.util.ArrayList;
import java.util.Calendar;

import quizPckg.QuizResults;

public class QuizResultsDBHandler {

	private QuizResultsDatabaseStub database;
	
	public QuizResultsDBHandler() {
		database = new QuizResultsDatabaseStub();
	}
	
	// ALL OF THESE ARE STUB IMPLEMENTATIONS!!!
	// EXPAND THIS STUB BY PUTTING TESTS INTO RESULTS DB STUB
	public ArrayList<QuizResults> getQuizResultsForQuizIDAndUsername(String quizID, String username) {
		ArrayList<QuizResults> results = new ArrayList<QuizResults>();
		QuizResults result;
		if (quizID.equals("000000"))
			result = new QuizResults("Marcel", "000000", 3, 3, Calendar.getInstance(), 50);
		else
			result = new QuizResults("Joe", "000001", 2, 2, Calendar.getInstance(), 40);
		results.add(result);
		return results;
	}
	
	public ArrayList<QuizResults> getBestFiveScoresAllTimeForQuizID(String quizID) {
		ArrayList<QuizResults> results = new ArrayList<QuizResults>();
		QuizResults result;
		if (quizID.equals("000000"))
			result = new QuizResults("Marcel", "000000", 3, 3, Calendar.getInstance(), 50);
		else
			result = new QuizResults("Joe", "000001", 2, 2, Calendar.getInstance(), 40);
		results.add(result);
		return results;
	}
	
	public ArrayList<QuizResults> getBestFiveScoresTodayForQuizID(String quizID) {
		Calendar today = Calendar.getInstance();
		ArrayList<QuizResults> results = new ArrayList<QuizResults>();
		QuizResults result;
		if (quizID.equals("000000"))
			result = new QuizResults("Marcel", "000000", 3, 3, Calendar.getInstance(), 50);
		else
			result = new QuizResults("Joe", "000001", 2, 2, Calendar.getInstance(), 40);
		results.add(result);
		return results;
	}
	
	public ArrayList<QuizResults> getFiveMostRecentResults(String quizID) {
		ArrayList<QuizResults> results = new ArrayList<QuizResults>();
		QuizResults result;
		if (quizID.equals("000000"))
			result = new QuizResults("Marcel", "000000", 3, 3, Calendar.getInstance(), 50);
		else
			result = new QuizResults("Joe", "000001", 2, 2, Calendar.getInstance(), 40);
		results.add(result);
		return results;
	}
}
