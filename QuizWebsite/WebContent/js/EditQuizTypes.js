/**
 * 
 */
var unique_id = 0;
function TypeHandler (wrapper) {
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
 		})
 		wrapper.appendChild(types_input);
 		wrapper.appendChild(select_type_button);
 		return wrapper;
 	}

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
			var qh = new types[index].question_class(_this, types[index]);
			qh.ingestData(data);
			return qh.getElem();
		} else {
			console.log('type not found');
			console.log(data);
			return this.getSelector();
		}
	}

	this.postData = function () {
		var questions = [];
		var children = _wrapper.children;
		for (var i = 0; i < children.length; i++) {
			var q_handler = children[i].firstChild.q_handler;
			if (q_handler) {
				questions.push(q_handler.reap());
			}
		};
		console.log(questions);
	}
}


function MultipleChoiceHandler (parent, type) {
	var _id = unique_id++;
	var _type = type;
	var _data;
	var _parent = parent;
	var _prompt;
	var _options_ul;
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
		_prompt.addEventListener('keyup',_parent.postData);
		prompt_li.appendChild(_prompt);
		ul.appendChild(prompt_li);

		/* options title */
		var options_title_li = document.createElement('li');
		var options_title = document.createElement('div');
		options_title.classList.add('faint');
		options_title.innerHTML = 'options';
		options_title_li.appendChild(options_title);
		ul.appendChild(options_title_li);

		/* options */
		var options_container_li = document.createElement('li');
		_options_ul = document.createElement('ul');
		if (_data && _data.data && _data.data.options) {
			var options = _data.data.options;
			for (var i = 0; i < options.length; i++) {
				_options_ul.appendChild(get_choice(options[i]));
			};
		}
		options_container_li.appendChild(_options_ul);

		ul.appendChild(options_container_li);

		return ul;
	}
	this.ingestData = function (data) {
		_data = data;
	}
	this.reap = function () {
		return {
			type:_type.json_name,
			prompt:_prompt.value,
			options:[],
			correct:0,
			score:1
		};
	}
	function get_choice (value) {
		console.log(value);
		value = value || "";
		var li = document.createElement('li');

		var radio = document.createElement('input');
		radio.type = 'radio';
		radio.name = 'multiple-choice-radio-'+_id;

		var text = document.createElement('input');
		text.type = 'text';
		text.name = 'multiple-choice-text-'+_id;
		text.value = value;

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
	this.getElem = function () {
		var div = document.createElement('div');
		div.innerHTML = 'hi';
		return div;
	}
	this.ingestData = function (data) {
		_data = data;
	}
	this.reap = function () {
		return {};
	}
}
function PictureResponseHandler (parent, type) {
	var _id = unique_id++;
	var _type = type;
	var _data;
	var _parent = parent;
	this.getElem = function () {
		var div = document.createElement('div');
		div.innerHTML = 'hi';
		return div;
	}
	this.ingestData = function (data) {
		_data = data;
	}
	this.reap = function () {
		return {};
	}
}
function MatchingHandler (parent, type) {
	var _id = unique_id++;
	var _type = type;
	var _data;
	var _parent = parent;
	this.getElem = function () {
		var div = document.createElement('div');
		div.innerHTML = 'hi';
		return div;
	}
	this.ingestData = function (data) {
		_data = data;
	}
	this.reap = function () {
		return {};
	}
}
function FillInBlankHandler (parent, type) {
	var _id = unique_id++;
	var _type = type;
	var _data;
	var _parent = parent;
	this.getElem = function () {
		var div = document.createElement('div');
		div.innerHTML = 'hi';
		return div;
	}
	this.ingestData = function (data) {
		_data = data;
	}
	this.reap = function () {
		return {};
	}
}
function SingleAnswerHandler (parent, type) {
	var _id = unique_id++;
	var _type = type;
	var _data;
	var _parent = parent;
	this.getElem = function () {
		var div = document.createElement('div');
		div.innerHTML = 'hi';
		return div;
	}
	this.ingestData = function (data) {
		_data = data;
	}
	this.reap = function () {
		return {};
	}
}

