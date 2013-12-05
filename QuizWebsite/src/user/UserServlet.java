package user;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import quiz.JSONParser;
import customObjects.SelfRefreshingConnection;

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
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext(); 
		SelfRefreshingConnection databaseConnection = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		response.setContentType("application/json");
		JSONObject responseJSON = new JSONObject();
		
		/* API SWITCH */
		String api = request.getParameter("api");
		
		/* check availability */
		if (api.equals("availability")) {
			String username = request.getParameter("username");
			try {
				responseJSON.accumulate("available", !Users.usernameExists(username, databaseConnection));
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
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
		SelfRefreshingConnection databaseConnection = (SelfRefreshingConnection)context.getAttribute("database_connection");
		
		response.setContentType("application/json");
		JSONObject responseJSON = new JSONObject();
		
		/* API SWITCH */
		String api = request.getParameter("api");
		
		if (api.equals("logout")) {
			request.getSession().setAttribute("user", null);
			responseJSON.accumulate("status", "success");
			response.getWriter().println(responseJSON.toString());
			return;
		}
		
		/* create new user */
		if (api.equals("create_user")) {
			JSONObject usrJSON = JSONParser.getJSONfromRequest(request);
			String username = usrJSON.getString("new_username");
			String password = usrJSON.getString("new_password");
			String pass_chk = usrJSON.getString("new_password_redundant");
			try {
				if (Users.createUser(username, password, databaseConnection) &&
						password.equals(pass_chk)) {
					responseJSON.accumulate("status", "success");
					response.getWriter().println(responseJSON.toString());
					return;
				} else {
					responseJSON.accumulate("status", "failure");
					response.getWriter().println(responseJSON.toString());
					return;
				}
			}
			catch (JSONException e) {e.printStackTrace();}
			catch (ClassNotFoundException e) {e.printStackTrace();}
		}
		
		/* login */
		else if (api.equals("login")) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String forward_to = request.getParameter("forward_to");
			try {
				if (Users.usernameExists(username,databaseConnection)) {
					User u = new User(username, databaseConnection);
					if (u.matchesPassword(password)) {
						responseJSON.accumulate("status", "success");
						responseJSON.accumulate("user_info", u.getPublicJSONSummary());
						request.getSession().setAttribute("user", u);
						if (forward_to != null && forward_to.equals("settings")) {
							response.sendRedirect("Settings.jsp");
						} else {
							response.sendRedirect("Home.jsp");
						}
					} else {
						responseJSON.accumulate("status", "password does not match");
						response.sendRedirect("Home.jsp");
					}
				} else {
					responseJSON.accumulate("status", "username does not exist");
					response.sendRedirect("Home.jsp");
				}
			}
			catch (JSONException e) {e.printStackTrace();}
			catch (ClassNotFoundException e) {e.printStackTrace();}
		
		}
		
		
		/* set property name */
		else if (api.equals("update")) {
			try {
				JSONObject usrJSON = JSONParser.getJSONfromRequest(request);
				User update_user = (User) request.getSession().getAttribute("user");
				if (update_user == null || !update_user.existsInDB()) {
					responseJSON.accumulate("status", "failure");
					response.getWriter().println(responseJSON.toString());
					return;
				}
				
				boolean success = false;
				if (usrJSON.has("first_name")) {
					update_user.setFirstName(usrJSON.getString("first_name"));
					success = true;
				}
				if (usrJSON.has("last_name")) {
					update_user.setLastName(usrJSON.getString("last_name"));
					success = true;
				}
				if (usrJSON.has("profile_picture")) {
					update_user.setProfilePicture(usrJSON.getString("profile_picture"));
					success = true;
				}
				if (usrJSON.has("email_address")) {
					update_user.setEmailAddress(usrJSON.getString("email_address"));
					success = true;
				}
				responseJSON.accumulate("status", success ? "success" : "failure");
			}
			catch (ClassNotFoundException e) {}
			response.getWriter().println(responseJSON.toString());
		}
		
	}

}
