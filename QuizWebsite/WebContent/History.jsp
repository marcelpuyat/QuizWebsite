<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ui.*" %>
<%@ page import="user.*" %>
<%
	if (!VerifyAccess.verify("Home.jsp",session, request, response))
    return;
%>

<%!
long getUserID(HttpSession session) {
	User user = (User) session.getAttribute("user");
	if (user != null) return user.getUserId();
	else return -1;
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>History</title>
<link rel="stylesheet" type="text/css" href="/QuizWebsite/General.css">
<link rel="stylesheet" type="text/css" href="/QuizWebsite/History.css">

</head>
<body onload="init_js(<%= getUserID(session) %>)">
	<div id="content-wrapper">
		<%= HTMLTemplater.getBlueBar(session)  %>
		
		<div id='history-panel'>
		
			<table class='center' id='history-table' border='1'>
			<tr>
			<th id="quiz-header" style="cursor: pointer;">Quiz</th>
			<th id="score-header" style="cursor: pointer;">Score</th>
			<th id="time-header" style="cursor: pointer;">Time</th>
			<th id="rank-header" style="cursor: pointer;">Rank</th>
			<th id="date-header" style="cursor: pointer;">Date</th>
			</tr>
			
			</table>
		
		</div>
		
		
	</div>
	<%= HTMLTemplater.getGeneralJS() %>
	<script src="/QuizWebsite/js/History.js" type="text/javascript"></script>
</body>
</html>