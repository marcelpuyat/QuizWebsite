/**
 * 
 */
var unique_id = 0;
function TypeHandler (wrapper, parent, quiz_id) {
	var types = [
		{json_name:'multiple-choice',question_class:MultipleChoiceHandler,display_name:'Multiple Choice'},
		{json_name:'multiple-answer',question_class:MultipleAnswerHandler,display_name:'Multiple Answer'},
		{json_name:'picture-response',question_class:PictureResponseHandler,display_name:'Picture Response'},
		{json_name:'matching',question_class:MatchingHandler,display_name:'Matching'},
		{json_name:'fill-blank',question_class:FillInBlankHandler,display_name:'Fill In the Blank'},
		{json_name:'single-answer',question_class:SingleAnswerHandler,display_name:'Single Answer'}
	];
	var _wrapper = wrapper;
	var _questionHandler;
	var _this = this;
	var _parent = parent;
	var _quiz_id = quiz_id;

 	this.getSelector = function () {
 		var wrapper = document.createElement('div');
 		wrapper.classList.add('center');
 		var types_input = document.createElement('select');
 		for (var i = 0; i < types.length; i++) {
 			var option = document.createElement('option');
 			option.value = i.toString();
 			option.innerHTML = types[i].display_name;
 			types_input.appendChild(option);
 		};
 		var select_type_button = document.createElement('input');
 		select_type_button.type = 'button';
 		select_type_button.value = 'go';
 		select_type_button.classList.add('select-type-button');
 		select_type_button.addEventListener('click', function () {
 			var selected_index = types_input.options[types_input.selectedIndex];
 			var type = types[selected_index.value];
 			wrapper.innerHTML = '';
 			wrapper.q_handler = new type.question_class(_this, type);
 			wrapper.appendChild(wrapper.q_handler.getElem());
 		});
 		wrapper.appendChild(types_input);
 		wrapper.appendChild(select_type_button);
 		return wrapper;
 	};

	this.getWithData = function (data) {
		var type = data.type;
		var index = -1;
		for (var i = 0; i < types.length; i++) {
			if (types[i].json_name == type) {
				index = i;
				break;
			}
		};
		if (index != -1) {
			var wrapper = document.createElement('div');
 			wrapper.classList.add('center');
			var qh = new types[index].question_class(_this, types[index]);
			qh.ingestData(data);
			wrapper.appendChild(qh.getElem());
			wrapper.q_handler = qh;
			return wrapper;
		} else {
			console.log('type not found');
			console.log(data);
			return this.getSelector();
		}
	};

	this.postData = function () {
		var questions = [];
		var children = _wrapper.children;
		var total_score = 0;
		for (var i = 0; i < children.length; i++) {
			var q_handler = children[i].firstChild.q_handler;
			if (q_handler) {
				var q_reap = q_handler.reap();
				questions.push(q_reap);//add score to max score
				total_score+=parseInt(q_reap.data.score);
			}
		};
		var jsonResponse = _parent.getMeta();
		jsonResponse.questions = questions;
		jsonResponse.max_score = total_score;
		jsonResponse.quiz_id = parseInt(_quiz_id);
		console.log('sending to :'+'/QuizWebsite/QuizServlet?quiz_id='+_quiz_id);
		console.log(jsonResponse);
		post_json_to_url(
			'/QuizWebsite/QuizServlet?quiz_id='+_quiz_id,
			jsonResponse,
			function (data) {
				console.log(data);
			}
		);
	};
}


