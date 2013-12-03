package user.message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import user.User;
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
	
	public static ArrayList<Message> getAllMessages(long user_id, SelfRefreshingConnection con) {
		
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT id FROM Messages WHERE user_from_id = " + user_id + " OR user_to_id = " + user_id + orderByDate);
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
