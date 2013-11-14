/**
 * 
 */
var q_handler;
var wrapper_elem = document.getElementById('quiz-prompt-content');
function init_js(quiz_id) {
	console.log('quiz init: '+quiz_id);
	resize_app();
	window.onresize = resize_app;
	q_handler = new QuizHandler(quiz_id, "/QuizWebsite/QuizServletStub");
	q_handler.waitForLoad(function(elem, aux){
		aux.wrapper.appendChild(elem);
	}, {wrapper:wrapper_elem});
}

function start() {
	next_question();
};

function next_question() {
	if (q_handler && q_handler.isLoaded()) {
		if (q_handler.hasNext()) {
			wrapper_elem.innerHTML = "";
			wrapper_elem.appendChild(q_handler.getNext());
		} else {
			wrapper_elem.innerHTML = "loading results...";
			q_handler.waitForResults(function(elem, aux){
				aux.wrp_elem.innerHTML = "";
				aux.wrp_elem.appendChild(elem);
			}, {wrp_elem:wrapper_elem});
		}
	}
}

function question_answered() {
	if (q_handler) {
		q_handler.informQuestionAnswered();
	}
	next_question();
	return false;
}

function resize_app() {
	document.getElementById('app-wrapper').style.width = 
		document.getElementById('app-selection-wrapper').parentElement.clientWidth 
		- document.getElementById('app-selection-wrapper').clientWidth - 10 + "px";
}




