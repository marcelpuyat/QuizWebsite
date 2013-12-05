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
			<span>Admin</span>
			<div id='stats-panel'>
				<h3 class='list-heading'>Site Statistics</h3>
				<ul id='stats-list'>
				
				</ul>
			</div>
		</div>
	</div>
	<%= HTMLTemplater.getGeneralJS() %>
	<script src="/QuizWebsite/js/Admin.js" type="text/javascript"></script>
</body>
</html>
