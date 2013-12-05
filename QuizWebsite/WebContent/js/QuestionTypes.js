/*
 * MUST IMPLEMENT!!: __constructor__(data, q_id) - id must be unique to ensure unique id tags in generated elems
 * 	   				 getType() - returns type
 *                   getDOMSubStructure(bool addButton) - returns displayed DOM structure
 *					 grade() - returns user score in form {score:int, possible:int};
 *                   killListeners() - removes appropriate action listeners from object, should disable the users ability to change answers
 *                   displayCorrected() - displays the corrected version of the card
 */
function getQuestionHandler(type, data, q_id) {
	switch (type) {
	case ('multiple-choice'):
		return new MultipleChoiceHandler(data, q_id);
	case ('multiple-answer'):
		return new MultipleAnswerHandler(data, q_id);
	case ('picture-response'):
		return new PictureResponseHandler(data, q_id);
	case ('matching'):
		return new MatchingHandler(data, q_id);
	case ('fill-blank'):
		return new FillInBlankHandler(data, q_id);
	case ('single-answer'):
		return new SingleAnswerHandler(data, q_id);
	}
};



// function QuestionHandler (data, q_id) {
// 	this.getType = function () {};
// 	this.getDOMSubStructure = function () {};
// 	this.grade = function () {return {score:0,possible:_data.score};};
// }

function MatchingHandler (data, q_id) {
	var _data = data;
	var _this = this;
	var _question_id = q_id;
	var _drag = {target:undefined,captureX:0,captureY:0,reciever:undefined};
	var _listeners = [];
	_capture_lis = [];
	this.getType = function () { return 'matching'; };
	this.getDOMSubStructure = function () {
		var wrapper = document.createElement('div');
		
		var prompt = _data.prompt;
		var prompt_div = document.createElement('h2');
		prompt_div.classList.add('prompt');
		prompt_div.innerHTML = _data.prompt;
		wrapper.appendChild(prompt_div);
		
		var options = _data.correct; // MODIFIED by Marcel (answers -> correct)
		var options_ul = document.createElement('ul');
		options_ul.classList.add('options','cluster','no-highlight');

		var capture_ul = document.createElement('ul');
		capture_ul.classList.add('options','no-highlight');


		var option_lis = [];
		for (var i = 0; i < options.length; i++) {
			_capture_lis.push(document.createElement('li'));
			var new_capture_label = document.createElement('span');
			new_capture_label.innerHTML = options[i][0];
			new_capture_label.classList.add('match','option-label','pointable');
			_capture_lis[i].appendChild(new_capture_label);

			var captured_item_label = document.createElement('span');
			captured_item_label.classList.add('match','option-label','right','pointable');
			_capture_lis[i].appendChild(captured_item_label);
			_capture_lis[i].captured_item_label = captured_item_label;
			_capture_lis[i].sorting_index = i;
			_capture_lis[i].classList.add('match','selection','pointable');

			option_lis.push(document.createElement('li'));
			var new_option_label = document.createElement('span');
			new_option_label.innerHTML = options[i][1];
			new_option_label.classList.add('match','option-label','pointable');
			option_lis[i].appendChild(new_option_label);
			option_lis[i].classList.add('pointable','match','selection','movable');
			option_lis[i].isCaptive = false;
			option_lis[i].label_text = options[i][1];
			var click_listener = function (e) {
				if (this.captive) {
					release_captive(this);
				}
			};
			var did_drag = function (e) {
				deselect_all(['selected','false']);
				if (_drag.target) {
					get_capture_elem(e,['selected']);
					_drag.target.style.left = (e.clientX - _drag.captureX) + 'px';
					_drag.target.style.top = (e.clientY - _drag.captureY) + 'px';
				} else {
					var capture = get_capture_elem(e,[]);
					if (capture && capture.captive) {
						capture.classList.add('false');
					}
				}
			};
			var begin_drag = function (e) {
				_drag.target = this;
				_drag.captureX = e.clientX;
				_drag.captureY = e.clientY;
				_drag.target.classList.add('moving');
			};
			var end_drag = function (e) {
				deselect_all(['selected']);
				if (_drag.target) {
					var capture = get_capture_elem(e,['selected']);
					if (capture) {
						if (capture.captive) {
							release_captive(capture);
						}
						capture.captive = _drag.target;
						_drag.target.classList.add('hide');
						_drag.target.isCaptive = true;
						capture.captured_item_label.innerHTML = _drag.target.label_text;
					} else {
						_drag.target.classList.remove('moving');
					}
					_drag.target = undefined;
					_drag.reciever = undefined;
				}
			};
			var release_captive = function (capture) {
				capture.captured_item_label.innerHTML = '';
				capture.captive.classList.remove('moving','hide');
				capture.captive.isCaptive = false;
				capture.captive = undefined;
			};
			var deselect_all = function (classes) {
				for (var i = 0; i < _capture_lis.length; i++) {
					for (var c = 0; c < classes.length; c++) _capture_lis[i].classList.remove(classes[c]);
				};
			};
			var get_capture_elem = function (e, toggle_classes) {
				var cap_li;
				for (var i = 0; i < _capture_lis.length; i++) {
					var bounding_rect = _capture_lis[i].getBoundingClientRect();
					if (e.clientX > bounding_rect.left && e.clientX < bounding_rect.left + bounding_rect.width &&
						e.clientY > bounding_rect.top && e.clientY < bounding_rect.top + bounding_rect.height) {
						for (var c = 0; c < toggle_classes.length; c++) _capture_lis[i].classList.add(toggle_classes[c]);
						cap_li = _capture_lis[i];
					} else {
						for (var c = 0; c < toggle_classes.length; c++) _capture_lis[i].classList.remove(toggle_classes[c]);
					}
				};
				return cap_li;
			};
			_capture_lis[i].addEventListener('click', click_listener);//bubble
			_listeners.push({elem:_capture_lis[i],action:'click',callback:click_listener});

			window.addEventListener('mousemove',did_drag, true);
			_listeners.push({elem:window,action:'mousemove',callback:did_drag});

			option_lis[i].addEventListener('mousedown',begin_drag);
			_listeners.push({elem:option_lis[i],action:'mousedown',callback:begin_drag});

			window.addEventListener('mouseup',end_drag);
			_listeners.push({elem:window,action:'mouseup',callback:end_drag});
		};
		option_lis.shuffle();
		for (var i = 0; i < option_lis.length; i++) {
			options_ul.appendChild(option_lis[i]);
		};
		wrapper.appendChild(options_ul);

		_capture_lis.shuffle();
		for (var i = 0; i < _capture_lis.length; i++) {
			capture_ul.appendChild(_capture_lis[i]);
		};
		wrapper.appendChild(capture_ul);
		return wrapper;
	};
	this.killListeners = function () {
		for (var i = 0; i < _listeners.length; i++) {
			var lis_obj = _listeners[i];
			lis_obj.elem.removeEventListener(lis_obj.action,lis_obj.callback,false);
		};
	};
	this.displayAnswer = function () {
		window.alert('answer!!');
	};
	this.grade = function () {
		var score = {score:0,possible:_data.score};
		var per_match_score = _data.score / _capture_lis.length;
		var perfect_score = true;
		_capture_lis.sort(function (a,b) {
			return a.sorting_index - b.sorting_index;
		});
		for (var i = 0; i < _capture_lis.length; i++) {
			if (_capture_lis[i].captive && _capture_lis[i].captive.label_text == _data.correct[i][1]) score.score += per_match_score;
			else perfect_score = false;												// MODIFIED by Marcel (answers -> correct)
		};
		if (!_data.partial_credit) score.score = perfect_score ? score.possible:0;
		return score;
	};
}

