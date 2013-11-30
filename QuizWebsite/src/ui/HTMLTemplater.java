package ui;

import javax.servlet.http.HttpSession;

import user.User;

public class HTMLTemplater {
	public static String getGeneralJS() {
		return 
		"<script src=\"/QuizWebsite/js/AJAXUtil.js\" type=\"text/javascript\"></script>"+
		"<script src=\"/QuizWebsite/js/Graph.js\" type=\"text/javascript\"></script>";
	}
	
	public static String getBlueBar (HttpSession session) throws ClassNotFoundException {
		User curr_user = (User)session.getAttribute("user");
		String username = "Login";
		String user_url = "/QuizWebsite/Login.jsp";
		if (curr_user != null) {
			username = curr_user.getDisplayName();
			user_url = curr_user.getUserURL();
		}
		
		return 
		"<div id=\"blue-bar\">"+
			"<div id=\"page-head\" class=\"page-width wide\">"+
				"<a href=\"/QuizWebsite/\" id=\"masthead-logo\" class=\"sprite logo\"></a>"+
				"<div class=\"header-wrapper\">"+
					"<div class=\"navbar right\">"+
						"<ul class=\"nav\">"+
							"<li class=\"sprite requests\"></li>"+
							"<li class=\"sprite messages\"></li>"+
							"<li class=\"sprite notifications\"></li>"+
						"</ul>"+
						"<a class=\"header-text\" href=\"/QuizWebsite/\">Home</a>"+
						"<a class=\"header-text\" href=\""+user_url+"\">"+username+"</a>"+
						"<a class=\"sprite2 admin admin-button\" href=\"/QuizWebsite/Admin.jsp\"></a>"+
						"<div class=\"sprite settings admin-button\"></div>"+
					"</div>"+
					"<div class=\"search-bar\">"+
						"<input type=\"text\" id=\"graph-search-bar\" placeholder=\"Search for People, Quizzes, and Topics\" value=\"\">"+
						"<ul id=\"graph-search-results\">"+
						"</ul>"+	
						"<div class=\"sprite search-mag\" id=\"search-mag\"></div>"+
					"</div>"+
				"</div>"+
			"</div>"+
		"</div>";
	}
	
}
