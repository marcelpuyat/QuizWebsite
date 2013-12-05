/**
 * 
 */
var _is_admin;
var _user_id;

var adminServletURL = "/QuizWebsite/AdminServlet";
var announcementsServletURL = "/QuizWebsite/AnnouncementServlet";

var stats_div = document.getElementById("stats-panel");
var stats_ul = document.getElementById("stats-list");

var announcements_div = document.getElementById("announcements-panel");
var announcements_ul = document.getElementById("announcements-list");

var post_announcements_div = document.getElementById("announcement-post-panel");

var subject_field = document.getElementById("subject-field");
var body_field = document.getElementById("body-field");
var post_button = document.getElementById("post-announcement-button");

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
		update_announcements();
		setup_announcement_poster();
	})();
	
}

function update_stats() {
	showDiv(stats_div);
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
		
		num_users_li.classList.add("stats-li");
		quizzes_made_li.classList.add("stats-li");
		quizzes_taken_li.classList.add("stats-li");
		
		stats_ul.appendChild(num_users_li);
		stats_ul.appendChild(quizzes_made_li);
		stats_ul.appendChild(quizzes_taken_li);
	});
}

function update_announcements() {
	get_json_from_url(announcementsServletURL, function (data) {
		var announcements = data.announcements;
		for (var i = 0; i < announcements.length; i++) {
			var poster = announcements[i].admin;
			var isMyAnnouncement = (poster.id == _user_id);
			var posterName;
			if (isMyAnnouncement)
				poster_name = "You";
			else poster_name = poster.first_name + " " + poster.last_name;
			
			var announcement_id = announcements[i].announcement_id;
			var subject = announcements[i].subject;
			var body = announcements[i].body;
			var date = announcements[i].date;
				var year = date.year;
				var month = date.month;
				var day = date.date;
			
			var announcement_li = document.createElement('li');
			var subject_header = document.createElement('h4');
			subject_header.innerHTML = subject;
			
			var br = document.createElement('br');
			var body_text = document.createElement('p');
			body_text.innerHTML = body;
			
			var br2 = document.createElement('br');
			
			var date_string = year + " / " + month + " / " + day + " - by " + poster_name;
			var date_span = document.createElement('span');
			date_span.innerHTML = date_string;
			
			var deleteButton = document.createElement('button');
			deleteButton.innerHTML = "Delete";
			deleteButton.id = announcement_id;
			deleteButton.addEventListener('click', function () {
				
				delete_announcement(this.id, this);
				
			});
			
			announcement_li.appendChild(subject_header);
			announcement_li.appendChild(br);
			announcement_li.appendChild(body_text);
			announcement_li.appendChild(br2);
			announcement_li.appendChild(date_span);
			if (isMyAnnouncement)
				announcement_li.appendChild(deleteButton);
			
			announcement_li.classList.add("announcement-li");

			if (i < announcements.length - 1)
				announcement_li.classList.add("bottom-line");
			announcements_ul.appendChild(announcement_li);
		}
		
	});
}

function setup_announcement_poster() {
	showDiv(post_announcements_div);
	post_button.addEventListener("click", function() {
		post_announcement();
	});
}

function delete_announcement(id, button) {
	hideElem(button);
	post_json_to_url(announcementsServletURL + "?action=delete&announcement_id=" + id, {}, function(){});
}

function post_announcement() {
	hideElem(post_button);
	post_json_to_url(announcementsServletURL + "?action=post", {user_id: _user_id, subject: subject_field.value, body: body_field.value}, function(){});
}

function showDiv(div) {
	div.classList.remove("hide");
}

function hideElem(elem) {
	elem.classList.add("hide");
}