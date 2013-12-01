package user.Relation;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import user.User;

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
			
		JSONObject userAInfo = parseUserIntoJSON(userA);
		JSONObject userBInfo = parseUserIntoJSON(userB);
		
		jSONstatus.put("user_a", userAInfo);
		jSONstatus.put("user_b", userBInfo);
		jSONstatus.put("status", status);
		
		return jSONstatus;
	}
	
	/**
	 * This is explained on this page: https://github.com/djoeman84/QuizWebsite/wiki/RelationServlet
	 * @param user
	 * @return
	 */
	private static JSONObject parseUserIntoJSON(User user) {
		try {
			long user_id = user.getUserId();
			String user_username = user.getUserName() == null ? "" : user.getUserName();
			String user_firstname = user.getFirstName() == null ? "" : user.getFirstName();
			String user_lastname = user.getLastName() == null ? "" : user.getLastName();
			
			JSONObject userInfo = new JSONObject();
			userInfo.put("username", user_username);
			userInfo.put("first_name", user_firstname);
			userInfo.put("last_name", user_lastname);
			userInfo.put("id", user_id);
			
			return userInfo;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
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
		relationsInfo.put("user", parseUserIntoJSON(user));
		relationsInfo.put("request_to", getListOfUsersInJSONArray(requestOutward));
		relationsInfo.put("request_from", getListOfUsersInJSONArray(requestInward));
		relationsInfo.put("friends", getListOfUsersInJSONArray(friends));
		relationsInfo.put("blocked_outward", getListOfUsersInJSONArray(blockedOutward));
		relationsInfo.put("blocked_inward", getListOfUsersInJSONArray(blockedInward));
		
		return relationsInfo;
	}
	
	/**
	 * This is explained on this page: https://github.com/djoeman84/QuizWebsite/wiki/RelationServlet
	 * @param users
	 * @return
	 */
	private static JSONArray getListOfUsersInJSONArray(ArrayList<User> users) {
		JSONArray array = new JSONArray();
		
		for (User user : users) {
			array.put(parseUserIntoJSON(user));
		}
		
		return array;
	}
}
