package user.admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

import customObjects.CustomDate;
import customObjects.SelfRefreshingConnection;

public class Announcement {

	private long id;
	private SelfRefreshingConnection con;
	
	public Announcement(long id, SelfRefreshingConnection con) {
		this.id = id;
		this.con = con;
	}
	
	public long getID() {
		return this.id;
	}
	
	public long getAdminID() {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT admin_id FROM Announcements where id = " + this.id);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return rs.getLong(1);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public String getSubject() {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT subject FROM Announcements where id = " + this.id);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return rs.getString(1);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getBody() {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT body FROM Announcements where id = " + this.id);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return rs.getString(1);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public CustomDate getDate() {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT date FROM Announcements where id = " + this.id);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(rs.getTimestamp(1).getTime());
			return new CustomDate(calendar);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public long getAnnouncementID() {
		return this.id;
	}
	
	public static void makeAnnouncement(long admin_id, String subject, String body, SelfRefreshingConnection con) {
		try {
			PreparedStatement stmt = con.prepareStatement("INSERT INTO Announcements VALUES(id, ?, ?, ?, NULL)");
			stmt.setLong(1, admin_id);
			stmt.setString(2, subject);
			stmt.setString(3, body);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteAnnouncement(long announcement_id, SelfRefreshingConnection con) {
		try {
			PreparedStatement stmt = con.prepareStatement("DELETE FROM Announcements WHERE id = " + announcement_id);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void editAnnouncement(long announcement_id, String subject, String body, SelfRefreshingConnection con) {
		try {
			PreparedStatement stmt = con.prepareStatement("UPDATE Announcements SET subject = \"" + subject + "\", body = \"" + body + "\" WHERE id = " + announcement_id);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Announcement> getAllAnnouncements(SelfRefreshingConnection con) {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT id FROM Announcements");
			ResultSet rs = stmt.executeQuery();
			
			ArrayList<Announcement> announcements = new ArrayList<Announcement>();
			
			while (rs.next()) {
				announcements.add(new Announcement(rs.getLong(1), con));
			}
			
			return announcements;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
