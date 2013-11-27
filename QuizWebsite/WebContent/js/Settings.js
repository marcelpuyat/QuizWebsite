/**
 * 
 */
 var update_url = '/QuizWebsite/UserServlet?api=update';
 function init_js () {
 	document.getElementById('first-name-button').addEventListener('click',update_first_name);
 	document.getElementById('last-name-button').addEventListener('click',update_last_name);
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