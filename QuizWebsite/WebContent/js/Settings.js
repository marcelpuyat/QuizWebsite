/**
 * 
 */
 var update_url = '/QuizWebsite/UserServlet?api=update';
 function init_js () {
   var form = document.getElementById('settings-update-form');

   var data = {};
   for (var i = 0, ii = form.length; i < ii; ++i) {
     var input = form[i];
     if (input.name) {
       data[input.name] = input.value;
     }
   }

 	//document.getElementById('first-name-button').addEventListener('click',update_first_name);
 	//document.getElementById('last-name-button').addEventListener('click',update_last_name);
 	//document.getElementById('profile-picture-button').addEventListener('click',update_profile_picture);
 	//document.getElementById('email-address-button').addEventListener('click', update_email_address);
 }

function update_all() {
  var data = {
 	  "first_name": document.getElementById('first-name-update').value,
 	  "last_name": document.getElementById('last-name-update').value,
 	  "profile_picture": document.getElementById('profile-picture-update').value,
 	  "email_address": document.getElementById('email-address-update').value
  };
  // TODO: Give better success / failure indicator when an update finishes
  var handler = function(response, args) {
    location.reload();
  }
  post_json_to_url(update_url, data, handler, null);
}
