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
		li.innerHTML = user.display_name + " took " + quiz_name + " and got a score of " + (user_percentage_score * 100).toFixed(0) + "%!";
		ul.appendChild(li);
	}
	
	newsfeed_bar.appendChild(ul);
}