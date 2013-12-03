/* Global divs */
var recent_results_div = document.getElementById("recent-results");
var created_quizzes_div = document.getElementById("created-quizzes");
var achievements_div = document.getElementById("achievements");
var user_info_div = document.getElementById("user-info");
var relation_controls_div = document.getElementById("relation-controls");
var message_button = document.getElementById("message-button");

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

function init_js(curr_user_id, target_user_id) {
	set_relations(curr_user_id, target_user_id);
	get_json_from_url(profileServletURL + '?user_id=' + target_user_id, function (data) {

		console.log(data);

		if (is_blocked) {
			message_button.className = message_button.className + " hide";
			new BlockedPageHandler(data);
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
function set_relations(curr_user_id, target_user_id) {

	/* Get info about viewer to viewee */
	get_json_from_url(relationServletURL + "?user_a_id=" + curr_user_id + "&user_b_id=" + target_user_id, function (data) {
		console.log(data);
		if (data.status == "REQ") has_requested = true;
		else if (data.status == "FRD") is_friend = true;
		else if (data.status == "BLK") has_blocked = true;
	});

	/* Get info about viewee to viewer */
	get_json_from_url(relationServletURL + "?user_a_id=" + target_user_id + "&user_b_id=" + curr_user_id, function (data) {
		console.log(data);
		if (data.status == "REQ") pending_request = true;
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
	user_info_div.innerHTML = data.user_info.username + " has blocked you.";
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

}
