package quizPckg;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;


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
		response.setContentType("application/json");
		
		// REMOVE THIS AFTER DEBUGGING PHASE IS OVER
		//System.out.println("quiz_id: "+quiz_id);

		ServletContext context = getServletContext(); 
		Connection databaseConnection = (Connection)context.getAttribute("database_connection");
		
		Quiz quiz = new Quiz(quiz_id, databaseConnection);
		
		// Places quiz in session for now, so it is accessible in doPost
		request.getSession().setAttribute("quiz", quiz);
		
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
		/* Forms JSONObject from request */
		StringBuilder json_str_response = new StringBuilder();
		BufferedReader br = request.getReader();
		String str;
		while( (str = br.readLine()) != null ){
			json_str_response.append(str);
	    }  
		JSONObject jSONanswer = new JSONObject(json_str_response.toString());
		
		/* Get quiz from Database given ID */   // For now just uses session
		Quiz quiz = (Quiz)request.getSession().getAttribute("quiz");
		
		QuizResults results = JSONParser.parseJSONIntoQuizResults(jSONanswer, quiz);
		
		JSONObject jSONresults = JSONParser.parseQuizResultsIntoJSON(results);
		
		response.getWriter().println(jSONresults.toString());
		
	}

}
