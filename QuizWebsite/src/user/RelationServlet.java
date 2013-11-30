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
 * Servlet implementation class RelationServlet
 */
@WebServlet("/RelationServlet")
public class RelationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RelationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext(); 
		SelfRefreshingConnection databaseConnection = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		if (request.getParameter("user_id") != null) {
			User user = new User(request.getParameter("user_id"), databaseConnection);
			// TODO process GET request 2 from spec
		} else {
			User userA = new User(request.getParameter("user_a_id"), databaseConnection);
			User userB = new User(request.getParameter("user_b_id"), databaseConnection);
			// TODO process GET request 1 from spec
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext(); 
		SelfRefreshingConnection databaseConnection = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		User userA = new User(request.getParameter("user_a_id"), databaseConnection);
		User userB = new User(request.getParameter("user_b_id"), databaseConnection);
		String status = request.getParameter("status");
		if (status.equals(RelationConstants.FRIEND_REQUESTED)) {
			// TODO Process POST 1 from spec
		} 
		else if (status.equals(RelationConstants.IS_FRIEND)) {
			// TODO Process POST 2 from spec
		} 
		else if (status.equals(RelationConstants.BLOCKED)) {
			// TODO Process POST 3 from spec
		}
		else if (status.equals("UNBLK")) {
			// TODO Process POST 4 from spec
		} 
		else/* if (status.equals("DELETE"))*/ {
			// TODO Process POST 5 from speec
		}
		
	}

}
