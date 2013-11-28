/**
 * 
 */
 function post_new_quiz (username) {
 	var quiz_input = document.getElementById('quiz-name-input');
 	post_json_to_url(
 			'/QuizWebsite/QuizServlet?quiz_id=new',
 			{
 				quiz_id:"new",
 				quiz_name:quiz_input.value,
 				description:"",
 				creator:username,
 				max_score:0,
 				is_immediately_corrected:false,
 				is_multiple_page:true,
 				is_randomized:true,
 				is_practicable:true,
 				tags:[],
 				questions:[]
 			},
 			function (data) {
 				window.location.replace('/QuizWebsite/EditQuiz.jsp?quiz_id='+data.id);
 			}
 		)
 }