package ui;

import java.io.IOException;

import javax.servlet.http.*;
import user.*;

public class VerifyAccess {

	public static void verify(String servletName, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		try {
			User u = (User)session.getAttribute("user");
			if (servletName.equals("Login.jsp")) return;
			if (u == null) {
				response.sendRedirect("/QuizWebsite/Login.jsp");
				return;
			}
			if (servletName.equals("Settings.jsp")) return;
			if (!u.hasCompletedProfile()) {
				response.sendRedirect("/QuizWebsite/Settings.jsp");
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}