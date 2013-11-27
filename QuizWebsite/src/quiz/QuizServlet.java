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
		JSONObject newQuiz = JSONParser.getJSONfromRequest(request);
		
		try {
			JSONParser.storeNewQuizWithJSON(newQuiz, (SelfRefreshingConnection)(getServletContext().getAttribute("database_connection")));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
