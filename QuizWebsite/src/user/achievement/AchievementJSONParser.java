package user.achievement;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import customObjects.SelfRefreshingConnection;

public class AchievementJSONParser {
	
	public static JSONArray getAchievementsInJSONGivenUser(long userID, SelfRefreshingConnection con) {
		ArrayList<Achievement> achievements = Achievement.getUserAchievements(con, userID);
		
		JSONArray arrayOfAchv = new JSONArray();
		
		for (int i = 0; i < achievements.size(); i ++) {
			arrayOfAchv.put(parseSingleAchievement(achievements.get(i)));
		}
		return arrayOfAchv;
	}
	
	private static JSONObject parseSingleAchievement(Achievement achievement) {
		JSONObject achv = new JSONObject();
		achv.put("title", achievement.getTitle());
		achv.put("description", achievement.getDescription());
		return achv;
	}
}
