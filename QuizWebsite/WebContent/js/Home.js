/**
 * 
 */


function init_js(userID) {
	new HomeHandler(document.getElementById("right-content-panel"),
					document.getElementById("achievements-earned-panel"),
					document.getElementById("achievements-not-earned-panel"),
					document.getElementById("popular-quizzes-panel"),
					document.getElementById("created-quizzes-panel"),
					document.getElementById("my-results-panel"),
					document.getElementById("my-friends-panel"),
					'/QuizWebsite/HomeServlet', 
					userID);
}

var _user_id;

function HomeHandler(newsfeed_bar, achievements_earned_panel, achievements_not_earned_panel,
		popular_quizzes_panel, created_quizzes_panel, my_results_panel, my_friends_panel, home_url, user_id) {
	var _newsfeed_bar = newsfeed_bar;
	var _achievements_earned_panel = achievements_earned_panel;
	var _achievements_not_earned_panel = achievements_not_earned_panel;
	var _popular_quizzes_panel = popular_quizzes_panel;
	var _created_quizzes_panel = created_quizzes_panel;
	var _my_results_panel = my_results_panel;
	var _my_friends_panel = my_friends_panel;
	_user_id = user_id;
	
	var _home_url = home_url;
	
	(function init() {
		get_json_from_url(_home_url + '?user_id='+user_id, function (data) {
			console.log(data);
			update_newsfeed(data.friend_results, _newsfeed_bar);
			update_achievements(data.achievements_earned, data.achievements_not_earned, _achievements_earned_panel, _achievements_not_earned_panel);
			update_popular_quizzes(data.popular_quizzes, _popular_quizzes_panel);
			update_created_quizzes(data.created_quizzes, _created_quizzes_panel);
			update_my_results(data.recent_results, _my_results_panel);
			update_my_friends(_my_friends_panel);
		});
		
	})();
}

function update_my_friends(friends_panel) {
	get_json_from_url("/QuizWebsite/RelationServlet?action=friends&user_id=" + _user_id, function (data) {
		console.log(data);
		var friends = data.friends;
		
		var ul = document.createElement('ul');
		for (var i = 0; i < friends.length; i++) {
			var li = document.createElement('li');
			
			var user = friends[i];
			
			var user_link = document.createElement('a');
			user_link.href = "/QuizWebsite/User.jsp?username=" + user.username;
			user_link.innerHTML = user.display_name;
			
			li.appendChild(user_link);
			ul.appendChild(li);
		}
		
		var title = document.createElement('span');
		title.innerHTML = "<u><b>My Friends</b></u>";
		
		var br = document.createElement('br');
		friends_panel.appendChild(title);
		friends_panel.appendChild(br);
		friends_panel.appendChild(ul);
	});
}
function update_newsfeed(friend_results, newsfeed_bar) {
	var ul = document.createElement('ul');
	
	for (var i = 0; i < friend_results.length; i++) {
		var quiz_name = friend_results[i].quiz_name;
		var quiz_id = friend_results[i].quiz_id;
		var user_percentage_score = friend_results[i].user_percentage_score;
		var date = friend_results[i].date;
		var user = friend_results[i].user;
		var li = document.createElement('li');
		
		var quiz_link = document.createElement('a');
		quiz_link.href = "/QuizWebsite/QuizPage.jsp?quiz_id=" + quiz_id;
		quiz_link.innerHTML = quiz_name;
		
		var user_link = document.createElement('a');
		user_link.href = "/QuizWebsite/User.jsp?username=" + user.username;
		user_link.innerHTML = user.display_name;
		
		var quiz_span = document.createElement('span');
		quiz_span.appendChild(quiz_link);
		
		var user_span = document.createElement('span');
		user_span.appendChild(user_link);
		
		var textSpan = document.createElement('span');
		textSpan.innerHTML = " took ";
		
		var textSpan2 = document.createElement('span');
		textSpan2.innerHTML = " and got a score of " + (user_percentage_score * 100).toFixed(0) + "%!";
		
		li.appendChild(user_link);
		li.appendChild(textSpan);
		li.appendChild(quiz_link);
		li.appendChild(textSpan2);
		
		ul.appendChild(li);
	}

	newsfeed_bar.appendChild(ul);
}

