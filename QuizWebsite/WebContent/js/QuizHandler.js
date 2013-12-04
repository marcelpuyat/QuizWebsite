/*
 *  QuizHandler(int quiz_id, str servlet_url):
 *     boolean isLoaded();
 *     boolean hasNext();
 *     element getNext();
 *     void    waitForLoad(fn(elem, aux) callback, aux);    //async, must load results from server
 *     void    waitForResults(fn(elem, aux) callback, aux); //  ^same
 *     void    informQuestionAnswered();
 *     
 */

function QuizHandler(quiz_id, load_url, post_url, is_practice, user_id) {
	/* init vars */
	var _user_id = user_id;
	var _this = this;
	var _questions = [];
	var _data;
	var _iterator;
	var _isLoaded = false;
	var _quiz_id = quiz_id;
	var _servlet_load_url = load_url;
	var _servlet_post_url = post_url;
	var _start_callbacks = [];
	var _next_callbacks  = [];
	var _start_callbacks_live = true;
	var _next_callbacks_live  = true;
	var _quiz_timer = new Timer(10);
	var _is_practice_quiz = is_practice;
	
	
	/* public methods */
	this.addEventListener = function (event, callback) {
		switch (event) {
			case 'start-quiz':
				if (_start_callbacks_live) _start_callbacks.push(callback);
				break;
			case 'next-question':
				if (_next_callbacks_live) _next_callbacks.push(callback);
				break;
		}
	};
	this.muteEventListener = function (event) {
		switch (event) {
			case 'start-quiz':
				_start_callbacks_live = false;
				break;
			case 'next-question':
				_next_callbacks_live = false;
				break;
		}
	};
	this.enableEventListener = function (event) {
		switch (event) {
			case 'start-quiz':
				_start_callbacks_live = true;
				break;
			case 'next-question':
				_next_callbacks_live = true;
				break;
		}
	};
	this.isLoaded = function() {
		return _isLoaded;
	};

	this.getQuizName = function () {
		return _data.quiz_name;
	};
	
	this.hasNext = function() {
		return (
				this.isLoaded() && (
					(_iterator == undefined && (_questions.length > 0)) ||
					((_iterator + 1) < _questions.length)
				)
			);
	};
	
	this.getNext = function() {
		if (this.hasNext()) {
			if (_iterator == undefined) {
				_iterator = 0;
			} else {
				_iterator++;
			}
			var next_elem = _questions[_iterator].getDOMSubStructure();
			return next_elem;
		}
		return null;
	};

	this.indexExists = function (index) {
		return (
				this.isLoaded() && (
					(index < _questions.length)
				)
			);
	};

	this.getAtIndex = function (index) {
		if (this.indexExists(index)) {
			return _questions[index].getDOMSubStructure();
		}
		return null;
	};

	this.getScoreAtIndex = function (index) {
		if (this.indexExists(index)) {
			return _questions[index].grade();
		}
		return -1;
	};

	this.sleepCard = function (index) {
		if (this.indexExists(index)) {
			_questions[index].killListeners();
		}
	};
	
	this.waitForLoad = function(callback, auxiliary_data) {
		_load_quiz_json(_servlet_load_url, _quiz_id, function(aux){
			var quiz_title = _data.quiz_name;
			if (quiz_title) document.title = quiz_title;


			var load_display_wrapper = document.createElement('div');
			load_display_wrapper.classList.add('fill-container','plus');

			var title = document.createElement('h1');
			title.innerHTML = quiz_title;
			title.classList.add('center');
			load_display_wrapper.appendChild(title);

			var play_now_button = document.createElement('div');
			play_now_button.innerHTML = "Play Now!";
			play_now_button.classList.add('pointable','center');
			play_now_button.id = "play-now-button";
			for (var i = 0; i < _start_callbacks.length; i++) {
				play_now_button.addEventListener('click',_start_callbacks[i]);
			};
			load_display_wrapper.appendChild(play_now_button);

			aux.client_callback(load_display_wrapper, aux.client_aux);
		}, {client_aux:auxiliary_data, client_callback:callback});
	};
	
	// GLOBALS
	
	var results_div;
	var score;

	this.waitForResults = function(callback, auxiliary_data) {
		/* build and send answer data */
		var answer = _build_results();
		if(!_is_practice_quiz) _send_quiz_results(answer);
		else _notify_quiz_practiced(_user_id);
		results_div = document.createElement('div');
		results_div.innerHTML = 'total correct: '+ answer.user_score+'\ntotal possible: '+answer.possible_score;
		
		// GLOBAL SCORE
		score = answer.user_score / answer.possible_score;
		
		//STYLE PLEASE
		var challenge_button = new_elem({
			type:'span',
			classList:['button'],
			innerHTML:'Challenge a friend'
		});
		challenge_button.onclick = _this.challenge_click;
		results_div.appendChild(challenge_button);
		
		callback(results_div, auxiliary_data);
	};
	
	this.challenge_click = function() {
		console.log(_user_id);
		get_json_from_url("/QuizWebsite/RelationServlet?user_id=" + _user_id + "&action=friends", function (data) {
			console.log(data);
			_this.display_friends(data.friends);
		});
	};
		
	this.display_friends = function (friends) {
		var friend_ul = document.createElement('ul');
		for (var i = 0; i < friends.length; i++) {
			var friend = document.createElement('li');
			var friend_button = document.createElement('button');
			var name = friends[i].display_name;
			var friend_id = friends[i].id;
			
			friend_button.innerHTML = name;
			friend_button.friend_id = friend_id;
			friend_button.addEventListener("click", function() {

					_this.challenge_friend(this.friend_id);
				
			});
			
			friend.appendChild(friend_button);
			friend_ul.appendChild(friend);
		}
		results_div.appendChild(friend_ul);
	};
	
	this.challenge_friend = function(friend_id) {
		var div = document.createElement('div');
		div.innerHTML = "Challenge sent";
		results_div.appendChild(div);
		console.log(friend_id);
		var challenge_message = "I challenge you to beat my score of " + (score * 100).toFixed(0) + "% on this quiz: <a href='/QuizWebsite/QuizPage.jsp?quiz_id=" + _quiz_id + "'>" + _data.quiz_name + "</a>";
		post_json_to_url("/QuizWebsite/MessageServlet?action=send", {user_from_id: _user_id, user_to_id: friend_id, body: challenge_message, subject: ""}, function() {});
		
	};
		
	this.isMultiPage = function () {
		return _data.is_multiple_page;
	};
	this.informStart = function () {
		_quiz_timer.start();
	};
	
	
	
	/* private methods */
	function _load_quiz_json(servlet_url, quiz_id, callback, aux) {
		console.log('loading ' + quiz_id);
		get_json_from_url(servlet_url+"?quiz_id="+quiz_id, function (data, aux) {
			console.log('::loaded quiz::');
			console.log(data);
			_data = data;
			var questions_json = data.questions;
			for (var i = 0; i < questions_json.length; i++) {
				var question_obj = questions_json[i];
				_questions.push(getQuestionHandler(question_obj.type, question_obj.data, i));
			}
			if (_data.is_randomized) _questions.shuffle();
			_isLoaded = true;
			aux.client_callback(aux.client_aux);
		}, {quiz_id:quiz_id, client_aux:aux, client_callback:callback});
	}
	function _build_results() {
		var score_data = {quiz_id:_quiz_id,time:get_elapsed_time(),user_score:0,possible_score:0,percentage:0};
		for (var i = 0; i < _questions.length; i++) {
			var score_for_q = _questions[i].grade(); //{score:int,possible:int}
			score_data.user_score += score_for_q.score;
			score_data.possible_score += score_for_q.possible;
		}
		score_data.percentage = score_data.user_score / score_data.possible_score;
		return  score_data;
	}

	function get_elapsed_time () {
		return _quiz_timer.getSecondsElapsed();
	}
	
	function _notify_quiz_practiced (user_id) {
		post_json_to_url(
			'/QuizWebsite/QuizResults?user_id='+user_id+'&practice=true',
			{}
		);
	}

	function _send_quiz_results(data, callback, aux) {
		console.log('sending data to:'+_servlet_post_url);
		console.log(data);
		post_json_to_url(_servlet_post_url, data, callback, aux);
	}
}