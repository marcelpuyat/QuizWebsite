<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%!
	String getTitle(HttpServletRequest req) {
		return req.getParameter("name") == null ? "Quiz" : req.getParameter("name");
	}
	String getQuizID(HttpServletRequest req) {
		return req.getParameter("quiz_id");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%= getTitle(request) %></title>
<link rel="stylesheet" type="text/css" href="/QuizWebsite/Quiz.css">
<link rel="stylesheet" type="text/css" href="/QuizWebsite/General.css">
</head>
<body onload="init_js('<%= getQuizID(request) %>')">
	<div id="content-wrapper">
		<div id="app-selection-wrapper">
			<ul id="app-selection-ul">
				<li>
					<a href="#" class="">Home</a>
				</li>
				<li>
					<a href="#" class="active">Quiz</a>
				</li>
				<li>
					<a href="#" class="">Friends</a>
				</li>
			</ul>
		</div>
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