function update_achievements(achvs_earned, achvs_not_earned, achvs_earned_div, achvs_not_earned_div) {
	var achvs_earned_ul = document.createElement('ul');
	
	for (var i = 0; i < achvs_earned.length; i++) {
		var title = achvs_earned[i].title;
		var desc = achvs_earned[i].description;
		
		var li = document.createElement('li');
		li.innerHTML = title + " - " + desc;
		achvs_earned_ul.appendChild(li);
	}
	var br = document.createElement('br');
	var title = document.createElement('span');
	title.innerHTML = "<u><b>Achievements Unlocked:</b></u>";
	
	achvs_earned_div.appendChild(title);
	achvs_earned_div.appendChild(br);
	achvs_earned_div.appendChild(achvs_earned_ul);
	
	var achvs_not_earned_ul = document.createElement('ul');
	for (var i = 0; i < achvs_not_earned.length; i++) {
		var title = achvs_not_earned[i].title;
		var desc = achvs_not_earned[i].description;
		
		var li = document.createElement('li');
		li.innerHTML = title + " - " + desc;
		achvs_not_earned_ul.appendChild(li);
	}
	
	var title2 = document.createElement('span');
	title2.innerHTML = "<u><b>Achievements Not Unlocked:</b></u>";
	
	achvs_not_earned_div.appendChild(title2);
	achvs_not_earned_div.appendChild(br);
	achvs_not_earned_div.appendChild(achvs_not_earned_ul);
}

function update_popular_quizzes(popular_quizzes, panel) {
	var ul = document.createElement('ul');

	for (var i = 0; i < popular_quizzes.length; i++) {
		var li = document.createElement('li');
		
		var quiz_name = popular_quizzes[i].quiz_name;
		var creator = popular_quizzes[i].creator;
		var quiz_id = popular_quizzes[i].quiz_id;
		var frequency = popular_quizzes[i].frequency;
		
		var quiz_link = document.createElement('a');
		quiz_link.href = "/QuizWebsite/QuizPage.jsp?quiz_id=" + quiz_id;
		quiz_link.innerHTML = quiz_name;
		
		var user_link = document.createElement('a');
		user_link.href = "/QuizWebsite/User.jsp?username=" + creator;
		user_link.innerHTML = creator;
		
		var frequency_span = document.createElement('span');
		frequency_span.innerHTML = frequency;
		
		var text = document.createElement('span');
		text.innerHTML = " created by: ";
		
		var text2 = document.createElement('span');
		text2.innerHTML = " taken by: ";
		
		var text3 = document.createElement('span');
		text3.innerHTML = " users";
		
		li.appendChild(quiz_link);
		li.appendChild(text);
		li.appendChild(user_link);
		li.appendChild(text2);
		li.appendChild(frequency_span);
		li.appendChild(text3);
		ul.appendChild(li);
	}
	
	var title = document.createElement('span');
	title.innerHTML = "<u><b>Popular Quizzes</b></u>";
	var br = document.createElement('br');
	
	panel.appendChild(title);
	panel.appendChild(br);
	panel.appendChild(ul);
}

function update_created_quizzes(created_quizzes, panel) {
	var ul = document.createElement('ul');

	for (var i = 0; i < created_quizzes.length; i++) {
		var li = document.createElement('li');
		
		var quiz_name = created_quizzes[i].quiz_name;
		var quiz_id = created_quizzes[i].quiz_id;
		
		var quiz_link = document.createElement('a');
		quiz_link.href = "/QuizWebsite/QuizPage.jsp?quiz_id=" + quiz_id;
		quiz_link.innerHTML = quiz_name;
		
		li.appendChild(quiz_link);
		
		ul.appendChild(li);
	}
	
	var title = document.createElement('span');
	title.innerHTML = "<u><b>My Quizzes</b></u>";
	var br = document.createElement('br');
	
	panel.appendChild(title);
	panel.appendChild(br);
	panel.appendChild(ul);
}

function update_my_results(my_results, panel) {
	var ul = document.createElement('ul');

	for (var i = 0; i < my_results.length; i++) {
		var li = document.createElement('li');
		
		var quiz_name = my_results[i].quiz_name;
		var quiz_id = my_results[i].quiz_id;
		var date = my_results[i].date;
		var year = date.year;
		var month = date.month;
		var date = date.date;
		var time_taken = my_results[i].time_taken;
		var user_percentage_score = my_results[i].user_percentage_score;
		
		var quiz_link = document.createElement('a');
		quiz_link.href = "/QuizWebsite/QuizPage.jsp?quiz_id=" + quiz_id;
		quiz_link.innerHTML = quiz_name;
		
		var text = document.createElement('span');
		text.innerHTML = ": score of " + (user_percentage_score * 100).toFixed(0) + " in " + (time_taken).toFixed(1) + " seconds"; 
		
		li.appendChild(quiz_link);
		li.appendChild(text);
		ul.appendChild(li);
	}
	
	var title = document.createElement('span');
	title.innerHTML = "<u><b>My Recent Results</b></u>";
	var br = document.createElement('br');
	
	panel.appendChild(title);
	panel.appendChild(br);
	panel.appendChild(ul);
}