package ui;

import javax.servlet.http.HttpSession;

public class HTMLTemplater {
	/* indicies into sidebarURLs */
//	private static int NAME = 0;
//	private static int URL = 1;
//	private static String[][] sidebarURLs = {{"Home","/"},{"Quiz","Quiz"},{"Friends","Friends"}};
//
//	public static String getSidebarHTML (String active) {
//		String html = "";
//		html += "<div id=\"app-selection-wrapper\">";
//		html += "	<ul id=\"app-selection-ul\">";
//		for (int i = 0; i < sidebarURLs.length; i++) {
//			String className = active.equals(sidebarURLs[i][NAME]) ? "active" :"";
//			html += "		<li>";
//			html += "			<a href=\""+sidebarURLs[i][URL]+"\" class=\""+className+"\">"+sidebarURLs[i][NAME]+"</a>";
//			html += "		</li>";
//		}
//		html += "	</ul>";
//		html += "</div>";
//		return html;
//	}
//	
	public static String getBlueBar (HttpSession session) {
		String username = "Bobby Womack";
		String user_url = "http://www.imgur.com";
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
						"<a class=\"header-text\" href=\""+user_url+"\">"+username+"</a>"+
						"<a class=\"header-text\" href=\"/QuizWebsite/\">Home</a>"+
						"<a class=\"sprite2 admin admin-button\" href=\"/QuizWebsite/Admin.jsp\"></a>"+
					"</div>"+
					"<div class=\"search-bar\">"+
						"<input type=\"text\" id=\"graph-search-bar\" placeholder=\"Search for People, Quizzes, and More\">"+
						"<ul id=\"graph-search-results\">"+
						"</ul>"+	
						"<div class=\"sprite search-mag\" id=\"search-mag\"></div>"+
					"</div>"+
				"</div>"+
			"</div>"+
		"</div>";
	}
	
}
