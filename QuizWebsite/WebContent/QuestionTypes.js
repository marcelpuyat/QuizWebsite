/*
 * MUST IMPLEMENT!!: __constructor__(data, q_id) - id must be unique to ensure unique id tags in generated elems
 * 	   				 getType() - returns type
 *                   getDOMSubStructure() - returns displayed DOM structure
 *                   format_answer() - returns object to be inserted into JSON
 *                   answered_question() - handles answered question
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
		console.log('here');
		var frm = document.createElement('form');
		var img_disp = document.createElement('img');
		img_disp.src = _data.img_url;
		img_disp.className = 'picutre-response-img center-block';
		frm.appendChild(img_disp);
		var prompt_div = document.createElement('h2');
		prompt_div.className = 'prompt';
		prompt_div.innerHTML = _data.prompt;
		frm.appendChild(prompt_div);
		
		_user_input_elem = document.createElement('input');
		_user_input_elem.id = 'pict-resp-input-'+_question_id;
		_user_input_elem.className = 'center-block';
		frm.appendChild(_user_input_elem);
		
		var sub = document.createElement('input');
		sub.type = "button";
		sub.value = "Next";
		sub.className = 'submit-button';
		sub.setAttribute('onclick','question_answered();');
		frm.appendChild(sub);
		return frm;
	};
	
	this.format_answer = function() {
		return {answer:_answer};
	};
	
	this.answered_question = function () {
		_answer = _user_input_elem.value;
	};
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
		var frm = document.createElement('form');
		var prompt_div = document.createElement('h2');
		prompt_div.className = 'prompt';
		prompt_div.innerHTML = _data.prompt;
		frm.appendChild(prompt_div);
		
		_user_input_elem = document.createElement('input');
		_user_input_elem.id = 'single-c-input-'+_question_id;
		_user_input_elem.className = 'center-block';
		frm.appendChild(_user_input_elem);
		
		var sub = document.createElement('input');
		sub.type = "button";
		sub.value = "Next";
		sub.className = 'submit-button';
		sub.setAttribute('onclick','question_answered();');
		frm.appendChild(sub);
		return frm;
	};
	
	this.format_answer = function() {
		return {answer:_answer};
	};
	
	this.answered_question = function () {
		_answer = _user_input_elem.value;
	};
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
		var frm = document.createElement('form');
		
		var prompt = _data.prompt;
		var prompt_div = document.createElement('h2');
		prompt_div.className = 'prompt';
		prompt_div.innerHTML = _data.prompt;
		frm.appendChild(prompt_div);
		
		var options = _data.options;
		var options_ul = document.createElement('ul');
		options_ul.className = 'mult-c-options';
		for (var i = 0; i < options.length; i++) {
			var new_option_wrapper = document.createElement('li');
			_user_input_elems.push(document.createElement('input'));
			_user_input_elems[i].type = 'radio';
			_user_input_elems[i].name = "mult-c-option-"+_question_id;
			_user_input_elems[i].id = i.toString();
			_user_input_elems[i].value = options[i];
			if (i == 0) _user_input_elems[i].checked = true;
			var new_option_label = document.createElement('span');
			new_option_label.innerHTML = options[i];
			new_option_label.className = 'mult-c-option-label';
			new_option_wrapper.appendChild(_user_input_elems[i]);
			new_option_wrapper.appendChild(new_option_label);
			options_ul.appendChild(new_option_wrapper);
		}	
		frm.appendChild(options_ul);
		var sub = document.createElement('input');
		sub.type = "button";
		sub.value = "Next";
		sub.className = 'submit-button';
		sub.setAttribute('onclick','question_answered();');
		frm.appendChild(sub);
		return frm;
	};
	/* returns object properly formatted to be returned with answer JSON */
	this.format_answer = function() {
		return _selection;
	};
	
	function get_checked() {
		var check_boxes = document.getElementsByName('mult-c-option-'+_question_id);
		for (var i = 0; i < check_boxes.length; i++) {
			if (check_boxes[i].checked) return check_boxes[i];
		}
	}
	
	/* handles answered question */
	this.answered_question = function () {
		var checked = get_checked();
		_selection = {item_selected:checked.value,index_selected:checked.id};
	};
}