package quiz;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

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
		
		// REMOVE THIS AFTER DEBUGGING PHASE IS OVER
		//System.out.println("quiz_id: "+quiz_id);

		ServletContext context = getServletContext(); 
		SelfRefreshingConnection databaseConnection = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		Quiz quiz = new Quiz(id, databaseConnection);
		
		// Creates empty list (for testing) to pass in for a user's quiz taking history
		ArrayList<QuizResults> emptyList = new ArrayList<QuizResults>(0);
		
		// Get quiz info from quiz info db handler
		
		JSONObject jSONquiz = JSONParser.parseQuizIntoJSON(quiz, null, emptyList);
		
		response.getWriter().println(jSONquiz.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		String quiz_id_string = request.getParameter("quiz_id");
		
		boolean isCreating = quiz_id_string.equals("new");
		
		JSONObject newQuizData = JSONParser.getJSONfromRequest(request);
		
		if (isCreating) {
			JSONObject newQuizResponse = new JSONObject();
			try {
				Quiz newQuiz = JSONParser.storeNewQuizWithJSON(newQuizData, (SelfRefreshingConnection)(getServletContext().getAttribute("database_connection")));
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
			ServletContext context = getServletContext(); 
			SelfRefreshingConnection databaseConnection = (SelfRefreshingConnection)context.getAttribute("database_connection");
			try {
				JSONParser.editQuizWithJSON(newQuizData, databaseConnection, quiz_id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
