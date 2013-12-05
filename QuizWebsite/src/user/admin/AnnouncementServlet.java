package user.admin;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import quiz.JSONParser;

import customObjects.SelfRefreshingConnection;

import user.User;

/**
 * Servlet implementation class AnnouncementServlet
 */
@WebServlet("/AnnouncementServlet")
public class AnnouncementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final String DELETE_ANNOUNCEMENT = "delete";
	private static final String MAKE_ANNOUNCEMENT = "post";
	@SuppressWarnings("unused")
	private static final String EDIT_ANNOUNCEMENT = "edit";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AnnouncementServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext(); 
		SelfRefreshingConnection con = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		HttpSession session = (HttpSession) request.getSession();
		User user = (User) session.getAttribute("user");
		
		JSONObject announcementsJSON = AnnouncementJSONParser.getAllAnnouncementsForUser(user.getUserId(), con);
		response.getWriter().println(announcementsJSON.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext(); 
		SelfRefreshingConnection con = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		String action = request.getParameter("action");
		
		// Delete announcement
		if (action.equals(DELETE_ANNOUNCEMENT)) {
			long announcement_id = Long.parseLong(request.getParameter("announcement_id"));
			Announcement.deleteAnnouncement(announcement_id, con);
		}
		
		// Either make announcement or edit. Either case, get JSON.
		else {
			JSONObject announcementData = JSONParser.getJSONfromRequest(request);

			if (action.equals(MAKE_ANNOUNCEMENT)) {
				AnnouncementJSONParser.makeAnnouncementWithJSON(announcementData, con);
			}
			else/*if(action == EDIT_ANNOUNCEMENT)*/{
				AnnouncementJSONParser.editAnnouncementWithJSON(announcementData, con);
			}
		}
			
		
	}

}
