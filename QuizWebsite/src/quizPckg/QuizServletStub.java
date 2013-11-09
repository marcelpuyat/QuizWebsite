package quizPckg;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

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
		System.out.println("quiz_id: "+quiz_id);
		//add first question
		JSONObject quiz_obj = new JSONObject();
		
		
		JSONObject question = new JSONObject();
		question.accumulate("type", "multiple-choice");
		JSONObject q_data   = new JSONObject();
		q_data.accumulate("prompt", "what class is this assignment for?");
		q_data.accumulate("options", Arrays.asList("CS110","CS108","CS987123"));
		q_data.accumulate("correct", 1);
		question.accumulate("data", q_data);
		
		quiz_obj.accumulate("questions", Arrays.asList(question));
		
		
		response.getWriter().println(quiz_obj.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder json_str_response = new StringBuilder();
		BufferedReader br = request.getReader();
		String str;
		while( (str = br.readLine()) != null ){
			json_str_response.append(str);
	    }  
		JSONObject jObj = new JSONObject(json_str_response.toString());
		response.getWriter().println(jObj.toString()); //echo
		
	}

}
