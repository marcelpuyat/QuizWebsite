package quiz;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Home
 */
@WebServlet("")
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Home() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("Quiz.jsp?quiz_id=000041"); // 39 is Mult Choice
														  // 41 is Single Answer 
														  // 43 is Multiple Answer
														  // 45 is Matching Question
														  // 47 is Fill in Blank questions
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("Quiz.jsp?quiz_id=000041"); // 39 is Mult Choice
														  // 41 is Single Answer 
														  // 43 is Multiple Answer
														  // 45 is Matching Question
														  // 47 is Fill in Blank questions
	}

}
