<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ui.*" %>
<%@ page import="user.*" %>
<%
	VerifyAccess.verify("Quiz.jsp",session, request, response);
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
<link rel="stylesheet" type="text/css" href="/QuizWebsite/Quiz.css">
<link rel="stylesheet" type="text/css" href="/QuizWebsite/General.css">
</head>
<body onload="init_js('<%= getQuizID(request) %>')">
	<div id="content-wrapper">
		<%= HTMLTemplater.getBlueBar(session)  %>
		<div id="app-wrapper">
			<div id="quiz-content-wrapper">
				<div id="quiz-cards-wrapper">
				</div>
			</div>
		</div>
	</div>
	<script src="/QuizWebsite/js/AJAXUtil.js" type="text/javascript"></script>
	<script src="/QuizWebsite/js/Graph.js" type="text/javascript"></script>
	<script src="/QuizWebsite/js/QuestionTypes.js" type="text/javascript"></script>
	<script src="/QuizWebsite/js/QuizHandler.js" type="text/javascript"></script>
	<script src="/QuizWebsite/js/Quiz.js" type="text/javascript"></script>
</body>
</html>