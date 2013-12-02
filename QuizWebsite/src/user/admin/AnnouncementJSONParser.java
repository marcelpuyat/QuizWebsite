package user.admin;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import user.User;
import user.UserJSONParser;
import customObjects.SelfRefreshingConnection;

public class AnnouncementJSONParser {

	public static JSONObject getAllAnnouncementsForUser(long viewer_id, SelfRefreshingConnection con) {
		JSONObject announcementsJSON = new JSONObject();
		JSONArray announcementsJSONArray = new JSONArray();
		
		ArrayList<Announcement> announcements = Announcement.getAllAnnouncements(con);
		
		for (int i = 0; i < announcements.size(); i++) {
			announcementsJSONArray.put(getJSONForAnnouncement(announcements.get(i), con, viewer_id));
		}
		
		announcementsJSON.put("announcements", announcementsJSONArray);
		return announcementsJSON;
	}
	
	private static JSONObject getJSONForAnnouncement(Announcement announcement, SelfRefreshingConnection con, long viewer_id) {
		JSONObject announcementJSON = new JSONObject();
		User admin = new User(announcement.getAdminID(), con);
		
		announcementJSON.put("admin", UserJSONParser.parseUserIntoJSON(admin));
		announcementJSON.put("subject", announcement.getSubject());
		announcementJSON.put("body", announcement.getBody());
		announcementJSON.put("announcement_id", announcement.getID());
		announcementJSON.put("date", announcement.getDate().toJSON());
		
		boolean canEdit;
		if (viewer_id == admin.getUserId()) {
			canEdit = true;
		}
		else {
			canEdit = false;
		}
		
		announcementJSON.put("viewer_can_edit", canEdit);
		return announcementJSON;
	}
	
	public static void makeAnnouncementWithJSON(JSONObject announcementJSON, SelfRefreshingConnection con) {
		long admin_id = announcementJSON.getLong("user_id");
		String subject = announcementJSON.getString("subject");
		String body = announcementJSON.getString("body");
		
		Announcement.makeAnnouncement(admin_id, subject, body, con);
	}
	
	public static void editAnnouncementWithJSON(JSONObject announcementJSON, SelfRefreshingConnection con) {
		String subject = announcementJSON.getString("subject");
		String body = announcementJSON.getString("body");
		long announcement_id = announcementJSON.getLong("announcement_id");
		
		Announcement.editAnnouncement(announcement_id, subject, body, con);
	}
}
