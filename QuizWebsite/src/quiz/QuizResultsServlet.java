package quiz;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import user.User;
import user.achievement.Achievement;
import customObjects.SelfRefreshingConnection;

/**
 * Servlet implementation class QuizResultsServlet
 */
@WebServlet("/QuizResultsServlet")
public class QuizResultsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizResultsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SelfRefreshingConnection con = (SelfRefreshingConnection) getServletContext().getAttribute("database_connection");

		String practice = request.getParameter("practice");
		
		if (practice != null) {
			long user_id = Long.parseLong(request.getParameter("user_id"));
			Achievement.updatePracticeAchievement(con, user_id);
			return;
		}
		
		JSONObject jSONresults = JSONParser.getJSONfromRequest(request);
		
		HttpSession session = (HttpSession) request.getSession();
		User user = (User) session.getAttribute("user");
		
		// Adds to database automatically
		QuizResults results = JSONParser.parseJSONIntoQuizResults(jSONresults, con, user);
		long quiz_id = results.getQuizID();
		
		// Increment quiz taken frequency
		Quiz quizTaken = new Quiz(quiz_id, con);
		quizTaken.incrementFrequency();
		
		// Update achievement for high score
		Achievement.updateHighScorerAchievement(con, user.getUserId());
		// Update achievement for taking 10 quizzes if so
		Achievement.updateQuizzesTakenAchievement(con, user.getUserId());
	}

}
