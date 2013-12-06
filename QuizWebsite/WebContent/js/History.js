/**
 * 
 */

var _user_id;
var historyServletURL = "/QuizWebsite/HistoryServlet";
var _results;

/* Store these lists for faster reference */
var _already_sorted_by_date;
var _already_sorted_by_name;
var _already_sorted_by_score;
var _already_sorted_by_time;
var _already_sorted_by_rank;

var sorted_by_date = true;
var sorted_by_score = false
var sorted_by_time = false;
var sorted_by_name = false;
var sorted_by_rank = false;
var reverse_date = false;
var reverse_score = false;
var reverse_time = false;
var reverse_name = false;
var reverse_rank = false;


var headers = {
	'quiz':document.getElementById('quiz-header'),
	'score':document.getElementById('score-header'),
	'time':document.getElementById('time-header'),
	'date':document.getElementById('date-header'),
	'rank':document.getElementById('rank-header')
}

var _results_table = document.getElementById("history-table");

function init_js(user_id) {
	_user_id = user_id;
	update_results_history();
}

function update_results_history() {	
	loader.start();
	get_json_from_url(historyServletURL + "?user_id=" + _user_id, function (data) {
		loader.stop();
		var results_array = data.history;
		
		_results = results_array;
		
		/* Sorted by date by default */
		fill_table(_results);
		
		_already_sorted_by_date = _results.slice(0); // SAVE THE SORTED BY DATE ARRAY
		
		set_click_events();
	});
	
}

function set_click_events() {
	
	document.getElementById("quiz-header").addEventListener("click", function() {sortBy("quiz")});
	document.getElementById("score-header").addEventListener("click", function() {sortBy("score")});
	document.getElementById("time-header").addEventListener("click", function() {sortBy("time")});
	document.getElementById("date-header").addEventListener("click", function() {sortBy("date")});
	document.getElementById("rank-header").addEventListener("click", function() {sortBy("rank")});

}

function style_headers (category) {
	var reversed =  (category.match('reverse-') !== null);
	var category = category.replace('reverse-','');

	for (var i in headers){
		if (i == category) {
			headers[i].classList.add('sort-by');
			if (reversed) {
				headers[i].classList.add('down');
				headers[i].classList.remove('up');
			} else {
				headers[i].classList.remove('down');
				headers[i].classList.add('up');
			}
		} else {
			headers[i].classList.remove('sort-by');
		}
	};
}

function sortBy(category) {
	
	cleanTable();
	
	if (category == "quiz") {

		/* Check if has already been sorted before */
		if (!_already_sorted_by_name) {
			_results.sort(function(result_one, result_two) {
				if (result_one.quiz_name.toUpperCase() < result_two.quiz_name.toUpperCase()) return -1;
				if (result_one.quiz_name.toUpperCase() > result_two.quiz_name.toUpperCase()) return 1;
				else return 0;
			})
			
			_already_sorted_by_name = _results.slice(0); // SAVE COPY
		}
		
		/* If currently sorted by this already, reverse. */
		if (sorted_by_name) {
			_already_sorted_by_name.reverse();
			setSortedBy("reverse-quiz");
		}
		else if (reverse_name) {
			_already_sorted_by_name.reverse();
			setSortedBy("quiz");
		}
		else setSortedBy("quiz");
		fill_table(_already_sorted_by_name);
	}
	
	if (category == "rank") {
		/* Check if has already been sorted before */
		if (!_already_sorted_by_rank) {
			_results.sort(function(result_one, result_two) {
				if (result_one.rank < result_two.rank) return -1;
				if (result_one.rank > result_two.rank) return 1;
				else return 0;
			})
			
			_already_sorted_by_rank = _results.slice(0); // SAVE COPY
		}
		
		/* If currently sorted by this already, reverse. */
		if (sorted_by_rank) {
			_already_sorted_by_rank.reverse();
			setSortedBy("reverse-rank");
		}
		else if (reverse_rank) {
			_already_sorted_by_rank.reverse();
			setSortedBy("rank");
		}
		else setSortedBy("rank");
		fill_table(_already_sorted_by_rank);
	}
	
	else if (category == "date") {
		if (sorted_by_date) {
			_already_sorted_by_date.reverse();
			setSortedBy("reverse-date");
		}
		else if (reverse_date) {
			_already_sorted_by_date.reverse();
			setSortedBy("date");
		}
		else setSortedBy("date");
		fill_table(_already_sorted_by_date); // USE SAVED ORDER (WILL ALWAYS BE INITIALIZED BEC THIS WAS DEFAULT)
	}
	
	else if (category == "score") {
		if (!_already_sorted_by_score) {
			_results.sort(function(result_one, result_two) {
				if (result_one.user_percentage_score > result_two.user_percentage_score) return -1;
				if (result_one.user_percentage_score < result_two.user_percentage_score) return 1;
				else {
					if (result_one.time_taken < result_two.time_taken) return -1;
					if (result_one.time_taken > result_two.time_taken) return 1;
				}
				return 0;
			})
			
			_already_sorted_by_score = _results.slice(0);
		}
		
		if (sorted_by_score) {
			_already_sorted_by_score.reverse();
			setSortedBy("reverse-score");
		}
		else if (reverse_score) {
			_already_sorted_by_score.reverse();
			setSortedBy("score");
		}
		else setSortedBy("score");
		fill_table(_already_sorted_by_score);
	}
	
	else if (category == "time") {
		if (!_already_sorted_by_time) {
			_results.sort(function(result_one, result_two) {
				if (result_one.time_taken < result_two.time_taken) return -1;
				if (result_one.time_taken > result_two.time_taken) return 1;
				else {
					if (result_one.user_percentage_score > result_two.user_percentage_score) return -1;
					if (result_one.user_percentage_score < result_two.user_percentage_score) return 1;
				}
				return 0;
			})
			
			_already_sorted_by_time = _results.slice(0);
		}

		if (sorted_by_time) {
			_already_sorted_by_time.reverse();
			setSortedBy("reverse-time");
		}
		else if (reverse_time) {
			_already_sorted_by_time.reverse();
			setSortedBy("time");
		}
		else setSortedBy("time");
		fill_table(_already_sorted_by_time);	
	}
}

