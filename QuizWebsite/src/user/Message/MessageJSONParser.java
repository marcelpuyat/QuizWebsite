package user.message;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import user.UserJSONParser;
import customObjects.MessageDate;
import customObjects.SelfRefreshingConnection;

public class MessageJSONParser {

	/**
	 * Return JSON with sent msgs and received msgs for argument user
	 * @param user_id
	 * @param con
	 * @return
	 */
	/*package*/static JSONObject getMessageInfoGivenUser(long user_id, SelfRefreshingConnection con) {
		JSONObject info = new JSONObject();
		
		ArrayList<Message> receivedMsgs = Message.getAllReceivedMessages(user_id, con);
		ArrayList<Message> sentMsgs = Message.getAllSentMessages(user_id, con);
		
		JSONArray receivedMsgsJSON = new JSONArray();
		JSONArray sentMsgsJSON = new JSONArray();
		
		for (int i = 0; i < receivedMsgs.size(); i++) {
			receivedMsgsJSON.put(getJSONForReceivedMessage(receivedMsgs.get(i)));
		}
		
		for (int i = 0; i < sentMsgs.size(); i++) {
			sentMsgsJSON.put(getJSONForSentMessage(sentMsgs.get(i)));
		}
		
		info.put("received", receivedMsgsJSON);
		info.put("sent", sentMsgsJSON);
		
		return info;
	}
	
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
		JSONObject msgInfo = new JSONObject();
		msgInfo.put("subject", msg.getSubject());
		msgInfo.put("body", msg.getBody());
		msgInfo.put("date", parseDate(msg.getDate()));
		msgInfo.put("message_id", msg.getMessageID());
		msgInfo.put("was_read", msg.hasBeenRead());
		
		if (sentMsg) {
			msgInfo.put("to_user", UserJSONParser.parseUserIntoJSON(msg.getReceiver()));
		}
		
		else /*if(receivedMsg)*/ {
			msgInfo.put("from_user", UserJSONParser.parseUserIntoJSON(msg.getSender()));
		}
		
		return msgInfo;
	}
	
	/**
	 * Returns JSON of date (specified in MessageServlet spec) given a Calendar object (using Message.getDate())
	 * @param calendar
	 * @return
	 */
	private static JSONObject parseDate(Calendar calendar) {
		JSONObject dateInfo = new JSONObject();
		MessageDate msgDate = new MessageDate(calendar);
		
		dateInfo.put("year", msgDate.getYear());
		dateInfo.put("month", msgDate.getMonth());
		dateInfo.put("day", msgDate.getDate());
		dateInfo.put("hours", msgDate.getHours());
		dateInfo.put("minutes", msgDate.getMinutes());
		return null;
	}
	
	/**
	 * Returns all messages between the two users.
	 * @param curr_user_id
	 * @param target_user_id
	 * @param con
	 * @return
	 */
	/*package*/static JSONObject getMessageInfoBetweenUsers(long curr_user_id, long target_user_id, SelfRefreshingConnection con) {
		JSONObject info = new JSONObject();
		
		ArrayList<Message> receivedMsgs = Message.getAllMessagesFromUser(curr_user_id, target_user_id, con);
		ArrayList<Message> sentMsgs = Message.getAllMessagesFromUser(target_user_id, curr_user_id, con);
		
		JSONArray receivedMsgsJSON = new JSONArray();
		JSONArray sentMsgsJSON = new JSONArray();
		
		for (int i = 0; i < receivedMsgs.size(); i++) {
			receivedMsgsJSON.put(getJSONForReceivedMessage(receivedMsgs.get(i)));
		}
		
		for (int i = 0; i < sentMsgs.size(); i++) {
			sentMsgsJSON.put(getJSONForSentMessage(sentMsgs.get(i)));
		}
		
		info.put("received", receivedMsgsJSON);
		info.put("sent", sentMsgsJSON);
		
		return info;
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
