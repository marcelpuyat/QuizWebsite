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
		// THIS IS ONLY A TEST. DOES NOT SERVE ANY PURPOSE
		ServletContext context = getServletContext(); 
		SelfRefreshingConnection databaseConnection = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		User marcel = new User(49, databaseConnection);
		User joe = new User(53, databaseConnection);
		
		Relation.unblockOrDelete(marcel, joe, databaseConnection, true);
		
		ArrayList<User> friends = Relation.getAllFriends(marcel, databaseConnection);
		for (User friend : friends) {
			try {
				System.out.println(friend.getUserName());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// NOT YET IMPLEMENTED
		ServletContext context = getServletContext(); 
		SelfRefreshingConnection databaseConnection = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		
	}

}
