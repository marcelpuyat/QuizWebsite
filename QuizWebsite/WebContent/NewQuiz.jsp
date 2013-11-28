<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ui.*" %>
<%@ page import="user.*" %>
<%
	VerifyAccess.verify("Settings.jsp",session, request, response);
%>
<%!
	String getUsername(HttpSession session) {
		try {
			User u = (User)session.getAttribute("user");
			return u.getUserName();	
		}
		catch(ClassNotFoundException e){}
		return "";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create New Quiz</title>
<link rel="stylesheet" type="text/css" href="/QuizWebsite/General.css">
<link rel="stylesheet" type="text/css" href="/QuizWebsite/Login.css">
</head>
<body>
	<div id="content-wrapper">
		<%= HTMLTemplater.getBlueBar(session)  %>
		<ul class="center login-ul">
			<li><h2 class="lighter">New Quiz</h2></li>
			<li><div class="faint">quiz name</div></li>
			<li><input type="text" name="quiz-name" id="quiz-name-input"></li>
			<li><input type="button" value="create" onclick="post_new_quiz('<%= getUsername(session) %>')"></li>
		</ul>
	</div>
	<script src="/QuizWebsite/js/AJAXUtil.js" type="text/javascript"></script>
	<script src="/QuizWebsite/js/Graph.js" type="text/javascript"></script>
	<script src="/QuizWebsite/js/NewQuiz.js" type="text/javascript"></script>
</body>
</html>