/**
 * 
 */


function init_js(userID) {
	new HomeHandler(document.getElementById("newsfeed"),
					document.getElementById("achievements-earned-panel"),
					document.getElementById("achievements-not-earned-panel"),
					document.getElementById("popular-quizzes-panel"),
					document.getElementById("highest-rated-quizzes-panel"),
					document.getElementById("newest-quizzes-panel"),
					document.getElementById("created-quizzes-panel"),
					document.getElementById("my-results-panel"),
					document.getElementById("my-friends-panel"),
					'/QuizWebsite/HomeServlet', 
					userID);
}

var _user_id;

function HomeHandler(newsfeed_bar, achievements_earned_panel, achievements_not_earned_panel,
		popular_quizzes_panel, highest_rated_quizzes_panel, newest_quizzes_panel, created_quizzes_panel, my_results_panel, my_friends_panel, home_url, user_id) {
	var _newsfeed_bar = newsfeed_bar;
	var _achievements_earned_panel = achievements_earned_panel;
	var _achievements_not_earned_panel = achievements_not_earned_panel;
	var _popular_quizzes_panel = popular_quizzes_panel;
	var _highest_rated_quizzes_panel = highest_rated_quizzes_panel;
	var _newest_quizzes_panel = newest_quizzes_panel;
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
			update_highest_rated_quizzes(data.highest_rated_quizzes, highest_rated_quizzes_panel);
			update_newest_quizzes(data.newest_quizzes, _newest_quizzes_panel);
			update_created_quizzes(data.created_quizzes, _created_quizzes_panel);
			update_my_results(data.recent_results, _my_results_panel);
			update_my_friends(_my_friends_panel);
		});
		
	})();
}

