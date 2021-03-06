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
	if (!VerifyAccess.verify("Settings.jsp",session, request, response))
    	return;
%>
<%!
	String getDisplayName(HttpServletRequest req, ServletContext context) {
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
	String getUserName(HttpServletRequest req, ServletContext context) {
		try {
			SelfRefreshingConnection con = (SelfRefreshingConnection)context.getAttribute("database_connection");
			String user_id_str = req.getParameter("user_id");
			String username    = req.getParameter("username");
			if (user_id_str != null && !user_id_str.equals("")) {
				User u = new User(Integer.parseInt(user_id_str), con);
				return u.getUserName();
			} else if (username != null && !username.equals("")) {
				User u = new User(username, con);
				return u.getUserName();
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
	boolean showPromote(ServletContext context, HttpServletRequest req) {
		SelfRefreshingConnection con = (SelfRefreshingConnection)context.getAttribute("database_connection");
		User viewer = new User(getCurrUserId(req), con);
		if (viewer.isAdmin() == false) return false;
		User viewee = new User(getUserIdOfPage(req, context), con);
		
		if (viewee.isAdmin() == true) return false;
		return true;
	}
	boolean showDelete(HttpSession session) {
		User user = (User)session.getAttribute("user");
		return user.isAdmin();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%= getDisplayName(request, application) %></title>
<link rel="stylesheet" type="text/css" href="/QuizWebsite/General.css">
<link rel="stylesheet" type="text/css" href="/QuizWebsite/User.css">
</head>
<body onload="init_js(<%= getCurrUserId(request) %>, <%=getUserIdOfPage(request, application)%>, <%= showPromote(application, request) %>, <%= showDelete(session) %>)">
	<div id="content-wrapper">
		<%= HTMLTemplater.getBlueBar(session)  %>
		<div class="page-width wide center-block hide" id="inner-content-container">
			<div class="masthead">
				<div id="masthead-friends-div"></div>
				<div id="user-photo-pad"><div id="user-photo"></div></div>
			</div>
			<span class="right faint" id="relation-status"></span>
			<div id="buttons" class="page-width thin center-block center">
				<div class="hide button" id="message-button" onclick="open_message_pane({id:<%= getUserIdOfPage(request, application) %>,display_name:'<%= getDisplayName(request, application) %>'})">Message <%= getDisplayName(request, application) %></div>
				<div class="hide button" id="accept-request-button" onclick="acceptRequest()">Accept Friend Request</div>
				<div class="hide button" id="reject-request-button" onclick="rejectRequest()">Reject Friend Request</div>
				<div class="hide button red" id="block-user-button" onclick="blockUser()">Block User</div>
				<div class="hide button" id="unblock-user-button" onclick="unblockUser()">Unblock User</div>
				<div class="hide button red" id="delete-friend-button" onclick="removeFriend()">Delete Friend</div>
				<div class="hide button" id="request-button" onclick="sendRequest()">Send Friend Request</div>
				<div class="hide button red" id="remove-user-button" onclick="removeUser()">Delete User</div>
				<div class="hide button" id="promote-user-button" onclick="promoteUser()">Promote User</div>
				
			</div>
			<div id="achievements" class="page-width thin center-block center">
				<h3 class="div-header">Achievements</h3>
			</div>
			<div class="bottom-center-block">
				<div id="user-info"></div>
				<div id ="relation-controls"></div>
				<div class="center-box" id="recent-results">
					<h3 class="div-header">Recent Results</h3>
				</div>
				<div class="center-box" id ="created-quizzes">
					<h3 class="div-header">Created Quizzes</h3>
				</div>
			</div>
			<ul id="user-summary">
				<li><div><h3 id="name"><%= getDisplayName(request, application) %></h3></div></li>
				<li><div class="faint"><%= getUserName(request, application) %></div></li>
			</ul>
		</div>
	</div>
	<%= HTMLTemplater.getGeneralJS() %>
	<script src="/QuizWebsite/js/User.js" type="text/javascript"></script>

</body>
</html>
