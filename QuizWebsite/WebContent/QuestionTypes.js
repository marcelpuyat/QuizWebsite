/*
 * MUST IMPLEMENT!!: __constructor__(data, q_id) - id must be unique to ensure unique id tags in generated elems
 * 	   				 getType() - returns type
 *                   getDOMSubStructure(bool addButton) - returns displayed DOM structure
 *					 grade() - returns user score in form {score:int, possible:int};
 */
function getQuestionHandler(type, data, q_id) {
	switch (type) {
	case ('multiple-choice'):
		return new MultipleChoiceHandler(data, q_id);
	case ('multiple-answer'):
		console.log(type+' NOT IMPLEMENTED');
		return;
	case ('picture-response'):
		return new PictureResponseHandler(data, q_id);
	case ('matching'):
		console.log(type+' NOT IMPLEMENTED');
		return;
	case ('fill-blank'):
		return new FillInBlankHandler(data, q_id);
	case ('single-answer'):
		return new SingleAnswerHandler(data, q_id);
	}
};

// function QuestionHandler (data, q_id) {
// 	this.getType = function () {};
// 	this.getDOMSubStructure = function () {}
// 	this.grade = function () {return {score:0,possible:_data.score};}
// }

function FillInBlankHandler (data, q_id) {
	var _data = data;
	var _answer;
	var _user_input_elem;
	var _question_id = q_id;
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
		_user_input_elem.attach_enter_listener(ui_handler.next);
		_user_input_elem.addEventListener('keydown',
			function(e){
				_user_input_elem.style.width = max(5,_user_input_elem.value.length/1.7) + 'em';
			}
		);
		_user_input_elem.style.textAlign = 'center'
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
	

	this.grade = function () {
		console.log('user ans: '+_user_input_elem.value+' correct: '+_data.answers[0]);
		var score = {score:0,possible:_data.score};
		var user_answer = _user_input_elem.value;
		if (_data.answers.indexOf(user_answer) != -1) {
			score.score = _data.score;
		} else {
			score.score = 0;
		}
		return score;
	}
}

function MultipleChoiceHandler(data, q_id) {
	var _data = data;
	var _selection;
	var _this = this;
	var _question_id = q_id;
	var _last_clicked;
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
		options_ul.classList.add('mult-c-options');
		var lis = [];
		for (var i = 0; i < options.length; i++) {
			lis.push(document.createElement('li'));
			var new_in = document.createElement('input');
			new_in.type = 'radio';
			new_in.name = "mult-c-option-"+_question_id;
			new_in.index_selected = i;
			new_in.value = options[i];
			if (i == 0) {
				new_in.checked = true;
				_last_clicked = i;
			}
			new_in.addEventListener('click', function (e) {
				if (e.x != 0 && e.y != 0) {
					_last_clicked = e.toElement.index_selected;
				}
			})
			var new_option_label = document.createElement('span');
			new_option_label.innerHTML = options[i];
			new_option_label.classList.add('mult-c-option-label');
			lis[i].appendChild(new_in);
			lis[i].appendChild(new_option_label);
		};
		lis.shuffle();
		for (var i = 0; i < lis.length; i++) {
			options_ul.appendChild(lis[i]);
		};
		wrapper.appendChild(options_ul);
		return wrapper;
	};
	
	this.grade = function () {
		var score = {score:0,possible:_data.score};
		if (_last_clicked == _data.correct) score.score = _data.score;
		return score;
	}

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
	this.getType = function() {
		return "picture-response";
	};
	this.getDOMSubStructure = function () {
		var wrapper = document.createElement('div');
		var img_disp = document.createElement('img');
		img_disp.src = _data.img_url;
		img_disp.classList.add('picutre-response-img','center-block');
		wrapper.appendChild(img_disp);
		var prompt_div = document.createElement('h2');
		prompt_div.classList.add('prompt');
		prompt_div.innerHTML = _data.prompt;
		wrapper.appendChild(prompt_div);
		
		_user_input_elem = document.createElement('input');
		_user_input_elem.id = 'pict-resp-input-'+_question_id;
		_user_input_elem.type = 'text';
		_user_input_elem.classList.add('center-block');
		_user_input_elem.attach_enter_listener(ui_handler.next);
		wrapper.appendChild(_user_input_elem);
		return wrapper;
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
	}
}



function SingleAnswerHandler(data, q_id) {
	var _data = data;
	var _answer;
	var _user_input_elem;
	var _question_id = q_id;
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
		_user_input_elem.attach_enter_listener(ui_handler.next);
		wrapper.appendChild(_user_input_elem);
		return wrapper;
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
	}
}


