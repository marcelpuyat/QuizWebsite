/**
 * 
 */
var submit_approved = true;
var username_charset = 'a-zA-Z0-9\-_';
function init_js() {
	add_new_username_check(document.getElementById('new-username'),document.getElementById('new-username-taken-error-message'), document.getElementById('new-username-malformed-error-message'));
	add_new_password_check(document.getElementById('new-password1'),document.getElementById('new-password2'),document.getElementById('new-password-error-message'));
}

function add_new_username_check (username_elem, taken_err_elem, malformed_err_elem) {
	username_elem.addEventListener('keyup', function () {
		if (username_elem.value.isOfCharSet(username_charset)) {
			malformed_err_elem.classList.add('hide');
			submit_approved = true;
		} else {
			malformed_err_elem.classList.remove('hide');
			taken_err_elem.classList.add('hide');
			return;
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

function submit_new_user () {
	if (submit_approved) {
		var username  = document.getElementById('new-username').value;
		var password1 = document.getElementById('new-password1').value;
		var password2 = document.getElementById('new-password2').value;

		if (!username || ! password1 || !password2) return;
		if (username == '' || password1 == '' || password2 == '') return;
		if (!username.isOfCharSet(username_charset)) return;
		if (password1 != password2) return;
		post_json_to_url('/QuizWebsite/UserServlet?api=create_user',
		{
			new_username:username,
			new_password:password1,
			new_password_redundant:password2
		}, function (data) {
			if (data.status == 'success') {
				var account_create_ul = document.getElementById('create-account-ul');
				account_create_ul.classList.add('opacity-hide');
				var now_sign_in = document.getElementById('now-sign-in-prompt');
				now_sign_in.classList.remove('hide');
				document.getElementById('forward-to').value = 'settings';
			} else {
				var err_message_elem = document.getElementById('creation-error-message');
				err_message_elem.classList.remove('hide');
			}
		});

	}
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

