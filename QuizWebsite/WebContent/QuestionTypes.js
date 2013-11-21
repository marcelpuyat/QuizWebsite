/*
 * MUST IMPLEMENT!!: __constructor__(data, q_id) - id must be unique to ensure unique id tags in generated elems
 * 	   				 getType() - returns type
 *                   getDOMSubStructure(bool addButton) - returns displayed DOM structure
 *                   format_answer() - returns object to be inserted into JSON
 *					 grade() - returns user score
 */
function getQuestionHandler(type, data, q_id) {
	switch (type) {
	case ('multiple-choice'):
		return new MultipleChoiceHandler(data, q_id);
	case ('single-answer'):
		return new SingleAnswerHandler(data, q_id);
	case ('picture-response'):
		return new PictureResponseHandler(data, q_id);
	}
};

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
	
	this.format_answer = function() {
		if (_user_input_elem == undefined) {
			return {answer:null};
		}
		return {answer:_user_input_elem.value};
	};
	this.grade = function () {
		var user_answer = _user_input_elem.value;
		if (_data.correct.indexOf(user_answer) != -1) {
			return _data.score;
		} else {
			return 0;
		}
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
	
	this.format_answer = function() {
		if (_user_input_elem == undefined) {
			return {answer:null};
		}
		return {answer:_user_input_elem.value};
	};

	this.grade = function () {
		var user_answer = _user_input_elem.value;
		if (_data.correct.indexOf(user_answer) != -1) {
			return _data.score;
		} else {
			return 0;
		}
	}
}




function MultipleChoiceHandler(data, q_id) {
	var _data = data;
	var _selection;
	var _user_input_elems = [];
	var _question_id = q_id;
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
		for (var i = 0; i < options.length; i++) {
			var new_option_wrapper = document.createElement('li');
			_user_input_elems.push(document.createElement('input'));
			_user_input_elems[i].type = 'radio';
			_user_input_elems[i].name = "mult-c-option-"+_question_id;
			_user_input_elems[i].index = i.toString();
			_user_input_elems[i].value = options[i];
			if (i == 0) _user_input_elems[i].checked = true;
			var new_option_label = document.createElement('span');
			new_option_label.innerHTML = options[i];
			new_option_label.classList.add('mult-c-option-label');
			new_option_wrapper.appendChild(_user_input_elems[i]);
			new_option_wrapper.appendChild(new_option_label);
			options_ul.appendChild(new_option_wrapper);
		}	
		wrapper.appendChild(options_ul);
		return wrapper;
	};
	/* returns object properly formatted to be returned with answer JSON */
	this.format_answer = function() {
		var checked = get_checked();
		if (checked == undefined) {
			return {};
		} else {
			var checked = get_checked();
			return {item_selected:checked.value,index_selected:checked.index};
		}
	};
	
	this.grade = function () {
		var user_answer = get_checked().value;
		if (_data.correct == user_answer.index) {
			return _data.score;
		} else {
			return 0;
		}
	}

	function get_checked() {
		var check_boxes = document.getElementsByName('mult-c-option-'+_question_id);
		for (var i = 0; i < check_boxes.length; i++) {
			if (check_boxes[i].checked) return check_boxes[i];
		}
	}
}
