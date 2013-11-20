package databasesPckg;

import quizPckg.Quiz;

/**
 * Passed around in context and is used by server to get particular quizzes
 * @author marcelp
 *
 */
public class QuizTakingDBHandler {
	
	/**
	 * Stub database made
	 */
	private InsertTestQuizzesIntoMySQL stubDatabase;
	
	/**
	 * This handler will have a stub database ready and initialized
	 */
	public QuizTakingDBHandler() {
		stubDatabase = new InsertTestQuizzesIntoMySQL();
	}
	
	/**
	 * Returns a Quiz object given an ID
	 * @param id String of ID
	 * @return Quiz
	 */
	public Quiz getQuizForID(String id) {
		return stubDatabase.getQuizForID(id);
	}
}
