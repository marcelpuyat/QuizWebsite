package graph;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import user.User;

import customObjects.SelfRefreshingConnection;

/**
 * Servlet implementation class GraphServlet
 */
@WebServlet("/GraphServlet")
public class GraphServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GraphServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User)request.getSession().getAttribute("user");
		if (u == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED); // No graph results if not signed in
			return;
		}
		
		
		String query = request.getParameter("query");
		String lim_s = request.getParameter("limit");
		if (lim_s == null || lim_s.equals("")) lim_s = "10";
		int limit    = Integer.parseInt(lim_s);
		
		response.setContentType("application/json");
		
		ServletContext context = getServletContext(); 
		SelfRefreshingConnection databaseConnection = (SelfRefreshingConnection)context.getAttribute("database_connection");
		JSONObject responseJSON;
		try {
			if (query.contains("#")) {
				responseJSON = GraphSearch.tag_search(databaseConnection, query, limit);
			} else {
				responseJSON = GraphSearch.simple_search(databaseConnection, query, limit);
			}
			if (responseJSON == null) {
				response.sendError(HttpServletResponse.SC_GATEWAY_TIMEOUT);
				return;
			}
			response.getWriter().println(responseJSON.toString());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
