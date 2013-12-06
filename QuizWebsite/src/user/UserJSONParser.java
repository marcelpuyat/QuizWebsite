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
			return user.getPublicJSONSummary();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
