/**
 * MUST IMPLEMENT!!: getDOMSubStructure(data) - returns displayed DOM structure
 *                   format_answer() - returns object to be inserted into JSON
 *                   answered_question() - handles answered question
 */
function MultipleChoiceHandler(data) {
	var _data = data;
	var _selection;
	/* returns a DOM element which will be inserted into the quiz's 'quiz-content' div */
	this.getDOMSubStructure = function () {
		var frm = document.createElement('form');
		
		var prompt = _data.prompt;
		var prompt_div = document.createElement('div');
		frm.appendChild(prompt_div);
		
		var options = _data.options;
		var options_ul = document.createElement('ul');
		for (var i = 0; i < options.length; i++) {
			var new_option_wrapper = document.createElement('li');
			var new_option = document.createElement('input');
			new_option.type = 'radio';
			new_option.name = "mult-c-option";
			new_option.value = options[i];
			if (i == 0) new_option.checked = true;
			var new_option_label = document.createElement('span');
			new_option_label.innerHTML = options[i];
			new_option_wrapper.appendChild(new_option);
			new_option_wrapper.appendChild(new_option_label);
			options_ul.appendChild(new_option_wrapper);
		}	
		frm.appendChild(options_ul);
		var sub = document.createElement('input');
		sub.type = "button";
		sub.value = "Submit!";
		sub.setAttribute('onclick','question_answered();');
		frm.appendChild(sub);
		return frm;
	};
	/* returns object properly formatted to be returned with answer JSON */
	this.format_answer = function() {
		return {item_selected:_selection};
	};
	
	function get_checked() {
		var check_boxes = document.getElementsByName('mult-c-option');
		for (var i = 0; i < check_boxes.length; i++) {
			if (check_boxes[i].checked) return check_boxes[i];
		}
	}
	
	this.answered_question = function () {
		_selection = get_checked().value;
	};
}