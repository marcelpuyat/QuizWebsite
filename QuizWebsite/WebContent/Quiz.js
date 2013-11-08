/**
 * 
 */
var q_handler;
function init_js(quiz_id) {
	resize_app();
	window.onresize = resize_app;
	getQuizJSON(load_content, quiz_id);
}

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
	q_handler = new QuizHandler(document.getElementById('quiz-prompt-content'), quiz_id);
	var questions = data.questions;
	for (var i = 0; i < questions.length; i++) {
		switch(questions[i].type) {
		case ('multiple-choice'):
			q_handler.pushQuestion(new MultipleChoiceHandler(questions[i].data));
			break;
		}
	}
	
}


function getQuizJSON(fn_callback, quiz_id) {
	//get_json_from_url("/QuizWebsite/QuizServlet?quiz_id="+quiz_id, fn_callback);
	console.log('STUB IMPLEMENTATION');
	fn_callback({questions:[
	                        {type:'multiple-choice',
	                        	data:{
	                        		prompt:'what class is this assignment for?',
	                        		options: ['CS110','CS108','CS23141'],
	                        		correct: 1
	                        	}}
	                        ]
	}, quiz_id);//STUBB!!!
}

function sendQuizResults(data) {
	console.log(data);
	console.log('STUBBBB!!!');
	post_json_to_url("/QuizWebsite/QuizServlet", data, function (data) {
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
				answers.push(_questions[i].format_answer());
			}
			var answer = {quiz_id:_quiz_id,'answers':answers};
			sendQuizResults(answer);
		}
	};
	this.answered_question = function () {
		_questions[_iterator].answered_question();
	};
	
}