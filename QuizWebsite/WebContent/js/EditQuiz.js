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

 	})();
 }