function MultipleChoiceHandler (parent, type) {
	var _id = unique_id++;
	var _type = type;
	var _data;
	var _parent = parent;
	var _prompt;
	var _answers_ul;
	var _score_input;
	this.getElem = function () {
		var ul = document.createElement('ul');
		ul.classList.add('login-ul','center');

		/* title */
		var title_li = document.createElement('li');
		var title = document.createElement('h2');
		title.innerHTML = type.display_name;
		title.classList.add('lighter');
		title_li.appendChild(title);
		ul.appendChild(title_li);

		/* score title */
		var prompt_title_li = document.createElement('li');
		var prompt_title = document.createElement('div');
		prompt_title.classList.add('faint');
		prompt_title.innerHTML = 'score possible';
		prompt_title_li.appendChild(prompt_title);
		ul.appendChild(prompt_title_li);


		/* score */
		var score = 1;
		if (_data && _data.data && _data.data.score) score = parseInt(_data.data.score);
		var score_li = document.createElement('li');
		_score_input = document.createElement('input');
		_score_input.classList.add('center');
		_score_input.type = 'number';
		_score_input.min = 0;
		_score_input.addEventListener('change',_parent.postData);
		_score_input.value = score;
		score_li.appendChild(_score_input);
		ul.appendChild(score_li);

		/* prompt title */
		var prompt_title_li = document.createElement('li');
		var prompt_title = document.createElement('div');
		prompt_title.classList.add('faint');
		prompt_title.innerHTML = 'prompt';
		prompt_title_li.appendChild(prompt_title);
		ul.appendChild(prompt_title_li);

		/* prompt */
		var prompt_li = document.createElement('li');
		_prompt = document.createElement('input');
		_prompt.type = "text";
		if (_data && _data.data && _data.data.prompt) {
			_prompt.value = _data.data.prompt;
		}
		_prompt.classList.add('prompt-input');
		_prompt.addEventListener('keyup',_parent.postData);
		prompt_li.appendChild(_prompt);
		ul.appendChild(prompt_li);

		/* answers title */
		var answers_title_li = document.createElement('li');
		var answers_title = document.createElement('div');
		answers_title.classList.add('faint');
		answers_title.innerHTML = 'answers';
		answers_title_li.appendChild(answers_title);
		ul.appendChild(answers_title_li);

		/* answers */
		var answers_container_li = document.createElement('li');
		_answers_ul = document.createElement('ul');
		if (_data && _data.data && _data.data.options) {
			var answers = _data.data.options;
			for (var i = 0; i < answers.length; i++) {
				var checked = (i == _data.data.correct);
				_answers_ul.appendChild(get_choice(answers[i],checked, false));
			};
		}
		append_blank_option(_answers_ul);
		answers_container_li.appendChild(_answers_ul);

		ul.appendChild(answers_container_li);

		return ul;
	}
	this.ingestData = function (data) {
		_data = data;
	};
	this.reap = function () {
		var opsObj = get_answers();
		return {
			type:_type.json_name,
			data:{
				prompt:_prompt.value,
				options:opsObj.answers,
				correct:opsObj.checked,
				score:parseInt(_score_input.value)
			}
		};
	};
	function append_blank_option (ul) {
		var li = get_choice("",false, true);
		li.ul = ul;
		li.addEventListener('keyup', new_option_keyup_handler);
		ul.appendChild(li);
	}
	function new_option_keyup_handler () {
		this.removeEventListener('keyup',new_option_keyup_handler);
		append_blank_option(this.ul);
	}
	function get_answers () {
		var answers_lis = document.getElementsByClassName('multiple-choice-li-'+_id);
		var checked = -1;
		var answers = [];
		for (var i = 0; i < answers_lis.length; i++) {
			var children = answers_lis[i].children;
			if (answers_lis[i].text.value != "") {
				answers.push(answers_lis[i].text.value);
				if (answers_lis[i].radio.checked) {
					checked = answers.length - 1; //will be last element in array
				}
			}
		};
		return {answers:answers, checked:checked};
	}
	function get_choice (value, checked, disabled) {
		console.log(value);
		value = value || "";
		var li = document.createElement('li');
		li.classList.add('multiple-choice-li-'+_id);

		var radio = document.createElement('input');
		radio.type = 'radio';
		radio.name = 'multiple-choice-radio-'+_id;
		radio.checked = checked;
		radio.disabled = disabled;
		radio.text = text;

		var text = document.createElement('input');
		text.type = 'text';
		text.name = 'multiple-choice-text-'+_id;
		text.value = value;
		text.radio = radio;

		li.text = text;
		li.radio = radio;

		radio.addEventListener('click',_parent.postData);
		text.addEventListener('keyup', function () {
			if (text.value == "") radio.disabled = true;
			else radio.disabled = false;
			_parent.postData();
		});

		li.appendChild(radio);
		li.appendChild(text);
		return li;
	}
}

