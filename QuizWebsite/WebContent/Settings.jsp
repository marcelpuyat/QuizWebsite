<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ui.*" %>
<%@ page import="user.*" %>
<%
	if (!VerifyAccess.verify("Settings.jsp",session, request, response))
		return;
%>
<%!
User getUser(HttpSession session) {
	User user = (User) session.getAttribute("user");
	return user;
}
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

        <li><div id="first-name-update-label" class="input faint">First Name</div></li>
        <li><input type="text" id="first-name-update" value='<%=getUser(session).getFirstName()%>'></li>

        <li><div id="last-name-update-label" class="input faint">Last Name</div></li>
        <li><input type="text" id="last-name-update" value='<%=getUser(session).getLastName()%>'></li>

        <li><div id="profile-picture-update-label" class="input faint">Profile Picture URL</div></li>
        <li><input type="text" id="profile-picture-update" value='<%=getUser(session).getProfilePicture()%>'></li>

        <li><div id="email-address-update-label" class="input faint">Email Address</div></li>
        <li><input type="text" id="email-address-update" value='<%=getUser(session).getEmailAddress()%>'></li>
        <li><div class="button" class="update-button" id="update-button">Update</div></li>
      </ul>
	</div>
	<%= HTMLTemplater.getGeneralJS() %>
	<script src="/QuizWebsite/js/Settings.js" type="text/javascript"></script>
</body>
</html>
