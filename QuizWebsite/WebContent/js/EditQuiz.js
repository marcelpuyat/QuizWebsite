/**
 * 
 */
 var num = 0;
 var th;
 function init_js (quiz_id) {
 	new QuestionsHandler(document.getElementById('questions-ul'), quiz_id);
 }

 function QuestionsHandler (wrapper, quiz_id) {
 	var _quiz_id = quiz_id;
 	var _wrapper = wrapper;
 	var _questions = [];
 	var _th = new TypeHandler(_wrapper);
 	th = _th;

 	(function init () {
 		console.log(_quiz_id);
 		get_json_from_url(
 				'/QuizWebsite/QuizServlet?quiz_id='+quiz_id,
 				construct_ui
 			);
 	})();

 	function construct_ui (data) {
 		var questions = data.questions;
 		_wrapper.appendChild(get_divider());
 		for (var i = 0; i < questions.length; i++) {
 			_wrapper.appendChild(new_elem(questions[i]));
 			_wrapper.appendChild(get_divider());
 		};
 	}

 	function get_divider () {
 		var li = document.createElement('li');
 		li.classList.add('transition-medium');
 		var line_left  = document.createElement('span');
 		var line_right = document.createElement('span');
 		line_left.classList.add('line','left');
 		line_right.classList.add('line','right');
 		li.appendChild(line_left);
 		li.appendChild(line_right);

 		var plus_circle = document.createElement('div');
 		var plus        = document.createElement('span')
 		plus_circle.addEventListener('click', function () {
 			var parent = li.parentElement;
 			parent.insertBefore(get_divider(), li);
 			parent.insertBefore(new_elem(), li);
 		});

 		plus_circle.classList.add('plus-circle')
 		plus.classList.add('plus');
 		plus.innerHTML = '+';
 		plus_circle.appendChild(plus);
 		li.appendChild(plus_circle);
 		return li;
 	}

 	function new_elem (data) {
 		var li = document.createElement('li');
 		li.classList.add('question-section');
 		//var th = new TypeHandler(li);
 		if (data) {
 			li.appendChild(_th.getWithData(data));
 		} else {
 			li.appendChild(_th.getSelector());
 		}
 		return li;
 	}
 }