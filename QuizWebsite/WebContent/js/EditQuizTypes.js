/**
 * 
 */
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
 			wrapper.q_handler = new type.question_class(_this);
 			wrapper.appendChild(wrapper.q_handler.getElem());
 		})
 		wrapper.appendChild(types_input);
 		wrapper.appendChild(select_type_button);
 		return wrapper;
 	}

	this.getWithData = function (data) {
		
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


function MultipleChoiceHandler (parent) {
	var _data;
	var _parent = parent;
	var _prompt;
	this.getElem = function () {
		var div = document.createElement('div');
		_prompt = document.createElement('input');
		_prompt.type = "text";
		_prompt.addEventListener('keyup',_parent.postData);
		div.appendChild(_prompt);
		return div;
	}
	this.ingestData = function (data) {
		_data = data;
	}
	this.reap = function () {
		return {
			type:'multiple-choice',
			prompt:_prompt.value,
			options:[],
			correct:0,
			score:1
		};
	}
}
function MultipleAnswerHandler (parent) {
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
function PictureResponseHandler (parent) {
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
function MatchingHandler (parent) {
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
function FillInBlankHandler (parent) {
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
function SingleAnswerHandler (parent) {
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

