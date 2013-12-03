/**
 * 
 */


function init_js(userID) {
	new NewsFeedHandler(document.getElementById("right-content-panel"), '/QuizWebsite/HomeServlet', userID);
}

function NewsFeedHandler(newsfeed_bar, home_url, user_id) {
	var _newsfeed_bar = newsfeed_bar;
	var _home_url = home_url;
	
	(function init() {
		get_json_from_url(_home_url + '?user_id='+user_id, function (data) {
			console.log(data);
			update_display(data, _newsfeed_bar);
		});
		
	})();
}

function update_display(data, newsfeed_bar) {
	console.log(newsfeed_bar);
	var friend_results = data.friend_results;
	var ul = document.createElement('ul');
	
	for (var i = 0; i < friend_results.length; i++) {
		var quiz_name = friend_results[i].quiz_name;
		var quiz_id = friend_results[i].quiz_id;
		var user_percentage_score = friend_results[i].user_percentage_score;
		var date = friend_results[i].date;
		var user = friend_results[i].user;
		console.log(quiz_name);
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