function MultipleAnswerHandler(data, q_id) {
	var _data = data;
	var _selection;
	var _this = this;
	var _question_id = q_id;
	var _lis = [];
	var _listeners = [];
	this.getType = function () {return 'multiple-answer';};
	/* returns a DOM element which will be inserted into the quiz's 'quiz-content' div */
	this.getDOMSubStructure = function () {
		var wrapper = document.createElement('div');
		
		var prompt = _data.prompt;
		var prompt_div = document.createElement('h2');
		prompt_div.classList.add('prompt');
		prompt_div.innerHTML = _data.prompt;
		wrapper.appendChild(prompt_div);
		
		var options = _data.correct; // MODIFIED by Marcel (answers -> correct)
		var options_ul = document.createElement('ul');
		options_ul.classList.add('options');
		for (var i = 0; i < options.length; i++) {
			_lis.push(document.createElement('li'));
			var new_option_label = document.createElement('span');
			new_option_label.innerHTML = options[i][0];
			new_option_label.classList.add('mult-ans','option-label');
			_lis[i].appendChild(new_option_label);
			_lis[i].truth_eval = false;
			_lis[i].sorting_index = i;
			_lis[i].classList.add('pointable','mult-ans','selection');
			var click_listener = function (e) {
				this.truth_eval = !this.truth_eval;
				this.classList.remove('true','false');
				this.classList.add(this.truth_eval ? 'true' : 'false');
			};
			_lis[i].addEventListener('click', click_listener, false);//bubble
			_listeners.push({elem:_lis[i],action:'click',callback:click_listener});
		};
		_lis.shuffle();
		for (var i = 0; i < _lis.length; i++) {
			_lis[i].classList.add(this.truth_eval ? 'true' : 'false');
			options_ul.appendChild(_lis[i]);
		};
		wrapper.appendChild(options_ul);
		return wrapper;
	};
	
	this.killListeners = function () {
			for (var i = 0; i < _listeners.length; i++) {
				var lis_obj = _listeners[i];
				lis_obj.elem.removeEventListener(lis_obj.action,lis_obj.callback,false);
			};
		};
	this.displayAnswer = function () {
		window.alert('answer!!');
	};
	this.grade = function () {
		var score = {score:0,possible:_data.score};
		var per_match_score = _data.score / _lis.length;
		var perfect_score = true;
		_lis.sort(function (a,b) {
			return a.sorting_index - b.sorting_index;
		});
		for (var i = 0; i < _lis.length; i++) {
			if (_lis[i].truth_eval == _data.correct[i][1]) score.score += per_match_score;
			else perfect_score = false;		// MODIFIED by Marcel (answers -> correct)
		};
		if (!_data.partial_credit) score.score = perfect_score ? score.possible:0;
		return score;
	};

	function get_checked() {
		var check_boxes = _user_input_elems;
		for (var i = 0; i < check_boxes.length; i++) {
			if (check_boxes[i].checked) return check_boxes[i];
		}
	}
}

