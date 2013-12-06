/* Global divs */
var recent_results_div = document.getElementById("recent-results");
var created_quizzes_div = document.getElementById("created-quizzes");
var achievements_div = document.getElementById("achievements");
var relation_controls_div = document.getElementById("relation-controls");
var status_div = document.getElementById("relation-status");

/* Buttons */
var message_button = document.getElementById("message-button");
var accept_button = document.getElementById("accept-request-button");
var reject_button = document.getElementById("reject-request-button");
var block_button = document.getElementById("block-user-button");
var unblock_button = document.getElementById("unblock-user-button");
var delete_friend_button = document.getElementById("delete-friend-button");
var request_button = document.getElementById("request-button");
var delete_user_button = document.getElementById("remove-user-button");
var promote_button = document.getElementById("promote-user-button");

/* masthead */
var masthead_friends_div = document.getElementById('masthead-friends-div');

/* Servlet URL for getting JSON */
var profileServletURL = "/QuizWebsite/ProfileServlet";

/* Servlet URL for getting relation details */
var relationServletURL = "/QuizWebsite/RelationServlet";

var default_profile_picture = 'http://0.tqn.com/d/menshair/1/0/l/5/-/-/round-wood.jpg';

/* Relation details */
var is_friend = false;
var is_blocked = false;
var has_blocked = false;
var pending_request = false;
var has_requested = false;
var is_self = false;

/* Admin */
var _show_promote;
var _show_delete;

/* viewer id and viewee id */
var _curr_user_id;
var _target_user_id;

/* I just store everything globally */
function init_js(curr_user_id, target_user_id, show_promote, show_delete) {
	_curr_user_id = curr_user_id;
	_target_user_id = target_user_id;
	_show_delete = show_delete;
	_show_promote = show_promote;
	
	if (curr_user_id == target_user_id) is_self = true;

	set_relations();
	set_background_photos();
	get_json_from_url(profileServletURL + '?user_id=' + target_user_id, function (data) {

		console.log(data);

		/* display page */
		document.getElementById('inner-content-container').classList.remove('hide');

		/* populate profile img */
		if (data.user_info && data.user_info.profile_picture && data.user_info.profile_picture != '') {
			document.getElementById('user-photo').style.backgroundImage = 'url(\''+data.user_info.profile_picture+'\')';
		} else {
			document.getElementById('user-photo').style.backgroundImage = 'url(\''+default_profile_picture+'\')';
		}

		if (is_blocked) {
			BlockedPageHandler(data.user_info);
		}

		/* If not blocked, display basic user details */
		else {
			message_button.classList.remove('hide');
			display_relation_controls(data.user_info);
			display_created_quizzes(data.created_quizzes);

			/* if friend, display more */
			if (is_friend || is_self) {
				new FriendPageHandler(data);
			}

		}



	});


}

/**
 * Gets all friends of user and displays them in the background
 */
function set_background_photos () {
	console.log('fetching relations');
	get_json_from_url(relationServletURL + '?user_id='+_target_user_id,
		function (data) {
			console.log('got relations');
			console.log(data);
			data.friends.shuffle();
			for (var i = 0; i < data.friends.length; i++) {
				var user = data.friends[i]
				var profile_picture = (user.profile_picture && user.profile_picture != "") ? user.profile_picture : default_profile_picture;
				var photo = new_elem({
					type:'a',
					attributes:[
						{name:'href',value:'/QuizWebsite/User.jsp?user_id='+user.id},
						{name:'style',value:'background-image:url('+profile_picture+');'},
						{name:'title',value:user.display_name}
					],
					classList:['parent-height','parent-width']
				});
				masthead_friends_div.appendChild(new_elem({
					type:'span',
					children:[photo],
					classList:['friend-photo']
				}));
			};
		})
}

/**
 * Find out relation booleans between users
 */
function set_relations() {

	/* Get info about viewer to viewee */
	get_json_from_url(relationServletURL + "?user_a_id=" + _curr_user_id + "&user_b_id=" + _target_user_id, function (data) {
		console.log(data);
		if (data.status == "REQ") pending_request = true;
		else if (data.status == "FRD") is_friend = true;
		else if (data.status == "BLK") has_blocked = true;
	});

	/* Get info about viewee to viewer */
	get_json_from_url(relationServletURL + "?user_a_id=" + _target_user_id + "&user_b_id=" + _curr_user_id, function (data) {
		console.log(data);
		if (data.status == "REQ") has_requested = true;
		else if (data.status == "BLK") is_blocked = true;
	});
}

/* Friends see achievements and recent results */
function FriendPageHandler(data) {
	(function init() {
		display_achievements(data.achievements);
		display_recent_results(data.recent_results);
	})();
}

/* Go ahead and style if you want... */
function BlockedPageHandler(user_info) {
	relation_controls_div.innerHTML = user_info.username + " has blocked you.";
}


/* PLEASE STYLE THIS. FUNCTIONALITY OF BUTTONS SHOULD ALREADY BE CORRECT */
function display_relation_controls(user_info) {

	/* Hides options depending on what should show given current status */
	if (is_friend) {
		showButton(block_button);
		showButton(delete_friend_button);
		status_div.innerHTML = "is your friend";
	}
	else if (has_blocked) {
		showButton(unblock_button);
		status_div.innerHTML = "is blocked";
	} else if (has_requested) {
		showButton(block_button);
		showButton(accept_button);
		showButton(reject_button);
		status_div.innerHTML = "has sent you a friend request";
	} else if (pending_request) {
		status_div.innerHTML = "friendship pending";
		showButton(block_button);
	} else if (is_self) {
		message_button.classList.add('hide');
	} else {
		showButton(request_button);
		showButton(block_button);
	}
	
	if (_show_delete) {
		showButton(delete_user_button);
	}
	if (_show_promote) {
		showButton(promote_button);
	}
}

