/**
 * 
 */
 function init_js (quiz_id) {
 	new QuestionsHandler(document.getElementById('questions-ul'), quiz_id);
 }

 function QuestionsHandler (wrapper, quiz_id) {
 	var _quiz_id = quiz_id;
 	var _wrapper = wrapper;

 	(function init () {
 		
 		console.log(_quiz_id);
 		_wrapper.appendChild(get_divider());

 	})();

 	function get_divider () {
 		var li = document.createElement('li');
 		//li.style.borderBottom = '1px solid rgba(0,0,0,0.3)';
 		var line_left  = document.createElement('span');
 		var line_right = document.createElement('span');
 		line_left.style.width = '48%';
 		line_right.style.width = '48%';
 		line_left.style.borderBottom = '1px solid rgba(0,0,0,0.3)';
 		line_right.style.borderBottom = '1px solid rgba(0,0,0,0.3)';
 		line_left.style.float = 'left';
 		line_right.style.float = 'right';
 		li.style.position = 'relative';
 		li.appendChild(line_left);
 		li.appendChild(line_right);

 		var add = document.createElement('span');
 		add.style.width = '4%';
 		add.style.height = add.style.width;
 		add.innerHTML = '+';
 		add.style.border = '1px solid rgba(0,0,0,0.3)';
 		add.style.borderRadius = '50%';
 		li.appendChild(add);
 		return li;
 	}
 }