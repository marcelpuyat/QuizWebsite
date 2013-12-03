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
		<div id="QuizPage-wrapper" class="page-width wide center-block hide">
			<ul>
				<li class="center">
					<h1 id="quiz-name" class="center flowing"></h1><span class="faint">by <a id="quiz-creator" href="#"></a></span>
				</li>
				<li class="center">
					<h3 id="quiz-description" class="lighter faint"></h3>
				</li>
				<li class="center">
					<ul>
						<li id="delete-li" class="flowing hide">
							<a class="button red" href="javascript:delete_quiz(<%= getQuizID(request) %>)">Delete</a>
						</li>
						<li id="practice-li" class="flowing hide">
							<a class="button" href="/QuizWebsite/Quiz.jsp?quiz_id=<%= getQuizID(request) %>&practice=true">Practice</a>
						</li>
						<li id="play-li" class="flowing">
							<a class="button" href="/QuizWebsite/Quiz.jsp?quiz_id=<%= getQuizID(request) %>&practice=false">Play</a>
						</li>
						<li id="edit-li" class="flowing hide">
							<a class="button" href="/QuizWebsite/EditQuiz.jsp?quiz_id=<%= getQuizID(request) %>">Edit</a>
						</li>
					</ul>
				</li>
				<li>
					<ul id="score-holder">
						<li class="flowing score-container">
							<ul id="user-history"></ul>
						</li>
						<li class="flowing score-container">
							<ul id="best-alltime"></ul>
						</li>
						<li class="flowing score-container">
							<ul id="best-today"></ul>
						</li>
						<li class="flowing score-container">
							<ul id="recent-scores"></ul>
						</li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
	<%= HTMLTemplater.getGeneralJS() %>
	<script src="/QuizWebsite/js/QuizPage.js" type="text/javascript"></script>
</body>
</html>