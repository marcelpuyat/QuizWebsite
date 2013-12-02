package user;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserJSONParser {

	/**
	 * This is explained on this page: https://github.com/djoeman84/QuizWebsite/wiki/RelationServlet
	 * @param users
	 * @return
	 */
	public static JSONArray getListOfUsersInJSONArray(ArrayList<User> users) {
		JSONArray array = new JSONArray();
		
		for (User user : users) {
			array.put(parseUserIntoJSON(user));
		}
		
		return array;
	}
	
	/**
	 * This is explained on this page: https://github.com/djoeman84/QuizWebsite/wiki/RelationServlet
	 * @param user
	 * @return
	 */
	public static JSONObject parseUserIntoJSON(User user) {
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
}