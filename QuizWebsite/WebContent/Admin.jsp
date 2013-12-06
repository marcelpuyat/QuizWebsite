<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ui.*" %>
<%@ page import="user.*" %>
<%
	if (!VerifyAccess.verify("Admin.jsp", session, request, response))
    return;
	VerifyAccess.verifyAdmin(session, request, response, application);
%>
<%!
	boolean isAdmin(HttpSession session) {
		User user = (User)session.getAttribute("user");
		if (user != null) {
			return user.isAdmin();
		}
		return false;
	}

	long getUserID(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user != null) return user.getUserId();
		else return -1;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit Quiz</title>
<link rel="stylesheet" type="text/css" href="/QuizWebsite/General.css">
<link rel="stylesheet" type="text/css" href="/QuizWebsite/Login.css">
<link rel="stylesheet" type="text/css" href="/QuizWebsite/EditQuiz.css">
<link rel="stylesheet" type="text/css" href="/QuizWebsite/Admin.css">

</head>
<body onload="init_js(<%= getUserID(session) %>, <%= isAdmin(session) %>)">
	<div id="content-wrapper">
		<%= HTMLTemplater.getBlueBar(session)  %>
		<div class="page-width wide center-block"  id="questions-wrapper">
			<div id='stats-panel' class='info-panel hide'>
				<h3 class='list-heading'>Site Statistics</h3>
				<ul id='stats-list'>
				
				</ul>
			</div>
			<div id='announcements-panel' class='info-panel'>
				<h3 class='list-heading'>Announcements</h3>
				<ul id='announcements-list'>
				
				</ul>
			</div>
			<div id='announcement-post-panel' class='info-panel hide'>
				<h3 class='list-heading'>Post New Announcement</h3>
				<ul class='post-announcement'>
				
					<li><label for='subject-field'>Subject: </label><input type='text' id='subject-field'></li>
					
					<li><span id='body-area'><label id="body-label" for='body-field'>Body: </label><textarea id='body-field'></textarea></span></li>
					
				</ul>
				<div class="button" onclick="post_announcement()" id='post-announcement-button'>Post</div>
				
			</div>
		</div>
	</div>
	<%= HTMLTemplater.getGeneralJS() %>
	<script src="/QuizWebsite/js/Admin.js" type="text/javascript"></script>
</body>
</html>
