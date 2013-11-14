/*
 *  QuizHandler(int quiz_id, str servlet_url):
 *     boolean isLoaded();
 *     boolean hasNext();
 *     element getNext();
 *     void    waitForLoad(fn(elem, aux) callback, aux);    //async, must load results from server
 *     void    waitForResults(fn(elem, aux) callback, aux); //  ^same
 *     void    informQuestionAnswered();
 *     
 */

function QuizHandler(quiz_id, servlet_url) {
	/* init vars */
	var _questions = [];
	var _data;
	var _iterator;
	var _isLoaded = false;
	var _quiz_id = quiz_id;
	var _servlet_url = servlet_url;

	this.iterator_pos = function() {
		return _iterator;
	};
	
	
	/* public methods */
	this.isLoaded = function() {
		return _isLoaded;
	};
	
	this.hasNext = function() {
		return (
				this.isLoaded() && (
					(_iterator == undefined && (_questions.length > 0)) ||
					((_iterator + 1) < _questions.length)
				)
			);
	};
	
	this.getNext = function() {
		if (this.hasNext()) {
			if (_iterator == undefined) {
				_iterator = 0;
			} else {
				_iterator++;
			}
			console.log('next:: '+_iterator);
			var next_elem = _questions[_iterator].getDOMSubStructure();
			return next_elem;
		}
		return null;
	};
	
	this.waitForLoad = function(callback, auxiliary_data) {
		_load_quiz_json(_servlet_url, _quiz_id, function(aux){
			var elem = document.createElement('div');
			elem.innerHTML = 'loaded';
			aux.client_callback(elem, aux.client_aux);
		}, {client_aux:auxiliary_data, client_callback:callback});
	};
	
	this.waitForResults = function(callback, auxiliary_data) {
		/* build and send answer data */
		var answer = _build_results();
		_send_quiz_results(answer, function (data, aux) {
			
			/* build display element */
			var elem = document.createElement('div');
			var feedback = data.feedback;
			elem.innerHTML = 'total correct: '+ feedback.total_correct+'\ntotal possible: '+feedback.total_possible+'\ntotal score: '+feedback.total_score;
			console.log(data);
			callback(elem, aux.client_aux);
			
		}, {client_aux:auxiliary_data,dev_aux:{}});
	};
	
	this.informQuestionAnswered = function() {
		_questions[_iterator].answered_question();
	};
	
	
	
	
	/* private methods */
	function _load_quiz_json(servlet_url, quiz_id, callback, aux) {
		console.log('loading ' + quiz_id);
		get_json_from_url(servlet_url+"?quiz_id="+quiz_id, function (data, aux) {
			console.log('::loaded quiz::');
			console.log('..data:');
			console.log(data);
			_data = data;
			var questions_json = data.questions;
			for (var i = 0; i < questions_json.length; i++) {
				var question_obj = questions_json[i];
				var question_handler = getQuestionHandler(question_obj.type);
				_questions.push(new question_handler(question_obj.data));
			}
			_isLoaded = true;
			aux.client_callback(aux.client_aux);
		}, {quiz_id:quiz_id, client_aux:aux, client_callback:callback});
	}
	function _build_results() {
		var answers = [];
		for (var i = 0; i < _questions.length; i++) {
			answers.push({type:_questions[i].getType(),data:_questions[i].format_answer()});
		}
		return  {quiz_id:_quiz_id,'answers':answers};
	}
	
	function _send_quiz_results(data, callback, aux) {
		console.log('sending data:');
		console.log(data);
		post_json_to_url("/QuizWebsite/QuizServletStub", data, callback, aux);
	}
}