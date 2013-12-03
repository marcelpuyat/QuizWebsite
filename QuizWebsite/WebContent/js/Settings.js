/**
 * 
 */
 var update_url = '/QuizWebsite/UserServlet?api=update';
 function init_js () {
 	document.getElementById('first-name-button').addEventListener('click',update_first_name);
 	document.getElementById('last-name-button').addEventListener('click',update_last_name);
 	document.getElementById('profile-picture-button').addEventListener('click',update_profile_picture);
 }

 function update_first_name () {
 	var text_field = document.getElementById('first-name-update');
 	var title      = document.getElementById('first-name-update-label');
 	post_json_to_url(update_url+'&field=first_name', 
 		{first_name:text_field.value}, 
 		collapse, 
 		{collapse:[this, text_field, title]});
 }

 function update_last_name () {
 	var text_field = document.getElementById('last-name-update');
 	var title      = document.getElementById('last-name-update-label');
 	post_json_to_url(update_url+'&field=last_name', 
 		{last_name:text_field.value}, 
 		collapse, 
 		{collapse:[this, text_field, title]});
 }

 function collapse (data, args) {
 	var collapse = args.collapse;
 	for (var i = 0; i < collapse.length; i++) {
 		collapse[i].classList.add('opacity-hide');
 	}
 }

 function update_profile_picture () {
 	var text_field = document.getElementById('profile-picture-update');
 	var title      = document.getElementById('profile-picture-update-label');
 	post_json_to_url(update_url+'&field=profile_picture', 
 		{profile_picture:text_field.value}, 
 		collapse, 
 		{collapse:[this, text_field, title]});
 }