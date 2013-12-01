package user.message;

import org.json.JSONObject;

import customObjects.SelfRefreshingConnection;

public class MessageJSONParser {

	public static void createMessageFromJSON(JSONObject messageJSON, SelfRefreshingConnection con) {
		long user_from_id = messageJSON.getLong("user_from_id");
		long user_to_id = messageJSON.getLong("user_to_id");
		String subject = messageJSON.getString("subject");
		String body = messageJSON.getString("body");
		
		Message.sendMessage(con, subject, body, user_from_id, user_to_id);
	}
}
