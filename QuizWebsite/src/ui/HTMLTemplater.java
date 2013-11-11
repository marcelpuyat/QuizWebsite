package ui;

public class HTMLTemplater {
	/* indicies into sidebarURLs */
	private static int NAME = 0;
	private static int URL = 1;
	private static String[][] sidebarURLs = {{"Home","/"},{"Quiz","Quiz"},{"Friends","Friends"}};

	public static String getSidebarHTML (String active) {
		String html = "";
		html += "<div id=\"app-selection-wrapper\">";
		html += "	<ul id=\"app-selection-ul\">";
		for (int i = 0; i < sidebarURLs.length; i++) {
			String className = active.equals(sidebarURLs[i][NAME]) ? "active" :"";
			html += "		<li>";
			html += "			<a href=\""+sidebarURLs[i][URL]+"\" class=\""+className+"\">"+sidebarURLs[i][NAME]+"</a>";
			html += "		</li>";
		}
		html += "	</ul>";
		html += "</div>";
		return html;
	}
	
}
