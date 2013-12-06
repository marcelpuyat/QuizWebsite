package user.message;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import user.UserJSONParser;
import customObjects.SelfRefreshingConnection;

public class MessageJSONParser {
	
	/**
	 * Get JSON for sent msg as in spec (Github)
	 * @param msg
	 * @return
	 */
	private static JSONObject getJSONForSentMessage(Message msg) {
		return getJSONForMessage(msg, true);
	}
	
	/**
	 * Get JSON for received msg as in spec (Github)
	 * @param msg
	 * @return
	 */
	private static JSONObject getJSONForReceivedMessage(Message msg) {
		return getJSONForMessage(msg, false);
	}
	
	/**
	 * Get JSON for msg. boolean is true if sent msg, false if received msg.
	 * @param msg
	 * @param sentMsg
	 * @return
	 */
	private static JSONObject getJSONForMessage(Message msg, boolean sentMsg) {
		System.out.println("Fetching message");

		/* Time this section */
		long start_time = System.nanoTime();
		JSONObject msgInfo = msg.toJSON();
		long total_time = System.nanoTime() - start_time;
		/* end of timed section */
		
		System.out.println(total_time);
		
		if (sentMsg) {
			msgInfo.put("type", "sent");
			msgInfo.put("user", UserJSONParser.parseUserIntoJSON(msg.getReceiver()));
		}
		
		else /*if(receivedMsg)*/ {
			msgInfo.put("type", "received");
			msgInfo.put("user", UserJSONParser.parseUserIntoJSON(msg.getSender()));
		}
		
		return msgInfo;
	}
	
	/**
	 * Returns all messages between the two users.
	 * @param curr_user_id
	 * @param target_user_id
	 * @param con
	 * @return
	 */
	/*package*/static JSONArray getMessageInfoBetweenUsers(long curr_user_id, long target_user_id, SelfRefreshingConnection con) {
		
		ArrayList<Message> msgs = Message.getAllMessagesFromAndToUser(curr_user_id, target_user_id, con);
		
		JSONArray messages = new JSONArray();
		
		for (int i = 0; i < msgs.size(); i++) {
			Message msg = msgs.get(i);
			if (msg.getSender().getUserId() == curr_user_id) {
				messages.put(getJSONForSentMessage(msg));
			} else {
				messages.put(getJSONForReceivedMessage(msg));
			}
		}
			
		return messages;
	}
	
	/**
	 * Adds msg to database
	 * @param messageJSON
	 * @param con
	 */
	/*package*/static void createMessageFromJSON(JSONObject messageJSON, SelfRefreshingConnection con) {
		long user_from_id = messageJSON.getLong("user_from_id");
		long user_to_id = messageJSON.getLong("user_to_id");
		String subject = messageJSON.getString("subject");
		String body = messageJSON.getString("body");
		
		Message.sendMessage(con, subject, body, user_from_id, user_to_id);
	}
}
