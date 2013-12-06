package user.history;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import quiz.Quiz;
import quiz.QuizResults;
import customObjects.CustomDate;
import customObjects.RankAndTotal;
import customObjects.SelfRefreshingConnection;

public class HistoryJSON {

	/**
	 * Returns full JSON from this: https://github.com/djoeman84/QuizWebsite/wiki/HistoryServlet-spec
	 * @param allResults
	 * @return
	 */
	public static JSONObject parseListIntoJSON(ArrayList<QuizResults> allResults, SelfRefreshingConnection con) {
		JSONObject info = new JSONObject();
		JSONArray array = new JSONArray();
		for (int i = 0; i < allResults.size(); i++) {
			JSONObject result = parseResultIntoJSON(allResults.get(i), con);
			if (result != null)
				array.put(result);
		}
		info.put("history", array);
		return info;
	}
	
	/**
	 * https://github.com/djoeman84/QuizWebsite/wiki/HistoryServlet-spec
	 * @param result
	 * @return
	 */
	private static JSONObject parseResultIntoJSON(QuizResults result, SelfRefreshingConnection con) {
		JSONObject info = new JSONObject();
		
		long quiz_id = result.getQuizID();
		String quiz_name = new Quiz(quiz_id, con).getName();
		long user_id = result.getUserID();
		double time_taken = result.getTimeTaken();
		double user_percentage_score = result.getUserPercentageScore();
		RankAndTotal rt = result.getRankAndTotal();
		int rank = rt.getRank();
		int total = rt.getTotal();
		
		Calendar date_taken_calendar = result.getDateTaken();
			CustomDate date_taken = new CustomDate(date_taken_calendar);
		
		if (quiz_name == null) return null; // If quiz deleted and error somehow
		
		info.put("quiz_name", quiz_name);
		info.put("quiz_id", quiz_id);
		info.put("user_id", user_id);
		info.put("time_taken", time_taken);
		info.put("user_percentage_score", user_percentage_score);
		info.put("rank", rank);
		info.put("total", total);
		
		JSONObject date = new JSONObject();
		
			date.put("year", date_taken.getYear());
			date.put("month", date_taken.getMonth());
			date.put("date", date_taken.getDate());
			date.put("hours", date_taken.getHours());
			date.put("minutes", date_taken.getMinutes());
			date.put("seconds", date_taken.getSeconds());

		info.put("date", date);
		
		return info;
	}
}
