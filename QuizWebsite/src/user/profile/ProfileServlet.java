package user.profile;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import user.User;
import user.achievement.AchievementJSONParser;
import customObjects.SelfRefreshingConnection;

/**
 * Servlet implementation class ProfileServlet
 */
@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext(); 
		SelfRefreshingConnection con = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		long user_id = Long.parseLong(request.getParameter("user_id"));
		
		User user = new User(user_id, con);
		
		JSONObject profileInfo = new JSONObject();
		profileInfo.put("recent_results", user.getRecentQuizResultsInJSONArray());
		profileInfo.put("achievements", AchievementJSONParser.getAchievementsInJSONGivenUser(user_id, con));
		profileInfo.put("created_quizzes", user.getCreatedQuizzes());
		try {
			profileInfo.put("user_info", user.getPublicJSONSummary());
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		response.getWriter().println(profileInfo.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
