<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="user.*" %>
<%@ page import="ui.*" %>
<%
	if (!VerifyAccess.verify("QuizPage.jsp",session, request, response))
    	return;
%>
<%!
	String getQuizID(HttpServletRequest req) {
		return req.getParameter("quiz_id");
	}
	long getUserID(HttpSession session) {
		User u = (User)session.getAttribute("user");
		if (u != null) {
			return u.getUserId();
		}
		return -1;
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
<body onload="init_js('<%= getQuizID(request) %>',<%= getUserID(session) %>)">
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
				<li id="ratings">
					<ul>
						<li id="num-ratings" class="center faint"></li>
						<li id="avg-rating" class="rating center"> <!-- stars -->
							<span>
								<span>	
									<span>&#9734;</span> <!-- 5 -->
									<span>&#9734;</span> <!-- 4 -->
									<span>&#9734;</span> <!-- 3 -->
									<span>&#9734;</span> <!-- 2 -->
									<span>&#9734;</span> <!-- 1 -->
								</span>
								<span class="ratings-cover" id="avg-rating-cover"></span>
							</span>
						</li>
						<li id="usr-rating" class="rating center"> <!-- stars -->
							<span>
								<span id="usr-rating-span">	
									<span rating="5" class="pointable rating-star">&#9734;</span> <!-- 5 -->
									<span rating="4" class="pointable rating-star">&#9734;</span> <!-- 4 -->
									<span rating="3" class="pointable rating-star">&#9734;</span> <!-- 3 -->
									<span rating="2" class="pointable rating-star">&#9734;</span> <!-- 2 -->
									<span rating="1" class="pointable rating-star">&#9734;</span> <!-- 1 -->
								</span>
								<span class="ratings-cover" id="usr-rating-cover"></span>
							</span>	
						</li>
					</ul>
				</li>
				<li class="center">
					<ul>
						<li id="delete-li" class="flowing hide">
							<a class="button red" href="javascript:delete_quiz(<%= getQuizID(request) %>)">Delete</a>
						</li>
						<li id="clear-li" class="flowing hide">
							<a class="button red" href="javascript:clear_quiz(<%= getQuizID(request) %>)">Clear History</a>
						</li>
						<li id="practice-li" class="flowing hide">
							<a class="button green" href="/QuizWebsite/Quiz.jsp?quiz_id=<%= getQuizID(request) %>&practice=true">Practice</a>
						</li>
						<li id="play-li" class="flowing">
							<a class="button green" href="/QuizWebsite/Quiz.jsp?quiz_id=<%= getQuizID(request) %>&practice=false">Play</a>
						</li>
						<li id="edit-li" class="flowing hide">
							<a class="button" href="/QuizWebsite/EditQuiz.jsp?quiz_id=<%= getQuizID(request) %>">Edit</a>
						</li>
					</ul>
				</li>
				<li>
					<ul id="score-holder">
						<li class="flowing score-container">
							<ul id="user-history">
								<li>
									<h4 class="best-of-header">User History</h4>
								</li>
							</ul>
						</li>
						<li class="flowing score-container">
							<ul id="best-alltime">
								<li>
									<h4 class="best-of-header">Best All-Time</h4>
								</li>
							</ul>
						</li>
						<li class="flowing score-container">
							<ul id="best-today">
								<li>
									<h4 class="best-of-header">Best Today</h4>
								</li>
							</ul>
						</li>
						<li class="flowing score-container">
							<ul id="recent-scores">
								<li>
									<h4 class="best-of-header">Recent Scores</h4>
								</li>
							</ul>
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
