package ui;

import javax.servlet.http.HttpSession;

import user.User;

public class HTMLTemplater {
	public static String getGeneralJS() {
		return 
		"<script src=\"/QuizWebsite/js/AJAXUtil.js\" type=\"text/javascript\"></script>"+
		"<script src=\"/QuizWebsite/js/Graph.js\" type=\"text/javascript\"></script>"+
		"<script src=\"/QuizWebsite/js/BlueBar.js\" type=\"text/javascript\"></script>";
	}
	
	public static String getBlueBar (HttpSession session) throws ClassNotFoundException {
		User curr_user = (User)session.getAttribute("user");
		String username = "Login";
		String user_url = "/QuizWebsite/Login.jsp";
		int user_id = -1;
		if (curr_user != null) {
			username = curr_user.getDisplayName();
			user_url = curr_user.getUserURL();
			user_id  = curr_user.getUserId();
		}
		
		return 
		"<div id=\"blue-bar\">"+
			"<div id=\"page-head\" class=\"page-width wide\">"+
				"<a href=\"/QuizWebsite/\" id=\"masthead-logo\" class=\"sprite logo\"></a>"+
				"<div class=\"header-wrapper\">"+
					"<div class=\"navbar right\">"+
						"<div id=\"user_id_stash\" user_id=\""+user_id+"\" style=\"display:none;\"></div>"+
						"<ul class=\"nav\">"+
							"<li class=\"sprite requests blue-dropdown-radio\" id=\"requests-button\"></li>"+
							"<li class=\"sprite messages blue-dropdown-radio\" id=\"messages-button\"></li>"+
							"<li class=\"sprite notifications blue-dropdown-radio\" id=\"notifications-button\"></li>"+
						"</ul>"+
						"<a class=\"header-text\" href=\"/QuizWebsite/\">Home</a>"+
						"<a class=\"header-text\" href=\""+user_url+"\">"+username+"</a>"+
						"<a class=\"sprite2 admin admin-button\" href=\"/QuizWebsite/Admin.jsp\"></a>"+
						"<div class=\"sprite settings admin-button blue-dropdown-radio\" id=\"settings-button\"></div>"+
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
