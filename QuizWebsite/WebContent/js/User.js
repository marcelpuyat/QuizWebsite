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
var relationServletURL = "/QuizWebsite/RelationServlet"

/* Relation details */
var is_friend = false;
var is_blocked = false;
var has_blocked = false;
var pending_request = false;
var has_requested = false;

/* viewer id and viewee id */
var _curr_user_id;
var _target_user_id;

function init_js(curr_user_id, target_user_id) {
	_curr_user_id = curr_user_id;
	_target_user_id = target_user_id;

	set_relations();
	get_json_from_url(profileServletURL + '?user_id=' + target_user_id, function (data) {

		console.log(data);

		if (is_blocked) {
			message_button.className = message_button.className + " hide";
			BlockedPageHandler(data.user_info);
		}

		/* If not blocked, display basic user details */
		else {
			display_user_info(data.user_info);
			display_relation_controls(data.user_info);

			if (is_friend) {
				new FriendPageHandler(data);
			}

			else {
				new AnonymousPageHandler(data, is_blocked, has_blocked, pending_request);
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

function FriendPageHandler(user_id) {
	(function init() {

	})();
}

function AnonymousPageHandler(user_id, is_blocked, has_blocked, pending_request) {
	(function init() {

	})();
}

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

	if (is_friend) {
		hideButton(accept_button);
		hideButton(reject_button);
		hideButton(unblock_button);
		hideButton(request_button);
		status_div.innerHTML = "is your friend";
	}
	else if (has_blocked) {
		hideButton(accept_button);
		hideButton(reject_button);
		hideButton(request_button);
		hideButton(block_button);
		hideButton(delete_friend_button);
		status_div.innerHTML = "is blocked";
	} else if (has_requested) {
		hideButton(request_button);
		hideButton(unblock_button);
		hideButton(delete_friend_button);
		status_div.innerHTML = "has sent you a friend request";
	} else if (pending_request) {
		hideButton(request_button);
		hideButton(accept_button);
		hideButton(reject_button);
		hideButton(delete_friend_button);
		hideButton(unblock_button);
		status_div.innerHTML = "friendship pending";
	} else {
		hideButton(accept_button);
		hideButton(reject_button);
		hideButton(unblock_button);
		hideButton(delete_friend_button);
	}
}

function hideButton(button) {
	button.className = button.className + " hide";
}

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
