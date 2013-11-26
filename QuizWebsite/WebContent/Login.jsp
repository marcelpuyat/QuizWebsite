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
<body onload="init_js()">
	<div id="content-wrapper">
		<%= HTMLTemplater.getBlueBar(session)  %>
		<div id="login-prompt-wrapper">
			<form action="/QuizWebsite/UserServlet?api=login" method="POST">
				<div>username</div>
				<input type="text" name="username">
				<div>password</div>
				<input type="password" name="password">
				<input type="submit">
			</form>
			<form action="/QuizWebsite/UserServlet?api=create_user" method="POST">
				<div>username</div>
				<input type="text" name="new-username" id="new-username"><span class="error-message hide" id="new-username-taken-error-message">username taken</span><span class="error-message hide" id="new-username-malformed-error-message">username must only consist of a-z, A-Z, 0-9, -, or _</span>
				<div>password</div>
				<input type="password" name="new-password" id="new-password1">
				<input type="password" name="new-password-redundant" id="new-password2"><span class="error-message hide" id="new-password-error-message">passwords don't match</span>
				<input type="submit">
			</form>
		</div>
	</div>
	<script src="/QuizWebsite/js/AJAXUtil.js" type="text/javascript"></script>
	<script src="/QuizWebsite/js/Graph.js" type="text/javascript"></script>
	<script src="/QuizWebsite/js/Login.js" type="text/javascript"></script>
</body>
</html>