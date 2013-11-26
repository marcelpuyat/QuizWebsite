<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ui.HTMLTemplater" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
<link rel="stylesheet" type="text/css" href="/QuizWebsite/General.css">
<link rel="stylesheet" type="text/css" href="/QuizWebsite/Login.css">
</head>
<body onload="init_js()">
	<div id="content-wrapper">
		<%= HTMLTemplater.getBlueBar(session)  %>
		<div id="login-prompt-wrapper" class="center">
			<form action="/QuizWebsite/UserServlet?api=login" method="POST">
				<ul class="login-ul">
					<li><h2 class="lighter">Log in</h2></li>
					<li><div class="faint">username</div></li>
					<li><input type="text" name="username"></li>
					<li><div class="faint">password</div></li>
					<li><input type="password" name="password"></li>
					<li class="hide"><input class="hide" type="text" name="forward_to" id="forward-to"></li>
					<li><input type="submit"></li>
				</ul>
			</form>
			<h2 class="hide" id="now-sign-in-prompt">Now sign in!</h2>
			<div class="center-block" id="login-division"></div>
			<ul class="login-ul" id="create-account-ul">
				<li><h2 class="lighter">Create a new Account</h2></li>
				<li><div class="faint">username</div></li>
				<li><input type="text" name="new-username" id="new-username"><span class="error-message smaller hide" id="new-username-taken-error-message">username taken</span><span class="error-message smaller hide" id="new-username-malformed-error-message">username must only consist of a-z, A-Z, 0-9, -, or _</span></li>
				<li><div class="faint">password</div></li>
				<li><input type="password" name="new-password" id="new-password1"></li>
				<li><div class="faint smaller">password again</div></li>
				<li><input type="password" name="new-password-redundant" id="new-password2"><span class="error-message smaller hide" id="new-password-error-message">passwords don't match</span></li>
				<li><input type="button" onclick="submit_new_user()" value="Submit"><span class="error-message smaller hide" id="creation-error-message">account creation error</span></li>
			</ul>
		</div>
	</div>
	<script src="/QuizWebsite/js/AJAXUtil.js" type="text/javascript"></script>
	<script src="/QuizWebsite/js/Graph.js" type="text/javascript"></script>
	<script src="/QuizWebsite/js/Login.js" type="text/javascript"></script>
</body>
</html>