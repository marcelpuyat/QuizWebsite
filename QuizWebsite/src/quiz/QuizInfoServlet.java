package quiz;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import user.User;
import customObjects.SelfRefreshingConnection;

/**
 * Servlet implementation class QuizInfoServlet
 */
@WebServlet("/QuizInfoServlet")
public class QuizInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String quiz_idString = request.getParameter("quiz_id");
		int quiz_id = Integer.parseInt(quiz_idString);

		response.setContentType("application/json");

		ServletContext context = getServletContext(); 
		SelfRefreshingConnection databaseConnection = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		HttpSession session = (HttpSession) request.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) user = new User(43, databaseConnection);
		
		QuizInfo quizInfo = new QuizInfo(quiz_id, databaseConnection);
		
		JSONObject jSONinfo = JSONParser.parseQuizInfoIntoJSON(quizInfo, user);
		
		addTagsToJSONQuizInfo(jSONinfo, quiz_id, databaseConnection);
		
		response.getWriter().println(jSONinfo.toString());
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
		// TODO Auto-generated method stub
	}

}