/* Go ahead and style this */
function display_achievements(achievements) {
	var ul = document.createElement('ul');
	for (var i = 0; i < achievements.length; i++) {
		var li = document.createElement('li');
		var title = achievements[i].title;
		var desc = achievements[i].description;
		li.innerHTML = title + " - " + desc;
		ul.appendChild(li);
	}
	achievements_div.appendChild(ul);
}

/* Same */
function display_created_quizzes(created_quizzes) {
	var ul = document.createElement('ul');
	for (var i = 0; i < created_quizzes.length; i++) {
		var li = document.createElement('li');
		var quiz_name = created_quizzes[i].quiz_name;
		var quiz_id = created_quizzes[i].quiz_id;

		var quiz_link = document.createElement('a');
		quiz_link.classList.add("quiz-link");
		quiz_link.href = "/QuizWebsite/QuizPage.jsp?quiz_id=" + quiz_id;
		quiz_link.innerHTML = quiz_name;

		li.appendChild(quiz_link);
		ul.appendChild(li);
	}
	created_quizzes_div.appendChild(ul);
}

/* have access to date */
function display_recent_results(recent_results) {
	var ul = document.createElement('ul');
	for (var i = 0; i < recent_results.length; i++) {
		var li = document.createElement('li');
		var quiz_name = recent_results[i].quiz_name;
		var quiz_id = recent_results[i].quiz_id;
		var date = recent_results[i].date;
		var quiz_link = document.createElement('a');
		quiz_link.classList.add("quiz-link");
		quiz_link.href = "/QuizWebsite/QuizPage.jsp?quiz_id=" + quiz_id;
		quiz_link.innerHTML = quiz_name;

		var user_percentage_score = recent_results[i].user_percentage_score;
		var time_taken = recent_results[i].time_taken;

		var span = document.createElement('span');
		span.innerHTML = (user_percentage_score * 100).toFixed(0) + "% in " + (time_taken).toFixed(1) + " seconds - ";
		
		var time_ago = document.createElement('span');
		time_ago.classList.add("time");
		time_ago.innerHTML = " " + get_time_ago(date);
		
		li.appendChild(span);
		li.appendChild(quiz_link);
		li.appendChild(time_ago);
		ul.appendChild(li);
	}
	recent_results_div.appendChild(ul);
}

function showButton(button) {
	button.classList.remove('hide');
}

function removeUser() {
	var should_remove = confirm("Are you sure you want to delete this user from the database?");
	if (should_remove) {
		relation_controls_div.innerHTML = "User deleted";
		post_json_to_url("/QuizWebsite/AdminServlet?action=remove_user&user_id=" + _target_user_id,
				{
			
				},
				function () {
					
				});
		alert("User deleted");
		window.location = "/QuizWebsite/Home.jsp";
	}
}

function promoteUser() {
	var should_promote = confirm("Are you sure you want to promote this user to admin?");
	if (should_promote) {
		relation_controls_div.innerHTML = "User promoted";
		post_json_to_url("/QuizWebsite/AdminServlet?action=promote_user&user_id=" + _target_user_id,
			{
		
			},
			function (data) {
				
			}
		);
		location.reload(true);
		alert("User promoted");
	}
}

/* I just need a POST req but didnt know how to. So i used an empty post json */
function removeFriend() {
	relation_controls_div.innerHTML = "you are no longer friends";
	post_json_to_url(
		relationServletURL + "?user_a_id=" + _curr_user_id + "&user_b_id=" + _target_user_id + "&status=DELETE",
		{

		}, 
		function (data) {
			location.reload();
		}
	);
}
function sendRequest() {
	relation_controls_div.innerHTML = "Friend Request sent";
	post_json_to_url(
		relationServletURL + "?user_a_id=" + _curr_user_id + "&user_b_id=" + _target_user_id + "&status=REQ",
		{
			
		}, 
		function (data) {
			location.reload();
		}
	);
}
function blockUser() {
	relation_controls_div.innerHTML = "User blocked";
	post_json_to_url(
		relationServletURL + "?user_a_id=" + _curr_user_id + "&user_b_id=" + _target_user_id + "&status=BLK",
		{
			
		}, 
		function (data) {
			location.reload();
		}
	);
}
function unblockUser() {
	relation_controls_div.innerHTML = "User unblocked";
	post_json_to_url(
		relationServletURL + "?user_a_id=" + _curr_user_id + "&user_b_id=" + _target_user_id + "&status=UNBLK",
		{
			
		}, 
		function (data) {
			location.reload();
		}
	);
}
function rejectRequest() {
	relation_controls_div.innerHTML = "Request rejected";
	post_json_to_url(
		relationServletURL + "?user_a_id=" + _curr_user_id + "&user_b_id=" + _target_user_id + "&status=NO",
		{
			
		}, 
		function (data) {
			location.reload();
		}
	);
}
function acceptRequest() {
	relation_controls_div.innerHTML = "Request accepted";
	post_json_to_url(
		relationServletURL + "?user_a_id=" + _curr_user_id + "&user_b_id=" + _target_user_id + "&status=FRD",
		{
			
		}, 
		function (data) {
			location.reload();
		}
	);
}
