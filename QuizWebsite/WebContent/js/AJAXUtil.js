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
};

Node.prototype.setCSS3Attr = function(attr, val) {
	var css3_kits = ['webkit',
					'Moz',
					'O',
					'ms',
					''];
	for (var i = 0; i < css3_kits.length; i++) {
		var formatted_val = val.replace('%-kit-','-'+css3_kits[i]+'-');
		if (css3_kits[i] != '') {
			formatted_val = formatted_val.replace('%kit',css3_kits[i]);
		}
		if (this.style[css3_kits[i]+attr.capitalizeFirst()] != undefined) {
			this.style[css3_kits[i]+attr.capitalizeFirst()] = val;
		}
	};
};

/* fixing javascripts TERRRRRRRIBLE mod operator, from Stack Overflow user Enrique */
Number.prototype.mod = function (n) {
	return ((this % n) + n) % n;
};

String.prototype.capitalizeFirst = function() {
	return this.charAt(0).toUpperCase() + this.slice(1);
};

String.prototype.isOfCharSet = function (charSet) {
	var regex = new RegExp('['+charSet+']*');
	return this.match(regex)[0].length == this.length;
};


function Timer (fps) {
	var elapsed = 0;
	var timer_id;
	var seconds = 1000;
	var _fps = fps || 1;
	this.start = function () {
		if (timer_id == undefined) {
			timer_id = setInterval(function () {
				elapsed+=seconds/_fps;
			}, seconds/_fps);
		}
	};
	this.pause = function () {
		window.clearInterval(timer_id);
		timer_id = undefined;
	};
	this.getElapsed = function () {
		return elapsed;
	};
	this.getSecondsElapsed = function () {
		return elapsed / seconds;
	};
}


function new_elem (aux) {
	var ret_elem = document.createElement(aux.type);
	if (aux.attributes) {
		var attributes = aux.attributes;
		for (var i = 0; i < attributes.length; i++) {
			ret_elem.setAttribute(attributes[i].name,attributes[i].value);
		};
	}
	if (aux.innerHTML) ret_elem.innerHTML = aux.innerHTML;
	if (aux.children) {
		var children = aux.children;
		for (var i = 0; i < children.length; i++) {
			ret_elem.appendChild(children[i]);
		};
	}
	return ret_elem;
}

function create_user_list (data) {
	var received = data.received;
	var sent = data.sent;
	var all = received.concat(sent);
	var sorted = all.sort(function (a,b) {
		if (a.date.year != b.date.year) return a.date.year - b.date.year;
		if (a.date.month != b.date.month) return a.date.month - b.date.month;
		if (a.date.date != b.date.date) return a.date.date - b.date.date;
		if (a.date.hours != b.date.hours) return a.date.hours - b.date.hours;
		if (a.date.minutes != b.date.minutes) return a.date.minutes - b.date.minutes;
		if (a.date.seconds != b.date.seconds) return a.date.seconds - b.date.seconds;
	});
	var users_set = {}; //set
	var mapped = sorted.map(function (element) {
		var obj = {};
		if (element.to_user != undefined) {
			obj.usr = element.to_user;
		}
		if (element.from_user != undefined) {
			obj.usr = element.from_user;
		}
		obj.subject = element.subject;
		obj.was_read = element.was_read;
		return obj;
	});
	var filtered = mapped.filter(function (element) {
		if (element.usr.username in users_set) return false;
		users_set[element.usr.username] = true;
		return true;
	});
	return filtered;
}
