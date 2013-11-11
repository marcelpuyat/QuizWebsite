/**
 * 
 */
var q_handler;
function init_js(quiz_id) {
	resize_app();
	window.onresize = resize_app;
	getQuizJSON(load_content, quiz_id);
}

function start() {
	next_question();
};

function next_question() {
	if (q_handler) {
		q_handler.nextQuestion();
	}
}

function question_answered() {
	if (q_handler) {
		q_handler.answered_question();
	}
	next_question();
	return false;
}

function resize_app() {
	document.getElementById('app-wrapper').style.width = 
		document.getElementById('app-selection-wrapper').parentElement.clientWidth 
		- document.getElementById('app-selection-wrapper').clientWidth - 10 + "px";
}

function load_content(data, quiz_id) {
	console.log('loaded data from quiz: '+quiz_id);
	console.log(data);
	q_handler = new QuizHandler(document.getElementById('quiz-prompt-content'), quiz_id);
	var questions = data.questions;
	for (var i = 0; i < questions.length; i++) {
		var QuestionHandlerType = getQuestionHandler(questions[i].type);
		q_handler.pushQuestion(new QuestionHandlerType(questions[i].data));
	}
	
}


function getQuizJSON(fn_callback, quiz_id) {
	get_json_from_url("/QuizWebsite/QuizServletStub?quiz_id="+quiz_id, fn_callback, quiz_id);
}

function sendQuizResults(data) {
	console.log('sending data:');
	console.log(data);
	post_json_to_url("/QuizWebsite/QuizServletStub", data, function (data) {
		console.log(data);
	});
}



function QuizHandler(dom_wrapper, quiz_id) {
	var _dom_wrapper = dom_wrapper;
	var _questions = [];
	var _iterator = -1;
	var _quiz_id = quiz_id;
	this.pushQuestion = function(questionHandler) {
		_questions.push(questionHandler);
	};
	this.nextQuestion = function() {
		_dom_wrapper.innerHTML = "";
		if (++_iterator < _questions.length) {
			_dom_wrapper.appendChild(_questions[_iterator].getDOMSubStructure());
		} else {
			_dom_wrapper.innerHTML = "all done!";
			var answers = [];
			for (var i = 0; i < _questions.length; i++) {
				answers.push({type:_questions[i].getType(),data:_questions[i].format_answer()});
			}
			var answer = {quiz_id:_quiz_id,'answers':answers};
			sendQuizResults(answer);
		}
	};
	this.answered_question = function () {
		_questions[_iterator].answered_question();
	};
	
}