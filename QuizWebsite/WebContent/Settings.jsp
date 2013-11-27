<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ui.*" %>
<%
	VerifyAccess.verify("Settings.jsp",session, request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Settings</title>
<link rel="stylesheet" type="text/css" href="/QuizWebsite/General.css">
<link rel="stylesheet" type="text/css" href="/QuizWebsite/Login.css">
</head>
<body onload="init_js()">
	<div id="content-wrapper">
		<%= HTMLTemplater.getBlueBar(session)  %>
		<ul class="login-ul center">
			<li><h2 class="lighter">Update Settings</h2></li>
			<li><div id="first-name-update-label" class="faint">first name</div></li>
			<li><input type="text" id="first-name-update"></li>
			<li><input type="button" value="update" id="first-name-button"></li>
			<li><div id="last-name-update-label" class="faint">last name</div></li>
			<li><input type="text" id="last-name-update"></li>
			<li><input type="button" value="update" id="last-name-button"></li>
		</ul>
	</div>
	<script src="/QuizWebsite/js/AJAXUtil.js" type="text/javascript"></script>
	<script src="/QuizWebsite/js/Graph.js" type="text/javascript"></script>
	<script src="/QuizWebsite/js/Settings.js" type="text/javascript"></script>
</body>
</html>