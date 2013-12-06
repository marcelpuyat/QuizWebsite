/**
* 
*/
function init_js (quiz_id) {
	query_db();

	function query_db () {
		loader.start();
		get_json_from_url(
			'/QuizWebsite/QuizInfoServlet?quiz_id='+quiz_id,
			function (data) {
				loader.stop();
				build_ui(data);
			}
		);
	}

	function build_ui (data) {
		console.log(data);
		set_name(data.quiz_name);
		set_description(data.description);
		set_creator(data.creator);

		if (data.is_editable) show_edit();
		if (data.is_practicable) show_practice();
		if (data.is_deletable) show_delete();

		append_results(data);

		//show div
		document.getElementById('QuizPage-wrapper').classList.remove('hide');
	}

	function set_name (name) {
		var name_elem = document.getElementById('quiz-name');
		name_elem.innerHTML = name;
	}

	function set_description (description) {
		var description_elem = document.getElementById('quiz-description');
		description_elem.innerHTML = description;
	}

	function set_creator (creator) {
		var creator_elem = document.getElementById('quiz-creator');
		creator_elem.innerHTML = creator;
		creator_elem.href = '/QuizWebsite/User.jsp?username='+creator;
	}

	function show_practice () {
		var practice_li = document.getElementById('practice-li');
		practice_li.classList.remove('hide');
	}

	function show_edit () {
		var edit_li = document.getElementById('edit-li');
		edit_li.classList.remove('hide');
	}

	function show_delete () {
		var delete_li = document.getElementById('delete-li');
		delete_li.classList.remove('hide');
		var clear_li = document.getElementById('clear-li');
		clear_li.classList.remove('hide');
	}

	function append_results (data) {
		/* user history */
		var user_history_ul = document.getElementById('user-history');
		var user_history_data = data.user_history || [];
		user_history_data.sort(sort_by_score);
		for (var i = 0; i < user_history_data.length; i++) {
			user_history_ul.appendChild(create_score_display(user_history_data[i],get_time_stamp(user_history_data[i])));
		};

		/* best all time */
		var best_alltime_ul = document.getElementById('best-alltime');
		var best_alltime_data = data.best_alltime || [];
		best_alltime_data.sort(sort_by_score);
		for (var i = 0; i < best_alltime_data.length; i++) {
			best_alltime_ul.appendChild(create_score_display(best_alltime_data[i],get_time_stamp(best_alltime_data[i])));
		};

		/* best today */
		var best_today_ul = document.getElementById('best-today');
		var best_today_data = data.best_today || [];
		best_today_data.sort(sort_by_score);
		for (var i = 0; i < best_today_data.length; i++) {
			best_today_ul.appendChild(create_score_display(best_today_data[i],get_time_stamp(best_today_data[i])));
		};

		/* recent scores */
		var recent_scores_ul = document.getElementById('recent-scores');
		var recent_scores_data = data.recent_scores || [];
		recent_scores_data.sort(sort_by_date);
		for (var i = 0; i < recent_scores_data.length; i++) {
			recent_scores_ul.appendChild(create_score_display(recent_scores_data[i],get_time_ago(recent_scores_data[i].date)));
		};

	}

	function create_score_display (data,extra_disp) {
		var li = document.createElement('li');
		var score = document.createElement('span');
		score.innerHTML = parseInt(100* data.score) + '% ';

		var username = document.createElement('a');
		username.innerHTML = data.name;
		username.href = '/QuizWebsite/User.jsp?username='+data.name;

		var time_disp = document.createElement('span');
		time_disp.innerHTML = ' ' + (extra_disp || '');
		li.appendChild(score);
		li.appendChild(username);
		li.appendChild(time_disp);
		return li;
	}

	function sort_by_date (a,b) {
		var a = a.date;
		var b = b.date;
		if (a.year    != b.year)    return b.year - a.year;
		if (a.month   != b.month)   return b.month - a.month;
		if (a.date    != b.date)    return b.date - a.date;
		if (a.hours   != b.hours)   return b.hours - a.hours;
		if (a.minutes != b.minutes) return b.minutes - a.minutes;
		if (a.seconds != b.seconds) return b.seconds - a.seconds;
		return sort_by_score(a,b);
	}

	function sort_by_score (a,b) {
		if (a.score != b.score) return b.score - a.score;
		return a.time - b.time;
	}

	function get_time_stamp (data) {
		return data.time + ' seconds';
	}

}

function delete_quiz (quiz_id) {
	var should_delete = window.confirm('Are you sure you want to delete this?');
	if (should_delete) {
		post_json_to_url(
			'/QuizWebsite/QuizServlet?quiz_id='+quiz_id+'&action=delete',
			{},
			function (data) {
				window.location = '/QuizWebsite/Home.jsp';
			}
		);
	}
}

function clear_quiz (quiz_id) {
	var should_clear = window.confirm('Are you sure you want to clear the history of this quiz?');
	if (should_clear) {
		post_json_to_url('/QuizWebsite/AdminServlet?action=clear_history&quiz_id=' + quiz_id,
				{},
				function () {
					
				}
		);
		alert("Quiz history cleared");
		location.reload(true);
	}
}
