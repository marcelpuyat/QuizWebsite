/**
 * 
 */
var submit_approved = true;
function init_js() {
	add_new_username_check(document.getElementById('new-username'),document.getElementById('new-username-taken-error-message'), document.getElementById('new-username-malformed-error-message'));
	add_new_password_check(document.getElementById('new-password1'),document.getElementById('new-password2'),document.getElementById('new-password-error-message'));
}

function add_new_username_check (username_elem, taken_err_elem, malformed_err_elem) {
	username_elem.addEventListener('keyup', function () {
		if (username_elem.value.match(/[a-zA-Z0-9\-_]*/)[0].length ==  username_elem.value.length) {
			malformed_err_elem.classList.add('hide');
			submit_approved = true;
		} else {
			malformed_err_elem.classList.remove('hide');
			submit_approved = false;
		}
		get_json_from_url('/QuizWebsite/UserServlet?api=availability&username='+username_elem.value,
			function (data) {
				console.log(data);
				if (data.available) {
					console.log('available');
					taken_err_elem.classList.add('hide');
					submit_approved = true;
				} else {
					console.log('taken');
					taken_err_elem.classList.remove('hide');
					submit_approved = false;
				}
			});
	});
}

function add_new_password_check (password_elem_1,password_elem_2, err_elem) {
	var password_changed = function () {
		if (password_elem_1.value == password_elem_2.value) {
			err_elem.classList.add('hide');
			submit_approved = true;
		} else {
			err_elem.classList.remove('hide');
			submit_approved = false;
		}
	}
	password_elem_1.addEventListener('keyup', password_changed);
	password_elem_2.addEventListener('keyup', password_changed);
}