function MultipleAnswerHandler (parent, type) {
	var _id = unique_id++;
	var _type = type;
	var _data;
	var _parent = parent;
	var _prompt;
	var _answers_ul;
	var _score_input;
	this.getElem = function () {
		var ul = document.createElement('ul');
		ul.classList.add('login-ul','center');

		/* title */
		var title_li = document.createElement('li');
		var title = document.createElement('h2');
		title.innerHTML = type.display_name;
		title.classList.add('lighter');
		title_li.appendChild(title);
		ul.appendChild(title_li);

		/* score title */
		var prompt_title_li = document.createElement('li');
		var prompt_title = document.createElement('div');
		prompt_title.classList.add('faint');
		prompt_title.innerHTML = 'score possible';
		prompt_title_li.appendChild(prompt_title);
		ul.appendChild(prompt_title_li);


		/* score */
		var score = 1;
		if (_data && _data.data && _data.data.score) score = parseInt(_data.data.score);
		var score_li = document.createElement('li');
		_score_input = document.createElement('input');
		_score_input.classList.add('center');
		_score_input.type = 'number';
		_score_input.min = 0;
		_score_input.addEventListener('change',_parent.postData);
		_score_input.value = score;
		score_li.appendChild(_score_input);
		ul.appendChild(score_li);

		/* prompt title */
		var prompt_title_li = document.createElement('li');
		var prompt_title = document.createElement('div');
		prompt_title.classList.add('faint');
		prompt_title.innerHTML = 'prompt';
		prompt_title_li.appendChild(prompt_title);
		ul.appendChild(prompt_title_li);

		/* prompt */
		var prompt_li = document.createElement('li');
		_prompt = document.createElement('input');
		_prompt.type = "text";
		if (_data && _data.data && _data.data.prompt) {
			_prompt.value = _data.data.prompt;
		}
		_prompt.classList.add('prompt-input');
		_prompt.addEventListener('keyup',_parent.postData);
		prompt_li.appendChild(_prompt);
		ul.appendChild(prompt_li);

		/* answers title */
		var answers_title_li = document.createElement('li');
		var answers_title = document.createElement('div');
		answers_title.classList.add('faint');
		answers_title.innerHTML = 'answers';
		answers_title_li.appendChild(answers_title);
		ul.appendChild(answers_title_li);

		/* answers */
		var answers_container_li = document.createElement('li');
		_answers_ul = document.createElement('ul');
		if (_data && _data.data && _data.data.correct) {
			var answers = _data.data.correct;
			for (var i = 0; i < answers.length; i++) {
				var checked = (answers[i][1]);
				var text    = (answers[i][0]);
				_answers_ul.appendChild(get_choice(text,checked, false));
			};
		}
		append_blank_option(_answers_ul);
		answers_container_li.appendChild(_answers_ul);

		ul.appendChild(answers_container_li);

		return ul;
	}
	this.ingestData = function (data) {
		_data = data;
	};
	this.reap = function () {
		var ansObj = get_answers();
		return {
			type:_type.json_name,
			data:{
				prompt:_prompt.value,
				partial_credit:true,
				correct:ansObj.answers,
				score:parseInt(_score_input.value)
			}
		};
	};
	function append_blank_option (ul) {
		var li = get_choice("",false, true);
		li.ul = ul;
		li.addEventListener('keyup', new_option_keyup_handler);
		ul.appendChild(li);
	}
	function new_option_keyup_handler () {
		this.removeEventListener('keyup',new_option_keyup_handler);
		append_blank_option(this.ul);
	}
	function get_answers () {
		var answers_lis = document.getElementsByClassName('multiple-choice-li-'+_id);
		var checked = -1;
		var answers = [];
		for (var i = 0; i < answers_lis.length; i++) {
			var children = answers_lis[i].children;
			if (answers_lis[i].text.value != "") {
				answers.push([answers_lis[i].text.value,answers_lis[i].check_box.checked]);
			}
		};
		return {answers:answers};
	}
	function get_choice (value, checked, disabled) {
		console.log(value);
		value = value || "";
		var li = document.createElement('li');
		li.classList.add('multiple-choice-li-'+_id);

		var check_box = document.createElement('input');
		check_box.type = 'checkbox';
		check_box.name = 'multiple-choice-check_box-'+_id;
		check_box.checked = checked;
		check_box.disabled = disabled;
		check_box.text = text;

		var text = document.createElement('input');
		text.type = 'text';
		text.name = 'multiple-choice-text-'+_id;
		text.value = value;
		text.check_box = check_box;

		li.text = text;
		li.check_box = check_box;

		check_box.addEventListener('click',_parent.postData);
		text.addEventListener('keyup', function () {
			if (text.value == "") check_box.disabled = true;
			else check_box.disabled = false;
			_parent.postData();
		});

		li.appendChild(check_box);
		li.appendChild(text);
		return li;
	}
}

