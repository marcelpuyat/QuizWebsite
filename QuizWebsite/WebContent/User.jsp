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
		if (user != null) return user.getUserId();
		return -1;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%= getUserName(request, application) %></title>
<link rel="stylesheet" type="text/css" href="/QuizWebsite/General.css">
<link rel="stylesheet" type="text/css" href="/QuizWebsite/User.css">
</head>
<body onload="init_js(<%= getCurrUserId(request) %>, <%=getUserIdOfPage(request, application)%>)">
	<div id="content-wrapper">
		<%= HTMLTemplater.getBlueBar(session)  %>
		<div class="page-width wide center-block hide" id="inner-content-container">
			<div class="masthead">
				<ul id="masthead-friends-ul"></ul>
				<div id="user-photo-pad"><div id="user-photo"></div></div>
			</div>
			<div id="buttons" class="page-width thin center-block center">
				<div class="hide button" id="message-button" onclick="open_message_pane(<%= getUserIdOfPage(request, application) %>)">Message <%= getUserName(request, application) %></div>
				<div class="hide button" id="accept-request-button" onclick="acceptRequest()">Accept Friend Request</div>
				<div class="hide button" id="reject-request-button" onclick="rejectRequest()">Reject Friend Request</div>
				<div class="hide button red" id="block-user-button" onclick="blockUser()">Block User</div>
				<div class="hide button" id="unblock-user-button" onclick="unblockUser()">Unblock User</div>
				<div class="hide button red" id="delete-friend-button" onclick="removeFriend()">Delete Friend</div>
				<div class="hide button" id="request-button" onclick="sendRequest()">Send Friend Request</div>
			</div>
			<div class="" id="user-info"></div><br>
			<div class="" id="relation-status"></div><br>
			<br>
			<div class="" id ="relation-controls">
			</div><br>
			<div class="" id="recent-results"></div><br>
			<div class="" id ="created-quizzes"></div><br>
			<div class="" id ="achievements"></div>
		</div>
	</div>
	<%= HTMLTemplater.getGeneralJS() %>
	<script src="/QuizWebsite/js/User.js" type="text/javascript"></script>

</body>
</html>