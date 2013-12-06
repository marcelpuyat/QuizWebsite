<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ui.*" %>
<%@ page import="user.*" %>
<%
	if (!VerifyAccess.verify("Home.jsp",session, request, response))
    return;
%>

<%!
long getUserID(HttpSession session) {
	User user = (User) session.getAttribute("user");
	if (user != null) return user.getUserId();
	else return -1;
}
String getDisplayName(HttpSession session) {
	User user = (User) session.getAttribute("user");
	try {
		if (user != null) return user.getDisplayName();	
	} catch (ClassNotFoundException e) {}
	return "";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>FaceQuiz</title>
<link rel="stylesheet" type="text/css" href="/QuizWebsite/General.css">
<link rel="stylesheet" type="text/css" href="/QuizWebsite/Home.css">

</head>
<body onload="init_js(<%= getUserID(session) %>)">
	<div id="content-wrapper">
		<%= HTMLTemplater.getBlueBar(session)  %>
		<div class="page-width wide" id="center-wrapper">	
			<div class="left side-panel panel" id="left-content-panel">
				<ul>
					<li>
						<h2 class="faint">Welcome back <%= getDisplayName(session) %></h2>
					</li>
					<li>
						<h3>Have some ideas?</h3>
					</li>
					<li>
						<a class="button pointable" href="/QuizWebsite/NewQuiz.jsp">Create a new Quiz</a>	
					</li>
				</ul>
			</div>
			<div class="page-width thin center-block panel" id="inner-center-wrapper">
        <div class="home-content-box">
				  <div class="score-panel" id="achievements-earned-panel"></div>
				  <div class="score-panel" id="achievements-not-earned-panel"></div>
        </div>

        <div class="home-content-box">
				  <div class="score-panel" id="popular-quizzes-panel"></div>
				  <div class="score-panel" id="newest-quizzes-panel"></div>
        </div>
        <div class="home-content-box">
				  <div class="score-panel" id="created-quizzes-panel"></div>
				  <div class="score-panel" id="my-results-panel"></div>
				  <div class="score-panel" id="my-friends-panel"></div>
        </div>
			</div>
			<div class="right side-panel panel" id="right-content-panel">
				<h3 align='center'><u><b>Newsfeed</b></u></h3>
			</div>
				
		</div>
		
		<div class="left button green" id="history-link-div" onclick='show_history()'>
			View Full Results History
		</div>
	</div>
	<%= HTMLTemplater.getGeneralJS() %>
	<script src="/QuizWebsite/js/Home.js" type="text/javascript"></script>
</body>
</html>