function PictureResponseHandler (parent, type) {
	var _id = unique_id++;
	var _type = type;
	var _data;
	var _parent = parent;
	var _prompt;
	var _url;
	var _answers_ul;
	var _score_input;
	this.getElem = function () {
		var ul = document.createElement('ul');
		ul.classList.add('login-ul','center');

		/* title */
		var title_li = document.createElement('li');
		var title = document.createElement('h2');
		title.innerHTML = type.display_name;
		title.classList.add('lighter');
		title_li.appendChild(title);
		ul.appendChild(title_li);

		/* score title */
		var prompt_title_li = document.createElement('li');
		var prompt_title = document.createElement('div');
		prompt_title.classList.add('faint');
		prompt_title.innerHTML = 'score possible';
		prompt_title_li.appendChild(prompt_title);
		ul.appendChild(prompt_title_li);


		/* score */
		var score = 1;
		if (_data && _data.data && _data.data.score) score = parseInt(_data.data.score);
		var score_li = document.createElement('li');
		_score_input = document.createElement('input');
		_score_input.classList.add('center');
		_score_input.type = 'number';
		_score_input.min = 0;
		_score_input.addEventListener('change',_parent.postData);
		_score_input.value = score;
		score_li.appendChild(_score_input);
		ul.appendChild(score_li);

		/* url title */
		var url_title_li = document.createElement('li');
		var url_title = document.createElement('div');
		url_title.classList.add('faint');
		url_title.innerHTML = 'image url';
		url_title_li.appendChild(url_title);
		ul.appendChild(url_title_li);

		/* url input */
		var prompt_li = document.createElement('li');
		_url = document.createElement('input');
		_url.type = "text";
		if (_data && _data.data && _data.data.img_url) {
			_url.value = _data.data.img_url;
		}
		// _url.classList.add('prompt-input');
		_url.addEventListener('keyup',_parent.postData);
		prompt_li.appendChild(_url);
		ul.appendChild(prompt_li);

		/* prompt title */
		var prompt_title_li = document.createElement('li');
		var prompt_title = document.createElement('div');
		prompt_title.classList.add('faint');
		prompt_title.innerHTML = 'prompt';
		prompt_title_li.appendChild(prompt_title);
		ul.appendChild(prompt_title_li);

		/* prompt */
		var prompt_li = document.createElement('li');
		_prompt = document.createElement('input');
		_prompt.type = "text";
		if (_data && _data.data && _data.data.prompt) {
			_prompt.value = _data.data.prompt;
		}
		_prompt.classList.add('prompt-input');
		_prompt.addEventListener('keyup',_parent.postData);
		prompt_li.appendChild(_prompt);
		ul.appendChild(prompt_li);

		/* answers title */
		var answers_title_li = document.createElement('li');
		var answers_title = document.createElement('div');
		answers_title.classList.add('faint');
		answers_title.innerHTML = 'answers';
		answers_title_li.appendChild(answers_title);
		ul.appendChild(answers_title_li);

		/* answers */
		var answers_container_li = document.createElement('li');
		_answers_ul = document.createElement('ul');
		if (_data && _data.data && _data.data.correct) {
			var correct = _data.data.correct;
			for (var i = 0; i < correct.length; i++) {
				var text    = (correct[i]);
				_answers_ul.appendChild(get_choice(text));
			};
		}
		append_blank_option(_answers_ul);
		answers_container_li.appendChild(_answers_ul);

		ul.appendChild(answers_container_li);

		return ul;
	}
	this.ingestData = function (data) {
		_data = data;
	};
	this.reap = function () {
		var ansObj = get_correct();
		return {
			type:_type.json_name,
			data: {
				img_url:_url.value,
				prompt:_prompt.value,
				correct:ansObj.correct,
				score:parseInt(_score_input.value)
			}
		};
	};
	function append_blank_option (ul) {
		var li = get_choice("");
		li.ul = ul;
		li.addEventListener('keyup', new_option_keyup_handler);
		ul.appendChild(li);
	}
	function new_option_keyup_handler () {
		this.removeEventListener('keyup',new_option_keyup_handler);
		append_blank_option(this.ul);
	}
	function get_correct () {
		var answers_lis = document.getElementsByClassName('multiple-choice-li-'+_id);
		var correct = [];
		for (var i = 0; i < answers_lis.length; i++) {
			var children = answers_lis[i].children;
			if (answers_lis[i].text.value != "") {
				correct.push(answers_lis[i].text.value);
			}
		};
		return {correct:correct};
	}
	function get_choice (value) {
		console.log(value);
		value = value || "";
		var li = document.createElement('li');
		li.classList.add('multiple-choice-li-'+_id);

		var text = document.createElement('input');
		text.type = 'text';
		text.name = 'multiple-choice-text-'+_id;
		text.value = value;

		li.text = text;

		text.addEventListener('keyup', _parent.postData);

		li.appendChild(text);
		return li;
	}
}
function MatchingHandler (parent, type) {
	var _id = unique_id++;
	var _type = type;
	var _data;
	var _parent = parent;
	var _prompt;
	var _answers_ul;
	var _score_input;
	this.getElem = function () {
		var ul = document.createElement('ul');
		ul.classList.add('login-ul','center');

		/* title */
		var title_li = document.createElement('li');
		var title = document.createElement('h2');
		title.innerHTML = type.display_name;
		title.classList.add('lighter');
		title_li.appendChild(title);
		ul.appendChild(title_li);

		/* score title */
		var prompt_title_li = document.createElement('li');
		var prompt_title = document.createElement('div');
		prompt_title.classList.add('faint');
		prompt_title.innerHTML = 'score possible';
		prompt_title_li.appendChild(prompt_title);
		ul.appendChild(prompt_title_li);


		/* score */
		var score = 1;
		if (_data && _data.data && _data.data.score) score = parseInt(_data.data.score);
		var score_li = document.createElement('li');
		_score_input = document.createElement('input');
		_score_input.classList.add('center');
		_score_input.type = 'number';
		_score_input.min = 0;
		_score_input.addEventListener('change',_parent.postData);
		_score_input.value = score;
		score_li.appendChild(_score_input);
		ul.appendChild(score_li);

		/* prompt title */
		var prompt_title_li = document.createElement('li');
		var prompt_title = document.createElement('div');
		prompt_title.classList.add('faint');
		prompt_title.innerHTML = 'prompt';
		prompt_title_li.appendChild(prompt_title);
		ul.appendChild(prompt_title_li);

		/* prompt */
		var prompt_li = document.createElement('li');
		_prompt = document.createElement('input');
		_prompt.type = "text";
		if (_data && _data.data && _data.data.prompt) {
			_prompt.value = _data.data.prompt;
		}
		_prompt.classList.add('prompt-input');
		_prompt.addEventListener('keyup',_parent.postData);
		prompt_li.appendChild(_prompt);
		ul.appendChild(prompt_li);

		/* answers title */
		var answers_title_li = document.createElement('li');
		var answers_title = document.createElement('div');
		answers_title.classList.add('faint');
		answers_title.innerHTML = 'answers';
		answers_title_li.appendChild(answers_title);
		ul.appendChild(answers_title_li);

		/* answers */
		var answers_container_li = document.createElement('li');
		_answers_ul = document.createElement('ul');
		if (_data && _data.data && _data.data.correct) {
			var answers = _data.data.correct;
			for (var i = 0; i < answers.length; i++) {
				var left = (answers[i][0]);
				var right    = (answers[i][1]);
				_answers_ul.appendChild(get_choice(left,right));
			};
		}
		append_blank_option(_answers_ul);
		answers_container_li.appendChild(_answers_ul);

		ul.appendChild(answers_container_li);

		return ul;
	}
	this.ingestData = function (data) {
		_data = data;
	};
	this.reap = function () {
		var ansObj = get_answers();
		return {
			type:_type.json_name,
			data: {
				prompt:_prompt.value,
				correct:ansObj.answers,
				score:parseInt(_score_input.value)
			}
		};
	};
	function append_blank_option (ul) {
		var li = get_choice("","");
		li.ul = ul;
		li.addEventListener('keyup', new_option_keyup_handler);
		ul.appendChild(li);
	}
	function new_option_keyup_handler () {
		this.removeEventListener('keyup',new_option_keyup_handler);
		append_blank_option(this.ul);
	}
	function get_answers () {
		var answers_lis = document.getElementsByClassName('multiple-choice-li-'+_id);
		var checked = -1;
		var answers = [];
		for (var i = 0; i < answers_lis.length; i++) {
			var children = answers_lis[i].children;
			if (answers_lis[i].left_text.value != "" && answers_lis[i].right_text.value != "") {
				answers.push([answers_lis[i].left_text.value,answers_lis[i].right_text.value]);
			}
		};
		return {answers:answers};
	}
	function get_choice (left, right, disabled) {
		left = left || "";
		right = right || "";
		var li = document.createElement('li');
		li.classList.add('multiple-choice-li-'+_id);

		var left_text = document.createElement('input');
		left_text.type = 'text';
		left_text.value = left;

		var right_text = document.createElement('input');
		right_text.type = 'text';
		right_text.value = right;

		li.left_text = left_text;
		li.right_text = right_text;

		left_text.addEventListener('keyup',_parent.postData);
		right_text.addEventListener('keyup',_parent.postData);

		li.appendChild(left_text);
		li.appendChild(right_text);
		return li;
	}
}

