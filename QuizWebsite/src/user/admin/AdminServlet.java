package user.admin;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import customObjects.SelfRefreshingConnection;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	private static final String PROMOTE_USER = "promote_user";
	@SuppressWarnings("unused")
	private static final String DELETE_USER = "remove_user";
	private static final String DELETE_QUIZ = "remove_quiz";
	@SuppressWarnings("unused")
	private static final String CLEAR_HISTORY = "clear_history";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext(); 
		SelfRefreshingConnection con = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		response.getWriter().println(Admin.getSiteStatistics(con));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext(); 
		SelfRefreshingConnection con = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		String action = request.getParameter("action");
		
		String user_id_string = request.getParameter("user_id");
		
		if (user_id_string != null) {
			Long user_id = Long.parseLong(request.getParameter("user_id"));
			if (action.equals(PROMOTE_USER)) {
				Admin.promoteAccount(user_id, con);
			}
			else/*if(action.equals(DELETE_USER)*/{
				Admin.removeUser(user_id, con);
			}
		} else {
			Long quiz_id = Long.parseLong(request.getParameter("quiz_id"));
			if (action.equals(DELETE_QUIZ)) {
				Admin.removeQuiz(quiz_id, con);
			}
			else/*if(action.equals(CLEAR_HISTORY)*/{
				Admin.clearHistory(quiz_id, con);
			}
		}
	}

}
