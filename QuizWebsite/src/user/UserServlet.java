package user;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext(); 
		Connection databaseConnection = (Connection)context.getAttribute("database_connection");
		
		response.setContentType("application/json");
		JSONObject responseJSON = new JSONObject();
		
		/* API SWITCH */
		String api = request.getParameter("api");
		
		/* login */
		if (api.equals("login")) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			if (Users.usernameExists(username,databaseConnection)) {
				User u = new User(username, databaseConnection);
				if (u.matchesPassword(password)) {
					responseJSON.accumulate("status", "success");
				} else {
					responseJSON.accumulate("status", "password does not match");
				}
			} else {
				responseJSON.accumulate("status", "username does not exist");
			}
		
		}
		
		/* check availability */
		else if (api.equals("availability")) {
			String username = request.getParameter("username");
			responseJSON.accumulate("available", !Users.usernameExists(username, databaseConnection));
			responseJSON.accumulate("username", username);
		}
		
		
		response.getWriter().println(responseJSON.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		response.setContentType("application/json");
		JSONObject responseJSON = new JSONObject();
		
		ServletContext context = getServletContext(); 
		Connection databaseConnection = (Connection)context.getAttribute("database_connection");
		
		if (Users.createUser(username, password, databaseConnection)) {
			responseJSON.accumulate("status", "success");
		} else {
			responseJSON.accumulate("status", "failure");
		}
		response.getWriter().println(responseJSON.toString());
		
	}

}
