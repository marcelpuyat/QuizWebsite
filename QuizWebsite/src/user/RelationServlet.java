package user;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

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
			User user = new User(Long.parseLong(request.getParameter("user_id")), databaseConnection);
			JSONObject userInfo = RelationJSONParser.parseAllUserRelationInfo(Relation.getAllRequestsOutward(user, databaseConnection), 
																			  Relation.getAllRequestsInward(user, databaseConnection),
																			  Relation.getAllFriends(user, databaseConnection),
																			  Relation.getAllBlockedOutward(user, databaseConnection),
																			  Relation.getAllBlockedInward(user, databaseConnection));
			
			response.getWriter().println(userInfo);
			return;
			
		} else {
			User userA = new User(request.getParameter("user_a_id"), databaseConnection);
			User userB = new User(request.getParameter("user_b_id"), databaseConnection);
			JSONObject userInfo = RelationJSONParser.parseUserComparisonStatus(userA, userB, getServletInfo());
			
			response.getWriter().println(userInfo);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext(); 
		SelfRefreshingConnection databaseConnection = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		User userA = new User(Long.parseLong(request.getParameter("user_a_id")), databaseConnection);
		User userB = new User(Long.parseLong(request.getParameter("user_b_id")), databaseConnection);
		
		String status = request.getParameter("status");
		
		if (status.equals(RelationConstants.FRIEND_REQUESTED)) {
			Relation.requestFriendship(userA, userB, databaseConnection);
		} 
		else if (status.equals(RelationConstants.IS_FRIEND)) {
			Relation.acceptFriendship(userA, userB, databaseConnection);
		} 
		else if (status.equals(RelationConstants.BLOCKED)) {
			Relation.blockUser(userA, userB, databaseConnection);
		}
		else if (status.equals(RelationConstants.UNBLOCK)) {
			Relation.unblockUser(userA, userB, databaseConnection);
		} 
		else/* if (status.equals(RelationConstants.DELETE_FRIEND))*/ {
			Relation.deleteFriend(userA, userB, databaseConnection);
		}
		
	}

}