function FillInBlankHandler (parent, type) {
	var _id = unique_id++;
	var _type = type;
	var _data;
	var _parent = parent;
	var _prompt;
	var _before_prompt
	var _after_prompt;
	var _answers_ul;
	var _score_input;
	this.getElem = function () {
		var ul = document.createElement('ul');
		ul.classList.add('login-ul','center');

		/* title */
		var title_li = document.createElement('li');
		var title = document.createElement('h2');
		title.innerHTML = type.display_name;
		title.classList.add('lighter');
		title_li.appendChild(title);
		ul.appendChild(title_li);

		/* score title */
		var prompt_title_li = document.createElement('li');
		var prompt_title = document.createElement('div');
		prompt_title.classList.add('faint');
		prompt_title.innerHTML = 'score possible';
		prompt_title_li.appendChild(prompt_title);
		ul.appendChild(prompt_title_li);


		/* score */
		var score = 1;
		if (_data && _data.data && _data.data.score) score = parseInt(_data.data.score);
		var score_li = document.createElement('li');
		_score_input = document.createElement('input');
		_score_input.classList.add('center');
		_score_input.type = 'number';
		_score_input.min = 0;
		_score_input.addEventListener('change',_parent.postData);
		_score_input.value = score;
		score_li.appendChild(_score_input);
		ul.appendChild(score_li);

		/* prompt title */
		var prompt_title_li = document.createElement('li');
		var prompt_title = document.createElement('div');
		prompt_title.classList.add('faint');
		prompt_title.innerHTML = 'prompt';
		prompt_title_li.appendChild(prompt_title);
		ul.appendChild(prompt_title_li);

		/* prompt */
		var prompt_li = document.createElement('li');
		_prompt = document.createElement('input');
		_prompt.type = "text";
		if (_data && _data.data && _data.data.optional_prompt) {
			_prompt.value = _data.data.optional_prompt;
		}
		_prompt.classList.add('prompt-input');
		_prompt.addEventListener('keyup',_parent.postData);
		prompt_li.appendChild(_prompt);
		ul.appendChild(prompt_li);


		/* before prompt title */
		var before_prompt_title_li = document.createElement('li');
		var before_prompt_title = document.createElement('div');
		before_prompt_title.classList.add('faint');
		before_prompt_title.innerHTML = 'before prompt';
		before_prompt_title_li.appendChild(before_prompt_title);
		ul.appendChild(before_prompt_title_li);

		/* before prompt */
		var before_prompt_li = document.createElement('li');
		_before_prompt = document.createElement('input');
		_before_prompt.type = "text";
		if (_data && _data.data && _data.data.first_prompt) {
			_before_prompt.value = _data.data.first_prompt;
		}
		_before_prompt.classList.add('prompt-input');
		_before_prompt.addEventListener('keyup',_parent.postData);
		before_prompt_li.appendChild(_before_prompt);
		ul.appendChild(before_prompt_li);


		/* answers title */
		var answers_title_li = document.createElement('li');
		var answers_title = document.createElement('div');
		answers_title.classList.add('faint');
		answers_title.innerHTML = 'answers';
		answers_title_li.appendChild(answers_title);
		ul.appendChild(answers_title_li);

		/* answers */
		var answers_container_li = document.createElement('li');
		_answers_ul = document.createElement('ul');
		if (_data && _data.data && _data.data.correct) {
			var answers = _data.data.correct;
			if (answers.length == undefined) answers = [answers];
			for (var i = 0; i < answers.length; i++) {
				var text    = (answers[i]);
				_answers_ul.appendChild(get_choice(text));
			};
		}
		append_blank_option(_answers_ul);
		answers_container_li.appendChild(_answers_ul);
		ul.appendChild(answers_container_li);


		/* after prompt title */
		var after_prompt_title_li = document.createElement('li');
		var after_prompt_title = document.createElement('div');
		after_prompt_title.classList.add('faint');
		after_prompt_title.innerHTML = 'after prompt';
		after_prompt_title_li.appendChild(after_prompt_title);
		ul.appendChild(after_prompt_title_li);

		/* after prompt */
		var after_prompt_li = document.createElement('li');
		_after_prompt = document.createElement('input');
		_after_prompt.type = "text";
		if (_data && _data.data && _data.data.second_prompt) {
			_after_prompt.value = _data.data.second_prompt;
		}
		_after_prompt.classList.add('prompt-input');
		_after_prompt.addEventListener('keyup',_parent.postData);
		after_prompt_li.appendChild(_after_prompt);
		ul.appendChild(after_prompt_li);

		return ul;
	}
	this.ingestData = function (data) {
		_data = data;
	};
	this.reap = function () {
		var ansObj = get_correct();
		return {
			type:_type.json_name,
			data:{
				optional_prompt:_prompt.value,
				first_prompt:_before_prompt.value,
				second_prompt:_after_prompt.value,
				correct:ansObj.correct,
				score:parseInt(_score_input.value)
			}
		};
	};
	function append_blank_option (ul) {
		var li = get_choice("");
		li.ul = ul;
		li.addEventListener('keyup', new_option_keyup_handler);
		ul.appendChild(li);
	}
	function new_option_keyup_handler () {
		this.removeEventListener('keyup',new_option_keyup_handler);
		append_blank_option(this.ul);
	}
	function get_correct () {
		var answers_lis = document.getElementsByClassName('multiple-choice-li-'+_id);
		var correct = [];
		for (var i = 0; i < answers_lis.length; i++) {
			var children = answers_lis[i].children;
			if (answers_lis[i].text.value != "") {
				correct.push(answers_lis[i].text.value);
			}
		};
		return {correct:correct};
	}
	function get_choice (value) {
		console.log(value);
		value = value || "";
		var li = document.createElement('li');
		li.classList.add('multiple-choice-li-'+_id);

		var text = document.createElement('input');
		text.type = 'text';
		text.name = 'multiple-choice-text-'+_id;
		text.value = value;

		li.text = text;

		text.addEventListener('keyup', _parent.postData);

		li.appendChild(text);
		return li;
	}
}