function FillInBlankHandler (data, q_id) {
	var _data = data;
	var _answer;
	var _user_input_elem;
	var _question_id = q_id;
	var _listeners = [];
	this.getType = function () {
		return 'single-answer';
	};
	this.getDOMSubStructure = function () {
		var wrapper = document.createElement('div');
		var prompt_div = document.createElement('h2');
		prompt_div.classList.add('prompt');
		prompt_div.innerHTML = _data.optional_prompt;
		wrapper.appendChild(prompt_div);
		
		var pre_prompt  = document.createElement('p');
		var post_prompt = document.createElement('p');
		pre_prompt.innerHTML  = _data.first_prompt;
		post_prompt.innerHTML = _data.second_prompt;

		_user_input_elem = document.createElement('input');
		_user_input_elem.id = 'single-c-input-'+_question_id;
		_user_input_elem.type = 'text';
		_user_input_elem.attach_enter_listener(function() {
			ui_handler.next();
		});
		var resize_text = function(e){
			_user_input_elem.style.width = max(5,_user_input_elem.value.length/1.7) + 'em';
		};
		_user_input_elem.addEventListener('keydown', resize_text);
		_listeners.push({elem:_user_input_elem,action:'keydown',callback:resize_text});
		_user_input_elem.style.textAlign = 'center';
		_user_input_elem.style.width = '5em';
		_user_input_elem.style.maxWidth = '60%';

		var fill_in_wrapper = document.createElement('div');
		fill_in_wrapper.classList.add('text-center');

		pre_prompt.classList.add('inline');
		_user_input_elem.classList.add('inline');
		post_prompt.classList.add('inline');

		fill_in_wrapper.appendChild(pre_prompt);
		fill_in_wrapper.appendChild(_user_input_elem);
		fill_in_wrapper.appendChild(post_prompt);

		wrapper.appendChild(fill_in_wrapper);
		return wrapper;
	};
	

	this.killListeners = function () {
			for (var i = 0; i < _listeners.length; i++) {
				var lis_obj = _listeners[i];
				lis_obj.elem.removeEventListener(lis_obj.action,lis_obj.callback,false);
			};
		};
	this.displayAnswer = function () {
		window.alert('answer!!');
	};
	this.grade = function () {
		var score = {score:0,possible:_data.score};
		var user_answer = _user_input_elem.value;
		if (_data.correct.indexOf(user_answer) != -1) { // MODIFIED by Marcel (answers -> correct)
			score.score = _data.score;
		} else {
			score.score = 0;
		}
		return score;
	};
}