function cleanTable() {
	for (var i = _results_table.rows.length - 1; i > 0; i--) {
		_results_table.deleteRow(i);
	}
}

function setSortedBy(category) {
	style_headers(category);
	if (category == "quiz") {
		setReverseFalse();
		sorted_by_date = false;
		sorted_by_score = false;
		sorted_by_time = false;
		sorted_by_name = true;
		sorted_by_rank = false;
	}
	else if (category == "reverse-quiz") {
		setInOrderFalse();
		reverse_date = false;
		reverse_time = false;
		reverse_name = true;
		reverse_score = false;
		reverse_rank = false;
	}
	else if (category == "score") {
		setReverseFalse();
		sorted_by_date = false;
		sorted_by_score = true;
		sorted_by_time = false;
		sorted_by_name = false;
		sorted_by_rank = false;
	}
	else if (category == "reverse-score") {
		setInOrderFalse();
		reverse_date = false;
		reverse_time = false;
		reverse_name = false;
		reverse_score = true;
		reverse_rank = false;
	}
	else if (category == "time") {
		setReverseFalse();
		sorted_by_date = false;
		sorted_by_score = false;
		sorted_by_time = true;
		sorted_by_name = false;
		sorted_by_rank = false;
	}
	else if (category == "reverse-time") {
		setInOrderFalse();
		reverse_date = false;
		reverse_time = true;
		reverse_name = false;
		reverse_score = false;
		reverse_rank = false;
	}
	else if(category == "date") {
		setReverseFalse();
		sorted_by_date = true;
		sorted_by_score = false;
		sorted_by_time = false;
		sorted_by_name = false;
		sorted_by_rank = false;
	}
	else if (category == "reverse-date") {
		setInOrderFalse();
		reverse_date = true;
		reverse_time = false;
		reverse_name = false;
		reverse_score = false;
		reverse_rank = false;
	}
	else if (category == "rank") {
		setReverseFalse();
		sorted_by_date = false;
		sorted_by_score = false;
		sorted_by_time = false;
		sorted_by_name = false;
		sorted_by_rank = true;
	}
	else if (category == "reverse-rank") {
		setInOrderFalse();
		reverse_date = false;
		reverse_time = false;
		reverse_name = false;
		reverse_score = false;
		reverse_rank = true;
	}
	
}

function setInOrderFalse() {
	sorted_by_date = false;
	sorted_by_score = false;
	sorted_by_time = false;
	sorted_by_name = false;
	sorted_by_rank = false;
}
function setReverseFalse() {
	reverse_date = false;
	reverse_score = false;
	reverse_time = false;
	reverse_name = false;
	reverse_rank = false;
}

function fill_table(results_array) {
	
	for (var i = 0; i < results_array.length; i++) {
		var result = results_array[i];
		
		var quiz_id = result.quiz_id;
		var quiz_name = result.quiz_name;		
		var user_percentage_score = result.user_percentage_score;
		var time_taken = result.time_taken;
		var rank = result.rank;
		var total = result.total;
		
		var year = result.date.year;
		var month = result.date.month + 1; // WATCH FOR THIS!
		var date = result.date.date;
		var minutes = result.date.minutes;
		var seconds = result.date.seconds;
		
		
		var row = document.createElement('tr');
		
		/* Set Quiz Column */
		var quiz_col = document.createElement('td');
		
		var quiz_link = document.createElement('a');
		quiz_link.classList.add('quiz-link');
		quiz_link.href = "/QuizWebsite/QuizPage.jsp?quiz_id=" + quiz_id;
		quiz_link.innerHTML = quiz_name;
		
		quiz_col.appendChild(quiz_link);
		
		/* Set Score Column */
		var score_col = document.createElement('td');
		
		score_col.innerHTML = (user_percentage_score*100).toFixed(1) + "%";
		
		/*Set Time Column */
		var time_col = document.createElement('td');
		
		time_col.innerHTML = time_taken.toFixed(1) + "s";
		
		/*Set Date Column (PRETTY THIS UP) */
		var date_col = document.createElement('td');
		
		date_col.innerHTML = year + "/" + month + "/" + date; // PRETTY UP
		
		/*Set rank column */
		var rank_col = document.createElement('td');
		
		rank_col.innerHTML = rank + " out of " + total;
		
		row.appendChild(quiz_col);
		row.appendChild(score_col);
		row.appendChild(time_col);
		row.appendChild(rank_col);
		row.appendChild(date_col);
		
		_results_table.appendChild(row);
	}
}