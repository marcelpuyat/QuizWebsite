package user.message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import user.User;
import user.UserJSONParser;
import customObjects.CustomDate;
import customObjects.SelfRefreshingConnection;

public class Message {

	private SelfRefreshingConnection con;
	private long id;
	private static final String orderByDate = " ORDER BY date DESC";
	
	public Message(SelfRefreshingConnection con, long id) {
		this.con = con;
		this.id = id;
	}
	
	public static void deleteMessage(long id, SelfRefreshingConnection con) {
		try {
			PreparedStatement stmt = con.prepareStatement("DELETE FROM Messages WHERE id = " + id);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void markMessageRead(long id, SelfRefreshingConnection con) {
		try {
			PreparedStatement stmt = con.prepareStatement("UPDATE Messages SET was_read = TRUE WHERE id = " + id);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getSubject() {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT subject FROM Messages WHERE id = " + this.id);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return rs.getString(1);
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
	}
	
	public long getMessageID() {
		return this.id;
	}
	
	public String getBody() {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT body FROM Messages WHERE id = " + this.id);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return rs.getString(1);
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
	}
	
	public User getSender() {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT user_from_id FROM Messages WHERE id = " + this.id);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			long user_id = rs.getLong(1);
			return new User(user_id, con);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public User getReceiver() {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT user_to_id FROM Messages WHERE id = " + this.id);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			long user_id = rs.getLong(1);
			return new User(user_id, con);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public CustomDate getDate() {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT date FROM Messages WHERE id = " + this.id);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			Timestamp ts = rs.getTimestamp(1);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(ts.getTime());
			return new CustomDate(calendar);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean hasBeenRead() {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT was_read FROM Messages WHERE id = " + this.id);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			boolean wasRead = rs.getBoolean(1);
			return wasRead;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public JSONObject toJSON() {
		JSONObject info = new JSONObject();
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM Messages WHERE id = " + this.id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				info.put("subject", rs.getString("subject"));
				info.put("body", rs.getString("body"));
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(rs.getTimestamp("date").getTime());
				
				info.put("date", (new CustomDate(calendar)).toJSON());
				info.put("message_id", this.id);
				info.put("was_read", rs.getBoolean("was_read"));
				return info;
			}
			else return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	/* Static methods */
	
	public static void sendMessage(SelfRefreshingConnection con, String subject, String body, long user_from_id, long user_to_id) {
		try {
			PreparedStatement stmt = con.prepareStatement("INSERT INTO Messages VALUES (id, ?, ?, ?, ?, NULL, FALSE)");
			stmt.setLong(1, user_from_id);
			stmt.setLong(2, user_to_id);
			stmt.setString(3, subject);
			stmt.setString(4, body);
			
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static JSONArray getAllMessages(long user_id, SelfRefreshingConnection con) {
		
		try {
			String getAllMessagesSQL = 
				"SELECT Messages.body, Messages.subject, Messages.was_read, Messages.date, Messages.user_from_id, Messages.user_to_id, Messages.id, Users.first_name, Users.last_name, Users.username, Users.profile_picture, Users.id FROM Messages, Users  WHERE (Messages.user_from_id = ? AND Users.id=Messages.user_to_id)  OR (Messages.user_to_id = ?  AND Users.id = Messages.user_from_id) ORDER BY Messages.date DESC";
			PreparedStatement stmt = con.prepareStatement(getAllMessagesSQL);
			stmt.setLong(1, user_id);
			stmt.setLong(2, user_id);
			ResultSet rs = stmt.executeQuery();
			
			JSONArray messages = new JSONArray();
			while (rs.next()) {
				JSONObject info = new JSONObject();
				info.put("subject", rs.getString("Messages.subject"));
				info.put("body", rs.getString("Messages.body"));
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(rs.getTimestamp("Messages.date").getTime());
				
				info.put("date", (new CustomDate(calendar)).toJSON());
				info.put("message_id", rs.getLong("Messages.id"));
				info.put("was_read", rs.getBoolean("Messages.was_read"));
				
				if (user_id == rs.getLong("Messages.user_from_id")) {
					info.put("type", "sent");
				}
				else {
					info.put("type", "received");
				}
				JSONObject userJSON = new JSONObject();


				userJSON.accumulate("username",rs.getString("Users.username"));
				userJSON.accumulate("first_name",rs.getString("Users.first_name"));
				userJSON.accumulate("last_name",rs.getString("Users.last_name"));
				userJSON.accumulate("profile_picture",rs.getString("Users.profile_picture"));
				userJSON.accumulate("id",rs.getLong("Users.id"));

				info.put("user", userJSON);
				messages.put(info);
			}
			
			return messages;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Message> getAllMessagesFromAndToUser(long receiver_user_id, long sender_user_id, SelfRefreshingConnection con) {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT id FROM Messages WHERE (user_from_id = " + sender_user_id + " AND user_to_id = " + receiver_user_id + ") OR (user_from_id = " + receiver_user_id + " AND user_to_id = " + sender_user_id + orderByDate);
			ResultSet rs = stmt.executeQuery();
			
			ArrayList<Message> messages = new ArrayList<Message>();
			
			while (rs.next()) {
				messages.add(new Message(con, rs.getLong(1)));
			}
			
			return messages;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
