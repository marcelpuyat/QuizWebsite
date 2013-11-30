<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="user.*" %>
<%@ page import="ui.*" %>
<%
	VerifyAccess.verify("Settings.jsp",session, request, response);
%>
<%!
	String getQuizID(HttpServletRequest req) {
		return req.getParameter("quiz_id");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Quiz</title>
<link rel="stylesheet" type="text/css" href="/QuizWebsite/General.css">
<link rel="stylesheet" type="text/css" href="/QuizWebsite/QuizPage.css">
</head>
<body onload="init_js('<%= getQuizID(request) %>')">
	<div id="content-wrapper">
		<%= HTMLTemplater.getBlueBar(session)  %>
		<div id="QuizPage-wrapper" class="page-width wide center-block">
			<ul>
				<li>
					<h1 id="quiz-name"></h1><span class="faint">by<span id="quiz-creator"></span></span>
				</li>
				<li>
					<h3 id="quiz-description" class="lighter faint"></h3>
				</li>
			</ul>
		</div>
		<a href="/QuizWebsite/Quiz.jsp?quiz_id=<%= getQuizID(request) %>&practice=false">Take the quiz</a>
		<a href="/QuizWebsite/Quiz.jsp?quiz_id=<%= getQuizID(request) %>&practice=true">Practice</a>
		<a href="/QuizWebsite/EditQuiz.jsp?quiz_id=<%= getQuizID(request) %>">Edit</a>
	</div>
	<script src="/QuizWebsite/js/AJAXUtil.js" type="text/javascript"></script>
	<script src="/QuizWebsite/js/Graph.js" type="text/javascript"></script>
	<script src="/QuizWebsite/js/QuizPage.js" type="text/javascript"></script>
</body>
</html>