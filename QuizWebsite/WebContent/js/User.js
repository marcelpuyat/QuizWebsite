/* Global divs */
var recent_results_div = document.getElementById("recent-results");
var created_quizzes_div = document.getElementById("created-quizzes");
var achievements_div = document.getElementById("achievements");
var user_info_div = document.getElementById("user-info");
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


/* Servlet URL for getting JSON */
var profileServletURL = "/QuizWebsite/ProfileServlet";

/* Servlet URL for getting relation details */
var relationServletURL = "/QuizWebsite/RelationServlet";

/* Relation details */
var is_friend = false;
var is_blocked = false;
var has_blocked = false;
var pending_request = false;
var has_requested = false;
var is_self = false;

/* viewer id and viewee id */
var _curr_user_id;
var _target_user_id;

/* I just store everything globally */
function init_js(curr_user_id, target_user_id) {
	_curr_user_id = curr_user_id;
	_target_user_id = target_user_id;
	if (curr_user_id == target_user_id) is_self = true;

	set_relations();
	get_json_from_url(profileServletURL + '?user_id=' + target_user_id, function (data) {

		console.log(data);

		/* display page */
		document.getElementById('inner-content-container').classList.remove('hide');

		/* populate profile img */
		if (data.user_info && data.user_info.profile_picture && data.user_info.profile_picture != '') {
			document.getElementById('user-photo').style.backgroundImage = 'url(\''+data.user_info.profile_picture+'\')';
		}

		if (is_blocked) {
			BlockedPageHandler(data.user_info);
		}

		/* If not blocked, display basic user details */
		else {
			message_button.classList.remove('hide');
			display_user_info(data.user_info);
			display_relation_controls(data.user_info);
			display_created_quizzes(data.created_quizzes);

			/* if friend, display more */
			if (is_friend) {
				new FriendPageHandler(data);
			}

		}



	});


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

/* PLEASE STYLE THIS */
function display_user_info(user_info) {
	var ul = document.createElement('ul');

	var username_li = document.createElement('li');
	var profile_picture_li = document.createElement('li');
	var first_name_li = document.createElement('li');
	var last_name_li = document.createElement('li');
	var display_name_li = document.createElement('li');

	/* TODO: Display profile picture */

	username_li.innerHTML = "Username: " + user_info.username;
	first_name_li.innerHTML = "First Name: " + user_info.first_name;
	last_name_li.innerHTML = "Last Name: " + user_info.last_name;
	display_name_li.innerHTML = "Display Name: " + user_info.display_name;

	ul.appendChild(username_li);
	ul.appendChild(first_name_li);
	ul.appendChild(last_name_li);
	ul.appendChild(display_name_li);

	user_info_div.appendChild(ul);
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

		var quiz_link = document.createElement('a');
		quiz_link.href = "/QuizWebsite/QuizPage.jsp?quiz_id=" + quiz_id;
		quiz_link.innerHTML = quiz_name;

		var user_percentage_score = recent_results[i].user_percentage_score;
		var time_taken = recent_results[i].time_taken;

		var span = document.createElement('span');
		span.innerHTML = (user_percentage_score * 100).toFixed(0) + "% in " + (time_taken).toFixed(1) + " seconds - ";

		li.appendChild(span);
		li.appendChild(quiz_link);
		ul.appendChild(li);
	}
	recent_results_div.appendChild(ul);
}

function showButton(button) {
	button.classList.remove('hide');
}

/* I just need a POST req but didnt know how to. So i used an empty post json */
function removeFriend() {
	relation_controls_div.innerHTML = "you are no longer friends";
	post_json_to_url(
		relationServletURL + "?user_a_id=" + _curr_user_id + "&user_b_id=" + _target_user_id + "&status=DELETE",
		{

		}, 
		function (data) {
			
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
			
		}
	);
}
