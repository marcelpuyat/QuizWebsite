package user.message;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import quiz.JSONParser;
import quiz.Quiz;
import user.User;
import customObjects.SelfRefreshingConnection;
import email.Mailer;

/**
 * Servlet implementation class MessageServlet
 */
@WebServlet("/MessageServlet")
public class MessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String DELETE_MESSAGE = "delete";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MessageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext(); 
		SelfRefreshingConnection con = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		/* Respond to query on single user */ // Explained by 1.) in https://github.com/djoeman84/QuizWebsite/wiki/MessageServlet
		String user_id_string = request.getParameter("user_id");
		
		JSONObject msgs = new JSONObject();
		
		if (user_id_string != null) {
			long user_id = Long.parseLong(user_id_string);
			
			JSONArray messageInfo = Message.getAllMessages(user_id, con);
			
			msgs.put("messages", messageInfo);
		}
		
		/* Respond to query on two users */ // Explained by 2.) in https://github.com/djoeman84/QuizWebsite/wiki/MessageServlet
		else {
			long curr_user_id = Long.parseLong(request.getParameter("curr_user_id"));
			long target_user_id = Long.parseLong(request.getParameter("target_user_id"));
			
			JSONArray conversationInfo = MessageJSONParser.getMessageInfoBetweenUsers(curr_user_id, target_user_id, con);
			msgs.put("messages", conversationInfo);
		}
		response.getWriter().println(msgs.toString());

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext(); 
		SelfRefreshingConnection con = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		String message_id_string = request.getParameter("message_id");
		
		// Delete or mark as read
		if (message_id_string != null) {
			long messageID = Long.parseLong(message_id_string);
			String action = request.getParameter("action");
			
			if (action.equals(DELETE_MESSAGE)) {
				Message.deleteMessage(messageID, con);
			} 
			else/*if(action.equals("read"))*/{
				Message.markMessageRead(messageID, con);
			}
			
			
		}
		
		// Send msg
		else {
			JSONObject newMessage = JSONParser.getJSONfromRequest(request);
			
			MessageJSONParser.createMessageFromJSON(newMessage, con);
			JSONObject responseJSON = new JSONObject();
			responseJSON.accumulate("status", "success");
			
			String challenge = request.getParameter("challenge");
			if (challenge != null) {
				
				int percentage = newMessage.getInt("score");
				double time_taken = newMessage.getDouble("time_taken");
				int user_from_id = newMessage.getInt("user_from_id");
				int user_to_id = newMessage.getInt("user_to_id");
				int quiz_id = newMessage.getInt("quiz_id");
				Quiz quiz = new Quiz(quiz_id, con);
				
				boolean emailSent = Mailer.emailChallenge(new User(user_to_id, con), new User(user_from_id, con), percentage, time_taken, quiz.getName());
				System.out.println("EMAIL SENT!");
				responseJSON.accumulate("email", emailSent);
			}
			
			response.getWriter().println(responseJSON.toString());
			
		}
	}

}
