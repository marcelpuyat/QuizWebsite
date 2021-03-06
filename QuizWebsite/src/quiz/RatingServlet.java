package quiz;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import user.User;
import customObjects.SelfRefreshingConnection;

/**
 * Servlet implementation class RatingServlet
 */
@WebServlet("/RatingServlet")
public class RatingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RatingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext(); 
		SelfRefreshingConnection con = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		String quiz_id_string = request.getParameter("quiz_id");
		if (quiz_id_string == null) {
			JSONObject topFiveInfo = new JSONObject();
			JSONArray topFive = Rating.getTopFiveRatedQuizzes(con);
			topFiveInfo.put("top_five", topFive);
			response.getWriter().println(topFiveInfo.toString());
		}
		
		else {
			long quiz_id = Long.parseLong(quiz_id_string);
			Quiz thisQuiz = new Quiz(quiz_id, con);

			String action = request.getParameter("action");
			JSONObject ratingInfo = new JSONObject();

			/* Put user rating */
			if (action.equals("all")) {
				long user_id = Long.parseLong(request.getParameter("user_id"));
				User user = new User(user_id, con);
				int userRating = user.getRatingOnQuiz(quiz_id);
				ratingInfo.put("user_rating", userRating);
				int num_ratings = thisQuiz.getNumberOfRatings();
				ratingInfo.put("num_ratings", num_ratings);
			}
			
			double average_rating = thisQuiz.getAverageRating();
			ratingInfo.put("average_rating", average_rating);
			response.getWriter().println(ratingInfo.toString());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext(); 
		SelfRefreshingConnection con = (SelfRefreshingConnection)context.getAttribute("database_connection");
		String action = request.getParameter("action");
		if (action != null) {
			
			long user_id = Long.parseLong(request.getParameter("user_id"));
			long quiz_id = Long.parseLong(request.getParameter("quiz_id"));
			Quiz thisQuiz = new Quiz(quiz_id, con);

			if (action.equals("rate")) {
				int rating = Integer.parseInt(request.getParameter("rating"));
				thisQuiz.rateQuiz(rating, user_id);	
			}
			
			// Delete
			else {
				thisQuiz.deleteRating(user_id);
			}
		}
		
	}

}
