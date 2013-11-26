<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ui.HTMLTemplater" %>
<%@ page import="user.*" %>
<%
	User u = (User)session.getAttribute("user");
	if (u == null) response.sendRedirect("/QuizWebsite/Login.jsp");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>FaceQuiz</title>
<link rel="stylesheet" type="text/css" href="/QuizWebsite/General.css">
</head>
<body>
	<div id="content-wrapper">
		<%= HTMLTemplater.getBlueBar(session)  %>
		<div class="page-width wide" id="center-wrapper" style="background-color:blue;">
			<div class="left side-panel panel" id="left-content-panel">
				<ul>
					<li>
						<a href="/QuizWebsite/NewQuiz.jsp">Create New Quiz</a>
					</li>
				</ul>
			</div>
			<div class="page-width thin center-block panel" style="height:300px;background-color:black;" id="inner-center-wrapper"></div>
			<div class="right side-panel panel" id="right-content-panel"></div>
		</div>
	</div>
	<script src="/QuizWebsite/js/AJAXUtil.js" type="text/javascript"></script>
	<script src="/QuizWebsite/js/Graph.js" type="text/javascript"></script>
</body>
</html>