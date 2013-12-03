<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ui.HTMLTemplater" %>
<%@ page import="user.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="javax.servlet.ServletContext" %>
<%@ page import="user.*" %>
<%@ page import="customObjects.*" %>
<%@ page import="ui.*" %>
<%
	VerifyAccess.verify("Settings.jsp",session, request, response);
%>
<%!
	String getUserName(HttpServletRequest req, ServletContext context) {
		try {
			SelfRefreshingConnection con = (SelfRefreshingConnection)context.getAttribute("database_connection");
			String user_id_str = req.getParameter("user_id");
			String username    = req.getParameter("username");
			if (user_id_str != null && !user_id_str.equals("")) {
				User u = new User(Integer.parseInt(user_id_str), con);
				return u.getDisplayName();
			} else if (username != null && !username.equals("")) {
				User u = new User(username, con);
				return u.getDisplayName();
			}
		}
		catch (ClassNotFoundException e) {}
		return "FaceQuiz";
	}
	long getUserIdOfPage(HttpServletRequest req, ServletContext context) {
		SelfRefreshingConnection con = (SelfRefreshingConnection)context.getAttribute("database_connection");
		String user_id_str = req.getParameter("user_id");
		String username    = req.getParameter("username");
		if (user_id_str != null) return Integer.parseInt(user_id_str);
		else if (username!= null) return (new User(username, con)).getUserId();
		return -1;
	}
	long getCurrUserId(HttpServletRequest req) {
		User user = (User)req.getSession().getAttribute("user");
		return user.getUserId();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%= getUserName(request, application) %></title>
<link rel="stylesheet" type="text/css" href="/QuizWebsite/General.css">
</head>
<body onload="init_js(<%= getCurrUserId(request) %>, <%=getUserIdOfPage(request, application)%>)">
	<div id="content-wrapper">
		<%= HTMLTemplater.getBlueBar(session)  %>
		<div class="" id="user-info"></div>
		<div class="" id="relation-status"></div>
		<br>
		<div class="" id ="relation-controls">
			<div class="pointable messag-user" id="message-button" onclick="open_message_pane(<%= getUserIdOfPage(request, application) %>)">Message <%= getUserName(request, application) %></div>
			<button class="button" id="accept-request-button" onclick="acceptRequest()">Accept Friend Request</button>
			<button class="button" id="reject-request-button" onclick="rejectRequest()">Reject Friend Request</button>
			<button class="button" id="block-user-button" onclick="blockUser()">Block User</button>
			<button class="button" id="unblock-user-button" onclick="unblockUser()">Unblock User</button>
			<button class="button" id="delete-friend-button" onclick="removeFriend()">Delete Friend</button>
			<button class="button" id="request-button" onclick="sendRequest()">Send Friend Request</button>
		</div>
		<div class="" id="recent-results"></div>
		<div class="" id ="created-quizzes"></div>
		<div class="" id ="achievements"></div>
	</div>
	<%= HTMLTemplater.getGeneralJS() %>
	<script src="/QuizWebsite/js/User.js" type="text/javascript"></script>

</body>
</html>