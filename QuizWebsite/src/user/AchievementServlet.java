package user;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import customObjects.SelfRefreshingConnection;

/**
 * Servlet implementation class AchievementServlet
 */
@WebServlet("/AchievementServlet")
public class AchievementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AchievementServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext(); 
		SelfRefreshingConnection con = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		long userID = Long.parseLong(request.getParameter("user_id"));
		
		ArrayList<Achievement> achievements = Achievement.getUserAchievements(con, userID);
		
		// TODO Finish and fill spec online
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
