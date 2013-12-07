<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ui.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Attribution</title>
<link rel="stylesheet" type="text/css" href="/QuizWebsite/General.css">
</head>
<body onload="init_js()">
	<div id="content-wrapper">
		<%= HTMLTemplater.getBlueBar(session)  %>
		<div class="page-width thin center-block panel">
			<ul>
				<li>
					<h1>Styling</h1>
				</li>
				<li>
					<h4>Star animation</h4>
					<p>When a user is setting their rating and the stars turn black under the mouse, that css is copied from online. Attribution in source. The rest of the rating system is homegrown (including avg rating etc.)</p>
				</li>
				<li>
					<h4>Facebook Theme</h4>
					<p>two sprite sheets and tips on styling copied directly from Facebook.com</p>
				</li>
				<li>
					<h4>Achievements Icons</h4>
					<p>Sprite sheet found via google search, property of Nike</p>
				</li>
				<li>
					<h4>Various Profile Pictures</h4>
					<p>Not our content, we just store urls</p>
				</li>
				<li>
					<h4>Various Profile Pictures</h4>
					<p>Not our content, we just store urls</p>
				</li>
				<li>
					<h1>Javascript</h1>
					<p class="faint">no external libraries or frameworks were used. All 100% homegrown javascript</p>
				</li>
				<li>
					<h1>Java</h1>
				</li>
				<li>
					<h4>JSON Library</h4>
					<p>We used <a href="http://json.org/java/">this library</a> to handle json</p>
				</li>
			</ul>
		</div>
	</div>
	<%= HTMLTemplater.getGeneralJS() %>
</body>
</html>
