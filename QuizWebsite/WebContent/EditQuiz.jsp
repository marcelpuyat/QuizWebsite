<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ui.*" %>
<%
	if (!VerifyAccess.verify("Settings.jsp", session, request, response))
    return;
	VerifyAccess.verifyQuizOwner(session, request, response, application);
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
<title>Edit Quiz</title>
<link rel="stylesheet" type="text/css" href="/QuizWebsite/General.css">
<link rel="stylesheet" type="text/css" href="/QuizWebsite/Login.css">
<link rel="stylesheet" type="text/css" href="/QuizWebsite/EditQuiz.css">
</head>
<body onload="init_js('<%= getQuizID(request) %>')">
	<div id="content-wrapper">
		<%= HTMLTemplater.getBlueBar(session)  %>
		<div class="page-width wide center-block"  id="questions-wrapper">
			<ul id="questions-ul">
			</ul>
		</div>
	</div>
	<%= HTMLTemplater.getGeneralJS() %>
	<script src="/QuizWebsite/js/EditQuiz.js" type="text/javascript"></script>
	<script src="/QuizWebsite/js/EditQuizTypes.js" type="text/javascript"></script>
</body>
</html>
