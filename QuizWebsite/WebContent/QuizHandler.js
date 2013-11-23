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

function QuizHandler(quiz_id, servlet_url) {
	/* init vars */
	var _questions = [];
	var _data;
	var _iterator;
	var _isLoaded = false;
	var _quiz_id = quiz_id;
	var _servlet_url = servlet_url;
	var _start_callbacks = [];
	var _next_callbacks  = [];
	var _start_callbacks_live = true;
	var _next_callbacks_live  = true;
	var _quiz_timer = new Timer(10);
	var _is_practice_quiz = false;
	
	
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
			console.log('next:: '+_iterator);
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
	}

	this.getAtIndex = function (index) {
		if (this.indexExists(index)) {
			return _questions[index].getDOMSubStructure();
		}
		return null;
	}

	this.getScoreAtIndex = function (index) {
		if (this.indexExists(index)) {
			return _questions[index].grade();
		}
		return -1;
	}

	this.sleepCard = function (index) {
		if (this.indexExists(index)) {
			_questions[index].killListeners();
		}
	}
	
	this.waitForLoad = function(callback, auxiliary_data) {
		_load_quiz_json(_servlet_url, _quiz_id, function(aux){
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
	
	this.waitForResults = function(callback, auxiliary_data) {
		/* build and send answer data */
		var answer = _build_results();
		if(!_is_practice_quiz) _send_quiz_results(answer);
		var elem = document.createElement('div');
		elem.innerHTML = 'total correct: '+ answer.user_score+'\ntotal possible: '+answer.possible_score;
		callback(elem, auxiliary_data);
	};
	
	this.isMultiPage = function () {
		return _data.is_multiple_page;
	}
	this.informStart = function () {
		_quiz_timer.start();
	}
	
	
	
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
	
	function _send_quiz_results(data, callback, aux) {
		console.log('sending data:');
		console.log(data);
		post_json_to_url(_servlet_url, data, callback, aux);
	}
}