function MultipleChoiceHandler(data, q_id) {
	var _data = data;
	var _selection;
	var _this = this;
	var _question_id = q_id;
	var _last_clicked;
	var _listeners = [];
	this.getType = function () {
		return 'multiple-choice';
	};
	/* returns a DOM element which will be inserted into the quiz's 'quiz-content' div */
	this.getDOMSubStructure = function () {
		var wrapper = document.createElement('div');
		
		var prompt = _data.prompt;
		var prompt_div = document.createElement('h2');
		prompt_div.classList.add('prompt');
		prompt_div.innerHTML = _data.prompt;
		wrapper.appendChild(prompt_div);
		
		var options = _data.options;
		var options_ul = document.createElement('ul');
		options_ul.classList.add('mult-c','options');
		var lis = [];
		for (var i = 0; i < options.length; i++) {
			lis.push(document.createElement('li'));
			var new_option_label = document.createElement('span');
			new_option_label.innerHTML = options[i];
			new_option_label.classList.add('mult-c','option-label');
			lis[i].appendChild(new_option_label);
			lis[i].index_selected = i;
			lis[i].prompt_name = options[i];
			lis[i].classList.add('pointable','mult-c','selection');
			var click_listener = function (e) {
				for (var i = 0; i < lis.length; i++) {
					lis[i].classList.remove('selected');
				}
				_last_clicked = this.index_selected;
				this.classList.add('selected');
			};
			lis[i].addEventListener('click', click_listener, false);//bubble
			_listeners.push({elem:lis[i],action:'click',callback:click_listener});
		};
		lis.shuffle();
		for (var i = 0; i < lis.length; i++) {
			if (i == 0) {
				lis[i].classList.add('selected');
				_last_clicked = lis[i].index_selected;
			}
			options_ul.appendChild(lis[i]);
		};
		wrapper.appendChild(options_ul);
		return wrapper;
	};
	
	this.killListeners = function () {
		for (var i = 0; i < _listeners.length; i++) {
			var lis_obj = _listeners[i];
			lis_obj.elem.removeEventListener(lis_obj.action,lis_obj.callback,false);
		};
	};
	this.displayAnswer = function () {
		window.alert('answer!!');
	};
	this.grade = function () {
		var score = {score:0,possible:_data.score};
		if (_last_clicked == _data.correct) score.score = _data.score;
		return score;
	};

	function get_checked() {
		var check_boxes = _user_input_elems;
		for (var i = 0; i < check_boxes.length; i++) {
			if (check_boxes[i].checked) return check_boxes[i];
		}
	}
}

function PictureResponseHandler(data, q_id) {
	var _data = data;
	var _answer;
	var _user_input_elem;
	var _question_id = q_id;
	var _listeners = [];
	this.getType = function() {
		return "picture-response";
	};
	this.getDOMSubStructure = function () {
		var wrapper = document.createElement('div');
		var img_disp = document.createElement('img');
		img_disp.src = _data.img_url;
		img_disp.classList.add('picutre-response-img',
							'center-block',
							(_data.prompt == undefined || _data.prompt == '')? 'large':'small');
		wrapper.appendChild(img_disp);
		var prompt_div = document.createElement('h2');
		prompt_div.classList.add('prompt');
		prompt_div.innerHTML = _data.prompt;
		wrapper.appendChild(prompt_div);
		
		_user_input_elem = document.createElement('input');
		_user_input_elem.id = 'pict-resp-input-'+_question_id;
		_user_input_elem.type = 'text';
		_user_input_elem.classList.add('center-block');
		_user_input_elem.attach_enter_listener(function() {
			ui_handler.next();
		});
		wrapper.appendChild(_user_input_elem);
		return wrapper;
	};
	
	this.killListeners = function () {
		for (var i = 0; i < _listeners.length; i++) {
			var lis_obj = _listeners[i];
			lis_obj.elem.removeEventListener(lis_obj.action,lis_obj.callback,false);
		};
	};
	this.displayAnswer = function () {
		window.alert('answer!!');
	};
	this.grade = function () {
		var score = {score:0,possible:_data.score};
		var user_answer = _user_input_elem.value;
		if (_data.correct.indexOf(user_answer) != -1) {
			score.score = _data.score;
		} else {
			score.score = 0;
		}
		return score;
	};
}



function SingleAnswerHandler(data, q_id) {
	var _data = data;
	var _answer;
	var _user_input_elem;
	var _question_id = q_id;
	var _listeners = [];
	this.getType = function () {
		return 'single-answer';
	};
	this.getDOMSubStructure = function () {
		var wrapper = document.createElement('div');
		var prompt_div = document.createElement('h2');
		prompt_div.classList.add('prompt');
		prompt_div.innerHTML = _data.prompt;
		wrapper.appendChild(prompt_div);
		
		_user_input_elem = document.createElement('input');
		_user_input_elem.id = 'single-c-input-'+_question_id;
		_user_input_elem.type = 'text';
		_user_input_elem.classList.add('center-block');
		_user_input_elem.attach_enter_listener(function() {
			ui_handler.next();
		});
		wrapper.appendChild(_user_input_elem);
		return wrapper;
	};
	

	this.killListeners = function () {
		for (var i = 0; i < _listeners.length; i++) {
			var lis_obj = _listeners[i];
			lis_obj.elem.removeEventListener(lis_obj.action,lis_obj.callback,false);
		};
	};
	this.displayAnswer = function () {
		window.alert('answer!!');
	};
	this.grade = function () {
		var score = {score:0,possible:_data.score};
		var user_answer = _user_input_elem.value;
		if (_data.correct.indexOf(user_answer) != -1) {
			score.score = _data.score;
		} else {
			score.score = 0;
		}
		return score;
	};
}


