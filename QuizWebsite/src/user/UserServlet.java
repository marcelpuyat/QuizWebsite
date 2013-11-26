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
		
		/* check availability */
		if (api.equals("availability")) {
			String username = request.getParameter("username");
			responseJSON.accumulate("available", !Users.usernameExists(username, databaseConnection));
			responseJSON.accumulate("username", username);
		} else {
			responseJSON.accumulate("result", "api not found");
		}
		
		
		response.getWriter().println(responseJSON.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext(); 
		Connection databaseConnection = (Connection)context.getAttribute("database_connection");
		
		response.setContentType("application/json");
		JSONObject responseJSON = new JSONObject();
		
		/* API SWITCH */
		String api = request.getParameter("api");
		
		/* create new user */
		if (api.equals("create_user")) {
			String username = request.getParameter("new-username");
			String password = request.getParameter("new-password");
			String pass_chk = request.getParameter("new-password-redundant");
			if (Users.createUser(username, password, databaseConnection) &&
					password.equals(pass_chk)) {
				responseJSON.accumulate("status", "success");
			} else {
				responseJSON.accumulate("status", "failure");
			}
		}
		
		/* login */
		else if (api.equals("login")) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			if (Users.usernameExists(username,databaseConnection)) {
				User u = new User(username, databaseConnection);
				if (u.matchesPassword(password)) {
					responseJSON.accumulate("status", "success");
					responseJSON.accumulate("user_info", u.getPublicJSONSummary());
					request.getSession().setAttribute("user", u);
					response.sendRedirect("Home.jsp");
				} else {
					responseJSON.accumulate("status", "password does not match");
				}
			} else {
				responseJSON.accumulate("status", "username does not exist");
			}
		
		}
		
		

		
		
		response.getWriter().println(responseJSON.toString());
		
	}

}
