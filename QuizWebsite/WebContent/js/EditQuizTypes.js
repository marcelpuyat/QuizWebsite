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
	var _header;

	this.getHeader = function (data) {
		_header = new MetaHandler(this);
		return _header.getElem(data);
	}

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
		var jsonResponse = _header.reap();
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

function MetaHandler (parent) {
	var _title;
	var _description;
	var _parent = parent;
	var _data;
	var _settings = {
		is_immediately_corrected_elem:undefined,
		is_multiple_page_elem:undefined,
		is_randomized_elem:undefined,
		is_practicable_elem:undefined
	};
	this.getElem = function (data) {
		_data = data;
		var ul = document.createElement('ul');
		ul.classList.add('login-ul','center','gap-large');

		/* title */
		var title_li = document.createElement('li');
		_title = document.createElement('h1');
		_title.innerHTML = data.quiz_name;
		_title.contentEditable = true;
		_title.addEventListener('keydown',no_enter);
		_title.addEventListener('keyup',_parent.postData);
		_title.classList.add('lighter');
		title_li.appendChild(_title);
		ul.appendChild(title_li);

		/* description */
		var description_li = document.createElement('li');
		_description = document.createElement('h4');
		var description = data.description;
		if (data.description == undefined || data.description == "") {
			description = "description";
		}
		_description.innerHTML = description;
		_description.contentEditable = true;
		_description.addEventListener('keydown',no_enter);
		_description.addEventListener('keyup',_parent.postData);
		_description.classList.add('faint');
		description_li.appendChild(_description);
		ul.appendChild(description_li);

		/* settings */
		var settings_li = document.createElement('li');
		var settings_ul = document.createElement('ul');
		settings_ul.classList.add('flowing','settings-ul','gap');


		var is_randomized_elem_li = document.createElement('li');
		is_randomized_elem_li.classList.add('flowing');
		var is_randomized_elem_prompt = document.createElement('span');
		is_randomized_elem_prompt.innerHTML = 'randomize';
		_settings.is_randomized_elem = document.createElement('input');
		_settings.is_randomized_elem.classList.add('flowing');
		_settings.is_randomized_elem.type = 'checkbox';
		var randomized = true;
		if (data && data.is_randomized != undefined) randomized = data.is_randomized;
		_settings.is_randomized_elem.checked = randomized;
		is_randomized_elem_li.appendChild(_settings.is_randomized_elem);
		is_randomized_elem_li.appendChild(is_randomized_elem_prompt);
		settings_ul.appendChild(is_randomized_elem_li);


		var is_practicable_elem_li = document.createElement('li');
		is_practicable_elem_li.classList.add('flowing');
		var is_practicable_elem_prompt = document.createElement('span');
		is_practicable_elem_prompt.innerHTML = 'allow practicing';
		_settings.is_practicable_elem = document.createElement('input');
		_settings.is_practicable_elem.classList.add('flowing');
		_settings.is_practicable_elem.type = 'checkbox';
		var practicable = true;
		if (data && data.is_practicable != undefined) practicable = data.is_practicable;
		_settings.is_practicable_elem.checked = practicable;
		is_practicable_elem_li.appendChild(_settings.is_practicable_elem);
		is_practicable_elem_li.appendChild(is_practicable_elem_prompt);
		settings_ul.appendChild(is_practicable_elem_li);

		
		var is_immediately_corrected_elem_li = document.createElement('li');
		is_immediately_corrected_elem_li.classList.add('flowing');
		var is_immediately_corrected_elem_prompt = document.createElement('span');
		is_immediately_corrected_elem_prompt.innerHTML = 'correct immediately';
		_settings.is_immediately_corrected_elem = document.createElement('input');
		_settings.is_immediately_corrected_elem.classList.add('flowing');
		_settings.is_immediately_corrected_elem.type = 'checkbox';
		var imediate = true;
		if (data && data.is_immediately_corrected != undefined) imediate = data.is_immediately_corrected;
		_settings.is_immediately_corrected_elem.checked = imediate;
		is_immediately_corrected_elem_li.appendChild(_settings.is_immediately_corrected_elem);
		is_immediately_corrected_elem_li.appendChild(is_immediately_corrected_elem_prompt);
		settings_ul.appendChild(is_immediately_corrected_elem_li);


		var is_multiple_page_elem_li = document.createElement('li');
		is_multiple_page_elem_li.classList.add('flowing');
		var is_multiple_page_elem_prompt = document.createElement('span');
		is_multiple_page_elem_prompt.innerHTML = 'show on multiple pages';
		_settings.is_multiple_page_elem = document.createElement('input');
		_settings.is_multiple_page_elem.classList.add('flowing');
		_settings.is_multiple_page_elem.type = 'checkbox';
		var multi = true;
		if (data && data.is_multiple_page != undefined) multi = data.is_multiple_page;
		_settings.is_multiple_page_elem.checked = multi;
		is_multiple_page_elem_li.appendChild(_settings.is_multiple_page_elem);
		is_multiple_page_elem_li.appendChild(is_multiple_page_elem_prompt);
		settings_ul.appendChild(is_multiple_page_elem_li);


		_settings.is_immediately_corrected_elem.addEventListener('change',_parent.postData);
		_settings.is_multiple_page_elem.addEventListener('change',_parent.postData);
		_settings.is_randomized_elem.addEventListener('change',_parent.postData);
		_settings.is_practicable_elem.addEventListener('change',_parent.postData);

		settings_li.appendChild(settings_ul);
		ul.appendChild(settings_li);

		return ul;
	}
	this.reap = function () {
		return {
 			quiz_name:_title.innerHTML||"",
 			description:_description.innerHTML||"",
 			creator:_data.creator||"",
 			max_score:0,
 			is_immediately_corrected: _settings.is_immediately_corrected_elem.checked,
 			is_multiple_page:_settings.is_multiple_page_elem.checked,
 			is_randomized:_settings.is_randomized_elem.checked,
 			is_practicable:_settings.is_practicable_elem.checked,
 			tags:_data.tags || []
 		}
	}

	function no_enter (e) {
		if (e.keyCode == 13) {//stop enter
			e = e || window.event; // get window.event if e argument missing (in IE)
			if (e.preventDefault) { e.preventDefault(); } // stops the browser from redirecting off to the image.
			return false;
		}
	}
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
	var _partial_credit_check;
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

		/* partial credit check title */
		var partial_credit_check_title_li = document.createElement('li');
		var partial_credit_check_title = document.createElement('div');
		partial_credit_check_title.classList.add('faint');
		partial_credit_check_title.innerHTML = 'give partial credit';
		partial_credit_check_title_li.appendChild(partial_credit_check_title);
		ul.appendChild(partial_credit_check_title_li);

		/* partial credit check */
		var partial_credit_check_li = document.createElement('li');
		_partial_credit_check = document.createElement('input');
		_partial_credit_check.type = 'checkbox';
		var partial_credit = true;
		if (_data && _data.data && _data.data.partial_credit != undefined) {
			partial_credit = _data.data.partial_credit;
		}
		_partial_credit_check.checked = partial_credit;
		_partial_credit_check.addEventListener('change',_parent.postData);
		partial_credit_check_li.appendChild(_partial_credit_check);
		ul.appendChild(partial_credit_check_li);

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
				partial_credit:_partial_credit_check.checked,
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
			data: {
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

