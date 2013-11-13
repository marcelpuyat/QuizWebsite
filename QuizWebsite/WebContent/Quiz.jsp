<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ui.HTMLTemplater" %>
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
		<%= HTMLTemplater.getSidebarHTML("Quiz")  %>
		<div id="app-wrapper">
			<div id="quiz-content-wrapper">
				<div id="quiz-content">
					<div id="quiz-prompt-content">
						<div id="start-test-wrapper" onclick="start()">Start!</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="/QuizWebsite/AJAXUtil.js" type="text/javascript"></script>
	<script src="/QuizWebsite/QuestionTypes.js" type="text/javascript"></script>
	<script src="/QuizWebsite/Quiz.js" type="text/javascript"></script>
</body>
</html>