<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ui.HTMLTemplater" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
<link rel="stylesheet" type="text/css" href="/QuizWebsite/General.css">
</head>
<body>
	<div id="content-wrapper">
		<%= HTMLTemplater.getBlueBar(session)  %>
		<form action="/QuizWebsite/UserServlet?api=login" method="POST">
			username:<input type="text" name="username">
			password:<input type="password" name="password">
			<input type="submit">
		</form>
	</div>
	<script src="/QuizWebsite/js/AJAXUtil.js" type="text/javascript"></script>
	<script src="/QuizWebsite/js/Graph.js" type="text/javascript"></script>
</body>
</html>