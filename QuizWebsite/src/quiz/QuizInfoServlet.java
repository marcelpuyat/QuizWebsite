package quiz;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		String quiz_id = request.getParameter("quiz_id");
		int id = Integer.parseInt(quiz_id);

		response.setContentType("application/json");

		ServletContext context = getServletContext(); 
		SelfRefreshingConnection databaseConnection = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		HttpSession session = (HttpSession) request.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) user = new User(43, databaseConnection);
		
		QuizInfo quizInfo = new QuizInfo(id, databaseConnection);
		
		JSONObject jSONinfo = JSONParser.parseQuizInfoIntoJSON(quizInfo, user);
		
		response.getWriter().println(jSONinfo.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
