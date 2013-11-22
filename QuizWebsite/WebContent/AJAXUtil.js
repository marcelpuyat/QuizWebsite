/**
 * 
 */

function get_json_from_url (url, responseHandler, args) {
	var xmlhttp;
	
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	
	xmlhttp.onreadystatechange=function() {
		if (xmlhttp.readyState==4 && xmlhttp.status==200) {
			responseHandler(JSON.parse(xmlhttp.responseText), args);
		}
	};
	xmlhttp.open("GET",url,true);
	xmlhttp.send();
};

function post_json_to_url (url, data, completionHandler, args) {
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange=function() {
		if (xmlhttp.readyState==4 && xmlhttp.status==200) {
			if (completionHandler) {
				completionHandler(JSON.parse(xmlhttp.responseText), args);
			}
		}
	};
	//xmlhttp.setRequestHeader('Content-Type', 'application/json;');
	xmlhttp.open("POST",url,true);
	xmlhttp.send(JSON.stringify(data));
};


function min (a,b,comp) {
	var diff;
	if (comp) {
		diff = comp(a,b);
	} else {
		diff = a - b;
	}
	if (diff < 0) return a;
	else return b;
}

function max (a,b,comp) {
	var diff;
	if (comp) {
		diff = comp(a,b);
	} else {
		diff = a - b;
	}
	if (diff > 0) return a;
	else return b;
}

// adapted from...
//+ Jonas Raoni Soares Silva
//@ http://jsfromhell.com/array/shuffle [v1.0]
Array.prototype.shuffle = function () {
	for(var j, x, i = this.length; i; j = Math.floor(Math.random() * i), x = this[--i], this[i] = this[j], this[j] = x);
	return this;
}

//q.addEventListener('keydown',function(e){q.style.width = max(10,q.value.length/1.6) + 'em';});
//q.style.textAlign = 'center'