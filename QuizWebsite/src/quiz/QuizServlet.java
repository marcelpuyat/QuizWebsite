package quiz;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import user.User;
import user.achievement.Achievement;
import customObjects.SelfRefreshingConnection;


/**
 * Servlet implementation class QuizServlet
 */
@WebServlet("/QuizServlet")
public class QuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String quiz_id = request.getParameter("quiz_id");
		int id = Integer.parseInt(quiz_id);
		
		response.setContentType("application/json");

		ServletContext context = getServletContext(); 
		SelfRefreshingConnection databaseConnection = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		Quiz quiz = new Quiz(id, databaseConnection);
		
		// Creates empty list (for testing) to pass in for a user's quiz taking history
		ArrayList<QuizResults> emptyList = new ArrayList<QuizResults>(0);
		
		// Get quiz info from quiz info db handler
		
		JSONObject jSONquiz = JSONParser.parseQuizIntoJSON(quiz, emptyList);
		
		jSONquiz = addTagsToJSONQuizInfo(jSONquiz, id, databaseConnection);
		
		response.getWriter().println(jSONquiz.toString());
	}
	
	private JSONObject addTagsToJSONQuizInfo(JSONObject jSONinfo, int quiz_id, SelfRefreshingConnection con) {
		Tag tag = new Tag(con, quiz_id);
		ArrayList<String> tags = tag.getAllTags();
		
		JSONArray tagStrings = new JSONArray();
		for (int i = 0; i < tags.size(); i++) {
			tagStrings.put(tags.get(i));
		}
		jSONinfo.put("tags", tagStrings);
		return jSONinfo;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		String quiz_id_string = request.getParameter("quiz_id");
		String action = request.getParameter("action");
		
		SelfRefreshingConnection con = (SelfRefreshingConnection)(getServletContext().getAttribute("database_connection"));
		JSONObject newQuizData = JSONParser.getJSONfromRequest(request);

		// DELETE QUIZ
		if (action != null) {
			if (action.equals("delete")) {
				JSONObject deleteResponse = new JSONObject();
				int quiz_id = Integer.parseInt(quiz_id_string);
				try {
					Quiz.deleteQuiz(quiz_id, con);
					deleteResponse.accumulate("status", "success");
					response.getWriter().println(deleteResponse.toString());
					return;
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				deleteResponse.accumulate("status", "failed");
				response.getWriter().println(deleteResponse.toString());
				return;
			}
		}
		
		boolean isCreating = quiz_id_string.equals("new");
				
		JSONObject newQuizResponse = new JSONObject();
		
		/* Quiz Creation */
		if (isCreating) {
			try {
				Quiz newQuiz = JSONParser.storeNewQuizWithJSON(newQuizData, con);
				
				String creator = newQuizData.getString("creator");

				// Add achv for quiz creating depending on num quizzes created of user
				User user = new User(creator, con);
				Achievement.updateQuizCreatedAchievements(con, user.getUserId());
				
				// Add tags
				ArrayList<String> tags = JSONParser.getTagsFromJSONArray(newQuizData.getJSONArray("tags"));
				createTags(tags, con, newQuiz.getID());
				
				// Add id and status
				newQuizResponse.accumulate("status", "success");
				newQuizResponse.accumulate("id", newQuiz.getID());
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				newQuizResponse.accumulate("status", "failed");
			}
			response.getWriter().println(newQuizResponse.toString());
		}
		
		/* Edit quiz */
		else {
			int quiz_id = Integer.parseInt(quiz_id_string);

			try {
				JSONParser.editQuizWithJSON(newQuizData, con, quiz_id);
				Tag oldTags = new Tag(con, quiz_id);
				
				ArrayList<String> newTags = JSONParser.getTagsFromJSONArray(newQuizData.getJSONArray("tags"));
				
				oldTags.deleteTags();
				
				createTags(newTags, con, quiz_id);
				
				newQuizResponse.accumulate("status", "success");
				newQuizResponse.accumulate("id", quiz_id);
				
			} catch (Exception e) {
				e.printStackTrace();
				newQuizResponse.accumulate("status", "failed");
			}
			response.getWriter().println(newQuizResponse.toString());
		}
	}
	
	private void createTags(ArrayList<String> tags, SelfRefreshingConnection con, long quiz_id) {
		@SuppressWarnings("unused")
		Tag tag = new Tag(con, quiz_id, tags);
	}

}
