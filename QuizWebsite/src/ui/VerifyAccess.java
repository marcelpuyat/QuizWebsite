package ui;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.*;

import quiz.Quiz;
import customObjects.SelfRefreshingConnection;
import user.*;

public class VerifyAccess {

	public static boolean verify(String servletName, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		try {
			User u = (User)session.getAttribute("user");
			if (servletName.equals("Login.jsp")) return true;
			if (u == null) {
				response.sendRedirect("/QuizWebsite/Login.jsp");
				return false;
			}
			if (servletName.equals("Settings.jsp")) return true;
			if (!u.hasCompletedProfile()) {
				response.sendRedirect("/QuizWebsite/Settings.jsp");
				return false;
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean verifyAdmin(HttpSession session, HttpServletRequest request, HttpServletResponse response, ServletContext application) {
		try {
			User u = (User)session.getAttribute("user");
			if (u == null) {
				response.sendRedirect("/QuizWebsite/Login.jsp");
			}
			if(u.isAdmin()){
				return true;
			}
			response.sendRedirect("/QuizWebsite/Home.jsp");
			return true;
		}
		catch (IOException e) {e.printStackTrace();}
		return false;
	}
	
	public static boolean verifyQuizOwner(HttpSession session, HttpServletRequest request, HttpServletResponse response, ServletContext application) {
		try {
			String quiz_id = request.getParameter("quiz_id");
			if (quiz_id == null || quiz_id.equals("")) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
			if (quiz_id.equals("new")) return true;
			int id = Integer.parseInt(quiz_id);
			SelfRefreshingConnection databaseConnection = (SelfRefreshingConnection)application.getAttribute("database_connection");
			Quiz quiz = new Quiz(id, databaseConnection);
			
			User u = (User)session.getAttribute("user");
			if (u == null) {
				response.sendRedirect("/QuizWebsite/Login.jsp");
			}
			if(quiz.getCreator().equals(u.getUserName())){
				return true;
			}
			response.sendRedirect("/QuizWebsite/Home.jsp");
			return false;
		}
		catch (ClassNotFoundException e) {e.printStackTrace();}
		catch (IOException e) {e.printStackTrace();}
		return false;
	}
}