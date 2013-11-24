package graph;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.*;

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
		String query = request.getParameter("query");
		String lim_s = request.getParameter("limit");
		if (lim_s == null || lim_s.equals("")) lim_s = "20";
		int limit    = Integer.parseInt(lim_s);
		
		response.setContentType("application/json");
		
		ServletContext context = getServletContext(); 
		Connection databaseConnection = (Connection)context.getAttribute("database_connection");
		
		try {
			Statement stmt = databaseConnection.createStatement();
			String getNameQuery = "SELECT name,id FROM Quizzes WHERE UPPER(name) LIKE UPPER(\"" + query + "%\") LIMIT 0, "+limit;
			ResultSet rs = stmt.executeQuery(getNameQuery);
			JSONObject results = new JSONObject();
			while(rs.next()) {
				JSONObject entry = new JSONObject();
				entry.accumulate("name", rs.getString("name"));
				entry.accumulate("id", rs.getInt("id"));
				results.accumulate("quiz_high", entry);
			}
			response.getWriter().println(results.toString());
			return;
		}
		catch (Exception e) { e.printStackTrace(); }
		response.getWriter().println("nope");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
