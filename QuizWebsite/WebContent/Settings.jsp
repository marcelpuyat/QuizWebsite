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
    <form id="settings-update-form" onsubmit="update_all(); return false;">
      <ul class="login-ul center">
        <li><h2 class="lighter">Update Settings</h2></li>

        <li><div id="first-name-update-label" class="faint">first name</div></li>
        <li><input type="text" id="first-name-update" value='<%=getUser(session).getFirstName()%>'></li>

        <li><div id="last-name-update-label" class="faint">last name</div></li>
        <li><input type="text" id="last-name-update" value='<%=getUser(session).getLastName()%>'></li>

        <li><div id="profile-picture-update-label" class="faint">profile picture</div></li>
        <li><input type="text" id="profile-picture-update" value='<%=getUser(session).getProfilePicture()%>'></li>

        <li><div id="email-address-update-label" class="faint">email address</div></li>
        <li><input type="text" id="email-address-update" value='<%=getUser(session).getEmailAddress()%>'></li>
        <li><input type="submit" value="Update"></li>
      </ul>
    </form>
	</div>
	<%= HTMLTemplater.getGeneralJS() %>
	<script src="/QuizWebsite/js/Settings.js" type="text/javascript"></script>
</body>
</html>
