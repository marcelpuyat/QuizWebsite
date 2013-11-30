/**
* 
*/
function init_js (quiz_id) {
	query_db();

	function query_db () {
		console.log('/QuizWebsite/QuizInfoServlet?quiz_id='+quiz_id);
		get_json_from_url(
			'/QuizWebsite/QuizInfoServlet?quiz_id='+quiz_id,
			build_ui
		);
	}

	function build_ui (data) {
		set_name(data.quiz_name);
		set_description(data.description);
		set_creator(data.creator);
	}

	function set_name (name) {
		console.log(name);
		var name_elem = document.getElementById('quiz-name');
		name_elem.innerHTML = name;
	}

	function set_description (description) {
		var description_elem = document.getElementById('quiz-description');
		description_elem.innerHTML = description;
	}

	function set_creator (creator) {
		var creator_elem = document.getElementById('quiz-creator');
		creator_elem.innerHTML = creator;
	}

}