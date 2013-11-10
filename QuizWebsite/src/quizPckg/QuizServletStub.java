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

import org.json.JSONArray;
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
        
		ServletContext context = getServletContext(); // Fakes pulling quiz from database. Instead uses stub listener
		Quiz quiz = (Quiz)context.getAttribute(quiz_id);
		
		JSONObject jSONquiz = parseQuizIntoJSON(quiz);
		
		response.getWriter().println(jSONquiz.toString());

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

	/**
	 * Quiz is formatted differently depending on type.
	 *    
	 * @param quiz A Quiz Object
	 * @return Quiz object formatted as JSON
	 */
	private JSONObject parseQuizIntoJSON(Quiz quiz) {
		
		// Quiz to be passed
		JSONObject jSONquiz = new JSONObject();
				
		ArrayList<Question> questions = quiz.getQuestions();
		
		JSONArray questionsFormatted = new JSONArray();
		
		for (int i = 0; i < questions.size(); i++) {
			JSONObject jSONquestion = parseQuestionIntoJSON(questions.get(i));
			questionsFormatted.put(jSONquestion);
		}
		
		jSONquiz.put("questions", questionsFormatted);

		return jSONquiz;
	}
	
	/**
	 * Parses a question into a JSONObject. Read below to see how each type
	 * is formatted.
	 * @param question
	 * @return
	 */
	private JSONObject parseQuestionIntoJSON(Question question) {
		if (question.getQuestionType() == QuestionTypes.MULTIPLE_CHOICE) {
			return parseMultChoiceIntoJSON((MultipleChoiceQuestion)question);
		}
		
		if (question.getQuestionType() == QuestionTypes.SINGLE_ANSWER) {
			return parseSingleAnswerIntoJSON((SingleAnswerQuestion)question);
		}
		
		// ADD MORE HERE
		else return null;
	}
	
	/**
	 * Given a multiple choice question, returns a JSONObject
	 * formatted as: {type:
	 * 				  data: {prompt:
	 * 	                     options:
	 *  					 correct:
	 *                       score: } 
	 * @param question
	 * @return
	 */
	private JSONObject parseMultChoiceIntoJSON(MultipleChoiceQuestion question) {
		JSONObject questionInfo = new JSONObject();
		questionInfo.accumulate("type", "multiple-choice");
		
		JSONObject q_data = new JSONObject();
		
		q_data.accumulate("prompt", question.getPrompts().get(0));
		q_data.accumulate("options", question.getOptions());
		q_data.accumulate("correct", question.getAnswer());
		q_data.accumulate("score", question.getScore());
		questionInfo.accumulate("data", q_data);
		return questionInfo;
	}
	
	/**
	 * Given a single answer question, returns a JSONObject
	 * formatted as: {type:
	 * 				  data: {prompt:
	 * 	                     correct:
	 *                       score: }              
	 * @param question
	 * @return
	 */
	private JSONObject parseSingleAnswerIntoJSON(SingleAnswerQuestion question) {
		JSONObject questionInfo = new JSONObject();
		questionInfo.accumulate("type", "single-answer");
		
		JSONObject q_data = new JSONObject();
		
		q_data.accumulate("prompt", question.getPrompts().get(0));
		q_data.accumulate("correct", question.getPossibleAnswers());
		q_data.accumulate("score", question.getScore());
		questionInfo.accumulate("data", q_data);
		return questionInfo;
	}
}
