package user.home;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import quiz.Quiz;
import user.User;
import user.achievement.Achievement;
import user.achievement.AchievementJSONParser;
import customObjects.SelfRefreshingConnection;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet("/HomeServlet")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomeServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext(); 
		SelfRefreshingConnection con = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		long user_id = Long.parseLong(request.getParameter("user_id"));
		User user = new User(user_id, con);
		
		JSONArray recentResults = user.getRecentQuizResultsInJSONArray();
		JSONArray createdQuizzes = user.getCreatedQuizzes();
		JSONArray friendsResults = user.getFriendsLatestResults();
		JSONArray popularQuizzes = Quiz.getMostPopularQuizzes(con);
		JSONArray achievements = AchievementJSONParser.getAchievementsInJSONGivenUser(user_id, con);
		JSONArray achvNotEarned = Achievement.getUserAchievementsNotEarned(con, user_id);
		
		JSONObject responseJSON = new JSONObject();
		responseJSON.put("recent_results", recentResults);
		responseJSON.put("created_quizzes", createdQuizzes);
		responseJSON.put("friend_results", friendsResults);
		responseJSON.put("popular_quizzes", popularQuizzes);
		responseJSON.put("achievements_earned", achievements);
		responseJSON.put("achievements_not_earned", achvNotEarned);

		response.getWriter().println(responseJSON.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
