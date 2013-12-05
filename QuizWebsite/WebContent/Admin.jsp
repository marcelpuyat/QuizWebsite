<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ui.*" %>
<%
	if (!VerifyAccess.verify("Admin.jsp", session, request, response))
    return;
	VerifyAccess.verifyAdmin(session, request, response, application);
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
<body>
	<div id="content-wrapper">
		<%= HTMLTemplater.getBlueBar(session)  %>
		<div class="page-width wide center-block"  id="questions-wrapper">
			<span>Admin</span>
		</div>
	</div>
	<%= HTMLTemplater.getGeneralJS() %>
</body>
</html>
