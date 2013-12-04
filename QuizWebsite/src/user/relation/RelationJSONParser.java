package user.relation;

import java.util.ArrayList;

import org.json.JSONObject;

import user.User;
import user.UserJSONParser;

public class RelationJSONParser {
	
	/**
	 * Returns JSON for status of user A to user B. This is explained on this page: https://github.com/djoeman84/QuizWebsite/wiki/RelationServlet
	 * @param userA
	 * @param userB
	 * @param status
	 * @return
	 */
	public static JSONObject parseUserComparisonStatus(User userA, User userB, String status) {
		JSONObject jSONstatus = new JSONObject();
			
		JSONObject userAInfo = UserJSONParser.parseUserIntoJSON(userA);
		JSONObject userBInfo = UserJSONParser.parseUserIntoJSON(userB);
		
		jSONstatus.put("user_a", userAInfo);
		jSONstatus.put("user_b", userBInfo);
		jSONstatus.put("status", status);
		
		return jSONstatus;
	}
	
	
	/**
	 * Returns JSON for all relations info given a user. This is explained on this page: https://github.com/djoeman84/QuizWebsite/wiki/RelationServlet
	 * @param requestOutward
	 * @param requestInward
	 * @param friends
	 * @param blockedOutward
	 * @param blockedInward
	 * @return
	 */
	public static JSONObject parseAllUserRelationInfo(User user, ArrayList<User> requestOutward, ArrayList<User> requestInward,
			ArrayList<User> friends, ArrayList<User> blockedOutward, ArrayList<User> blockedInward) {
		
		JSONObject relationsInfo = new JSONObject();
		relationsInfo.put("user", UserJSONParser.parseUserIntoJSON(user));
		relationsInfo.put("request_to", UserJSONParser.getListOfUsersInJSONArray(requestOutward));
		relationsInfo.put("request_from", UserJSONParser.getListOfUsersInJSONArray(requestInward));
		relationsInfo.put("friends", UserJSONParser.getListOfUsersInJSONArray(friends));
		relationsInfo.put("blocked_outward", UserJSONParser.getListOfUsersInJSONArray(blockedOutward));
		relationsInfo.put("blocked_inward", UserJSONParser.getListOfUsersInJSONArray(blockedInward));
		
		return relationsInfo;
	}
	
	
}