function update_my_friends(panel) {
	get_json_from_url("/QuizWebsite/RelationServlet?action=friends&user_id=" + _user_id, function (data) {
		console.log(data);
		var friends = data.friends;
		
		var lis = [];
		for (var i = 0; i < friends.length; i++) {
			
			var user = friends[i];
			
			lis.push(new_elem({
				type:'a',
				attributes:[
					{name:'href',value:"/QuizWebsite/User.jsp?username=" + user.username},
					{name:'title',value:user.display_name},
					{name:'style',value:'background-image:url('+user.profile_picture+');'}
				],
				classList:['mini-profile-pic','flowing']
			}));
		}
		panel.appendChild(new_elem({
			type:'ul',
			children:lis,
			classList:['center','profile-pic-ul']
		}));
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
		quiz_link.classList.add('quiz-link');
		quiz_link.href = "/QuizWebsite/QuizPage.jsp?quiz_id=" + quiz_id;
		quiz_link.innerHTML = quiz_name;
		
		var user_link = document.createElement('a');
		user_link.classList.add('user-link');
		user_link.href = "/QuizWebsite/User.jsp?username=" + user.username;
		user_link.innerHTML = user.display_name;
		
		var quiz_span = document.createElement('span');
		quiz_span.appendChild(quiz_link);
		
		var user_span = document.createElement('span');
		user_span.appendChild(user_link);
		
		var textSpan = document.createElement('span');
		textSpan.innerHTML = " took ";
		
		var textSpan2 = document.createElement('span');
		var time_ago = document.createElement('span');
		time_ago.classList.add('time');
		time_ago.innerHTML = " " + get_time_ago(date);
		textSpan2.innerHTML = " and got a score of " + (user_percentage_score * 100).toFixed(0) + "%!";
		
		li.appendChild(user_link);
		li.appendChild(textSpan);
		li.appendChild(quiz_link);
		li.appendChild(textSpan2);
		li.appendChild(time_ago);
		
		ul.appendChild(li);
	}

	newsfeed_bar.appendChild(ul);
}

function update_achievements(achvs_earned, achvs_not_earned, achvs_earned_div, achvs_not_earned_div) {
	console.log("ACHIEVEMENTS NOT EARNED:");
	console.log(achvs_not_earned);
	var achvs_earned_ul = new_elem({
		type:'ul',
		classList:['center']
	});
	
	for (var i = 0; i < achvs_earned.length; i++) {
		var title = achvs_earned[i].title;
		var desc = achvs_earned[i].description;
		
		var li = new_elem({
			type:'li',
			classList:[acheivements_map[title],'trophy-sprite','flowing'],
			attributes:[
				{name:'title',value:title + ': '+desc}
			]
		});
		achvs_earned_ul.appendChild(li);
	}

	achvs_earned_div.appendChild(new_elem({
		type:'h2',
		innerHTML:'Achievements Unlocked',
		classList:['center']
	}));
	achvs_earned_div.appendChild(achvs_earned_ul);
	


	var achvs_not_earned_ul = new_elem({
		type:'ul',
		classList:['center']
	});
	for (var i = 0; i < achvs_not_earned.length; i++) {
		var title = achvs_not_earned[i].title;
		var desc = achvs_not_earned[i].description;
		
		var li = new_elem({
			type:'li',
			classList:[acheivements_map[title],'trophy-sprite','flowing','trophy-not-unlocked'],
			attributes:[
				{name:'title',value:title + ': '+desc}
			]
		});
		achvs_not_earned_ul.appendChild(li);
	}
	
	achvs_not_earned_div.appendChild(new_elem({
		type:'h2',
		innerHTML:'Achievements Not Unlocked',
		classList:['center']
	}));
	achvs_not_earned_div.appendChild(achvs_not_earned_ul);
}

function update_popular_quizzes(popular_quizzes, panel) {
	var ul = new_elem({
		type:'ul',
		classList:['center']
	});

	var title = new_elem({
		type:'h2',
		classList:['center'],
		innerHTML:'Trending Quizzes'
	});

	for (var i = 0; i < popular_quizzes.length; i++) {
		var li = document.createElement('li');
		
		var quiz_name = popular_quizzes[i].quiz_name;
		var creator = popular_quizzes[i].creator;
		var quiz_id = popular_quizzes[i].quiz_id;
		var frequency = popular_quizzes[i].frequency;
		
		
		li.appendChild(new_elem({
			type:'a',
			classList:['quiz-link'],
			innerHTML:quiz_name,
			attributes:[
				{name:'href',value:"/QuizWebsite/QuizPage.jsp?quiz_id=" + quiz_id}
			]
		}));
		
		li.appendChild(new_elem({
			type:'span',
			innerHTML:' by '
		}));
		
		li.appendChild(new_elem({
			type:'a',
			classList:['user-link'],
			innerHTML:creator,
			attributes:[
				{name:'href',value:"/QuizWebsite/User.jsp?username=" + creator}
			]
		}));

		var played_text = '';
		switch(frequency) {
			case 0:
				played_text += ' has never been played';
				break;
			case 1:
				played_text += ' been played once';
				break;
			case 2:
				played_text += ' has been played twice';
				break;
			default:
				played_text += ' has been played ' + frequency + ' times';
		}
		li.appendChild(new_elem({
			type:'span',
			innerHTML:played_text
		}));

		ul.appendChild(li);
	}
	
	panel.appendChild(title);
	panel.appendChild(ul);
}

function update_highest_rated_quizzes(highest_rated_quizzes, panel) {
	var ul = new_elem({
		type:'ul',
		classList:['center']
	});

	var title = new_elem({
		type:'h2',
		classList:['center'],
		innerHTML:'Highest Rated Quizzes'
	});

	for (var i = 0; i < highest_rated_quizzes.length; i++) {
		var li = document.createElement('li');
		
		var quiz_name = highest_rated_quizzes[i].quiz_name;
		var creator = highest_rated_quizzes[i].creator;
		var quiz_id = highest_rated_quizzes[i].quiz_id;
		var rating = highest_rated_quizzes[i].average_rating;
		
		li.appendChild(new_elem({
			type:'a',
			classList:['quiz-link'],
			innerHTML:quiz_name,
			attributes:[
				{name:'href',value:"/QuizWebsite/QuizPage.jsp?quiz_id=" + quiz_id}
			]
		}));
		
		li.appendChild(new_elem({
			type:'span',
			innerHTML:' by '
		}));
		
		li.appendChild(new_elem({
			type:'a',
			classList:['user-link'],
			innerHTML:creator,
			attributes:[
				{name:'href',value:"/QuizWebsite/User.jsp?username=" + creator}
			]
		}));
		
		var played_text = ' has a '+ rating.toFixed(1) + ' star rating';

		li.appendChild(new_elem({
			type:'span',
			innerHTML:played_text
		}));

		ul.appendChild(li);
	}
	
	panel.appendChild(title);
	panel.appendChild(ul);
}

function update_newest_quizzes(newest_quizzes, panel) {
	var ul = new_elem({
		type:'ul',
		classList:['center']
	});

	var title = new_elem({
		type:'h2',
		classList:['center'],
		innerHTML:'Newest Quizzes'
	});

	for (var i = 0; i < newest_quizzes.length; i++) {
		var li = document.createElement('li');
		
		var quiz_name = newest_quizzes[i].quiz_name;
		var creator = newest_quizzes[i].creator;
		var quiz_id = newest_quizzes[i].quiz_id;
		
		
		li.appendChild(new_elem({
			type:'a',
			classList:['quiz-link'],
			innerHTML:quiz_name,
			attributes:[
				{name:'href',value:"/QuizWebsite/QuizPage.jsp?quiz_id=" + quiz_id}
			]
		}));
		
		li.appendChild(new_elem({
			type:'span',
			innerHTML:' by '
		}));
		
		li.appendChild(new_elem({
			type:'a',
			classList:['user-link'],
			innerHTML:creator,
			attributes:[
				{name:'href',value:"/QuizWebsite/User.jsp?username=" + creator}
			]
		}));

		ul.appendChild(li);
	}
	
	panel.appendChild(title);
	panel.appendChild(ul);
}


function update_created_quizzes(created_quizzes, panel) {
	panel.appendChild(new_elem({
		type:'h2',
		classList:['center'],
		innerHTML:'Your Quizzes'
	}));
	var lis = [];
	for (var i = 0; i < created_quizzes.length; i++) {
		var li = document.createElement('li');
		
		var quiz_name = created_quizzes[i].quiz_name;
		var quiz_id = created_quizzes[i].quiz_id;
		
		li.appendChild(new_elem({
			type:'a',
			classList:['quiz-link'],
			innerHTML:quiz_name,
			attributes:[
				{name:'href',value:"/QuizWebsite/QuizPage.jsp?quiz_id=" + quiz_id}
			]
		}));
		
		lis.push(li);
	}

	panel.appendChild(new_elem({
		type:'ul',
		classList:['center'],
		children:lis
	}));
}


function update_my_results(my_results, panel) {
	panel.appendChild(new_elem({
		type:'h2',
		classList:['center'],
		innerHTML:'Recent Results'
	}));
	var lis = [];
	for (var i = 0; i < my_results.length; i++) {
		var li = document.createElement('li');
		
		var quiz_name = my_results[i].quiz_name;
		var quiz_id = my_results[i].quiz_id;
		var date = my_results[i].date;
		var time_taken = my_results[i].time_taken;
		var user_percentage_score = my_results[i].user_percentage_score * 100;
		
		li.appendChild(new_elem({
			type:'span',
			innerHTML:'You got '+ user_percentage_score.toFixed(0) +'% playing '
		}));

		li.appendChild(new_elem({
			type:'a',
			classList:['quiz-link'],
			innerHTML:quiz_name,
			attributes:[
				{name:'href',value:"/QuizWebsite/QuizPage.jsp?quiz_id=" + quiz_id}
			]
		}));	

		li.appendChild(new_elem({
			type:'span',
			innerHTML:' '+get_time_ago(date) + ' and finished in just '+ time_taken +' seconds'
		}));
		
		lis.push(li);
	}

	panel.appendChild(new_elem({
		type:'ul',
		classList:['center'],
		children:lis
	}));
}


function show_history() {
	window.location = "/QuizWebsite/History.jsp"
}
