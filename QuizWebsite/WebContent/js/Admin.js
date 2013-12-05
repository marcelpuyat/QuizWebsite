/**
 * 
 */
var _is_admin;
var _user_id;

var adminServletURL = "/QuizWebsite/AdminServlet";

var stats_div = document.getElementById("stats-panel");
var stats_ul = document.getElementById("stats-list");

function init_js(user_id, is_admin) {
	_is_admin = is_admin;
	_user_id = user_id;
	
	if (_is_admin) {
		new AdminHandler();
	}
	
	else {
		new UserHandler();
	}
}

function AdminHandler() {
	(function update() {
		update_stats();
	})();
	
}

function update_stats() {
	get_json_from_url(adminServletURL, function (data) {
		var num_users = data.num_users;
		var quizzes_made = data.num_quizzes_made;
		var quizzes_taken = data.num_quizzes_taken;
		
		var num_users_li = document.createElement('li');
		num_users_li.innerHTML = "Number of users: " + num_users;
		
		var quizzes_made_li = document.createElement('li');
		quizzes_made_li.innerHTML = "Number of quizzes created: " + quizzes_made;
		
		var quizzes_taken_li = document.createElement('li');
		quizzes_taken_li.innerHTML = "Number of quiz-taking instances: " + quizzes_taken;
		
		stats_ul.appendChild(num_users_li);
		stats_ul.appendChild(quizzes_made_li);
		stats_ul.appendChild(quizzes_taken_li);

	});
}