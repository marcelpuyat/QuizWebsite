package quizPckg;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import databasesPckg.QuizInfoDBHandler;
import databasesPckg.QuizTakingDBHandler;

/**
 * Servlet implementation class QuizServletStub
 */
@WebServlet("/QuizServletStub")
public class QuizServletStub extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizServletStub() {
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
		QuizTakingDBHandler quizDBHandler = (QuizTakingDBHandler)context.getAttribute("quiz_handler");
		
		Quiz quiz = quizDBHandler.getQuizForID(quiz_id);
		
		// Places quiz in session for now, so it is accessible in doPost
		request.getSession().setAttribute("quiz", quiz);
		
		// Creates empty list (for testing) to pass in for a user's quiz taking history
		ArrayList<QuizResults> emptyList = new ArrayList<QuizResults>(0);
		
		// Get quiz info from quiz info db handler
		QuizInfo quizInfo = (new QuizInfoDBHandler()).getQuizInfoGivenID(quiz_id);
		
		JSONObject jSONquiz = JSONParser.parseQuizIntoJSON(quiz, quizInfo, emptyList);
		
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
