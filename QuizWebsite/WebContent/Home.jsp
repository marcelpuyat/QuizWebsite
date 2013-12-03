<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ui.*" %>
<%@ page import="user.*" %>
<%
	VerifyAccess.verify("Home.jsp",session, request, response);
%>

<%!
long getUserID(HttpSession session) {
	User user = (User) session.getAttribute("user");
	if (user != null) return user.getUserId();
	else return -1;
}
String getUsername(HttpSession session) {
	User user = (User) session.getAttribute("user");
	try {
		if (user != null) return user.getUserName();	
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
						<a href="/QuizWebsite/NewQuiz.jsp"><button>Create New Quiz</button></a>

			</div>
			<div class="page-width thin center-block panel" id="inner-center-wrapper"></div>
			<div class="right side-panel panel" id="right-content-panel"></div>
			<div class="score-panel" id="achievements-earned-panel"></div>
			<div class="score-panel" id="achievements-not-earned-panel"></div>
			<div class="score-panel" id="popular-quizzes-panel"></div>
			<div class="score-panel" id="created-quizzes-panel"></div>
			<div class="score-panel" id="my-results-panel"></div>
				
		</div>
	</div>
	<%= HTMLTemplater.getGeneralJS() %>
	<script src="/QuizWebsite/js/Home.js" type="text/javascript"></script>
</body>
</html>