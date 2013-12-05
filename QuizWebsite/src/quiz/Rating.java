package quiz;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;

import user.achievement.Achievement;
import customObjects.RatingAndQuizID;
import customObjects.SelfRefreshingConnection;

public class Rating {

	public static JSONArray getTopFiveRatedQuizzes(SelfRefreshingConnection con) {
		ArrayList<Quiz> quizzes = new ArrayList<Quiz>(5);
		
		ArrayList<RatingAndQuizID> ratingAndQuizIDs = new ArrayList<RatingAndQuizID>();
		
		ArrayList<Integer> allQuizIDs = Achievement.getIDsOfQuizzes(con);
		
		for (int i = 0; i < allQuizIDs.size(); i++) {
			Quiz thisQuiz = new Quiz((long)allQuizIDs.get(i), con);
			Double rating = new Double(thisQuiz.getAverageRating());
			
			if (rating.doubleValue() < 0) continue;
			RatingAndQuizID rAndQ = new RatingAndQuizID(rating, allQuizIDs.get(i));
			ratingAndQuizIDs.add(rAndQ);
		}
		
		Collections.sort(ratingAndQuizIDs);
		
		int limit = ratingAndQuizIDs.size() < 5 ? ratingAndQuizIDs.size() : 5;
		
		for (int i = 0; i < limit; i++) {
			quizzes.add(new Quiz(ratingAndQuizIDs.get(i).getQuizID().longValue(), con));
		}
		
		return parseQuizListIntoJSON(quizzes);
	}
	
	private static JSONArray parseQuizListIntoJSON(ArrayList<Quiz> quizzes) {
		JSONArray quizzesInfo = new JSONArray();
		for (int i = 0; i < quizzes.size(); i++) {
			JSONObject quizInfo = parseQuizIntoJSON(quizzes.get(i));
			quizzesInfo.put(quizInfo);
		}
		return quizzesInfo;
	}
	
	private static JSONObject parseQuizIntoJSON(Quiz quiz) {
		JSONObject info = new JSONObject();
		String quiz_name = quiz.getName();
		String creator = quiz.getCreator();
		double average_rating = quiz.getAverageRating();
		int num_ratings = quiz.getNumberOfRatings();
		long quiz_id = quiz.getID();
		
		info.put("num_ratings", num_ratings);
		info.put("creator", creator);
		info.put("average_rating", average_rating);
		info.put("quiz_name", quiz_name);
		info.put("quiz_id", quiz_id);
		
		return info;
	}
}