function SingleAnswerHandler (parent, type) {
	var _id = unique_id++;
	var _type = type;
	var _data;
	var _parent = parent;
	var _prompt;
	var _answers_ul;
	var _score_input;
	this.getElem = function () {
		var ul = document.createElement('ul');
		ul.classList.add('login-ul','center');

		/* title */
		var title_li = document.createElement('li');
		var title = document.createElement('h2');
		title.innerHTML = type.display_name;
		title.classList.add('lighter');
		title_li.appendChild(title);
		ul.appendChild(title_li);

		/* score title */
		var prompt_title_li = document.createElement('li');
		var prompt_title = document.createElement('div');
		prompt_title.classList.add('faint');
		prompt_title.innerHTML = 'score possible';
		prompt_title_li.appendChild(prompt_title);
		ul.appendChild(prompt_title_li);


		/* score */
		var score = 1;
		if (_data && _data.data && _data.data.score) score = parseInt(_data.data.score);
		var score_li = document.createElement('li');
		_score_input = document.createElement('input');
		_score_input.classList.add('center');
		_score_input.type = 'number';
		_score_input.min = 0;
		_score_input.addEventListener('change',_parent.postData);
		_score_input.value = score;
		score_li.appendChild(_score_input);
		ul.appendChild(score_li);

		/* prompt title */
		var prompt_title_li = document.createElement('li');
		var prompt_title = document.createElement('div');
		prompt_title.classList.add('faint');
		prompt_title.innerHTML = 'prompt';
		prompt_title_li.appendChild(prompt_title);
		ul.appendChild(prompt_title_li);

		/* prompt */
		var prompt_li = document.createElement('li');
		_prompt = document.createElement('input');
		_prompt.type = "text";
		if (_data && _data.data && _data.data.prompt) {
			_prompt.value = _data.data.prompt;
		}
		_prompt.classList.add('prompt-input');
		_prompt.addEventListener('keyup',_parent.postData);
		prompt_li.appendChild(_prompt);
		ul.appendChild(prompt_li);

		/* answers title */
		var answers_title_li = document.createElement('li');
		var answers_title = document.createElement('div');
		answers_title.classList.add('faint');
		answers_title.innerHTML = 'answers';
		answers_title_li.appendChild(answers_title);
		ul.appendChild(answers_title_li);

		/* answers */
		var answers_container_li = document.createElement('li');
		_answers_ul = document.createElement('ul');
		if (_data && _data.data && _data.data.correct) {
			var correct = _data.data.correct;
			for (var i = 0; i < correct.length; i++) {
				var text    = (correct[i]);
				_answers_ul.appendChild(get_choice(text));
			};
		}
		append_blank_option(_answers_ul);
		answers_container_li.appendChild(_answers_ul);

		ul.appendChild(answers_container_li);

		return ul;
	}
	this.ingestData = function (data) {
		_data = data;
	};
	this.reap = function () {
		var ansObj = get_correct();
		return {
			type:_type.json_name,
			prompt:_prompt.value,
			correct:ansObj.correct,
			score:parseInt(_score_input.value)
		};
	};
	function append_blank_option (ul) {
		var li = get_choice("");
		li.ul = ul;
		li.addEventListener('keyup', new_option_keyup_handler);
		ul.appendChild(li);
	}
	function new_option_keyup_handler () {
		this.removeEventListener('keyup',new_option_keyup_handler);
		append_blank_option(this.ul);
	}
	function get_correct () {
		var answers_lis = document.getElementsByClassName('multiple-choice-li-'+_id);
		var correct = [];
		for (var i = 0; i < answers_lis.length; i++) {
			var children = answers_lis[i].children;
			if (answers_lis[i].text.value != "") {
				correct.push(answers_lis[i].text.value);
			}
		};
		return {correct:correct};
	}
	function get_choice (value) {
		console.log(value);
		value = value || "";
		var li = document.createElement('li');
		li.classList.add('multiple-choice-li-'+_id);

		var text = document.createElement('input');
		text.type = 'text';
		text.name = 'multiple-choice-text-'+_id;
		text.value = value;

		li.text = text;

		text.addEventListener('keyup', _parent.postData);

		li.appendChild(text);
		return li;
	}
}

