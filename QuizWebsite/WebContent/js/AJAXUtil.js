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
				if (xmlhttp.responseText) {
					completionHandler(JSON.parse(xmlhttp.responseText), args);
				} else {
					completionHandler({}, args);
				}
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


Node.prototype.prependChild = function(elem) {
	this.insertBefore(elem, this.firstChild);
};

var seconds = 1000;
var minutes = seconds * 60;
var hours   = minutes * 60;
var days    = hours * 24;
var weeks   = days * 7;
var months  = days * 30;
var years   = days * 365;

function get_time_ago (date) {
	var now = new Date();
	var then = new Date(date.year, date.month, date.date, date.hours, date.minutes, date.seconds, 0);


	var diff = now - then;
	if (diff > years)   return (Math.floor(diff/years) == 1)   ? 'a year ago'   : Math.floor(diff/years)   + ' years ago';
	if (diff > months)  return (Math.floor(diff/months) == 1)  ? 'a month ago'  : Math.floor(diff/months)  + ' months ago';
	if (diff > weeks)   return (Math.floor(diff/weeks) == 1)   ? 'a week ago'   : Math.floor(diff/weeks)   + ' weeks ago';
	if (diff > days)    return (Math.floor(diff/days) == 1)    ? 'a day ago'    : Math.floor(diff/days)    + ' days ago';
	if (diff > hours)   return (Math.floor(diff/hours) == 1)   ? 'an hour ago'   : Math.floor(diff/hours)   + ' hours ago';
	if (diff > minutes) return (Math.floor(diff/minutes) == 1) ? 'a minute ago' : Math.floor(diff/minutes) + ' minutes ago';
	if (diff > seconds) return (Math.floor(diff/seconds) == 1) ? 'a second ago' : Math.floor(diff/seconds) + ' seconds ago';
	return 'just now';
}

function new_elem (aux) {
	var ret_elem = document.createElement(aux.type);
	if (aux.attributes) {
		var attributes = aux.attributes;
		for (var i = 0; i < attributes.length; i++) {
			ret_elem.setAttribute(attributes[i].name,attributes[i].value);
		};
	}
	if (aux.classList) {
		var classList = aux.classList;
		for (var i = 0; i < classList.length; i++) {
			ret_elem.classList.add(classList[i]);
		};
	}
	if (aux.innerHTML) ret_elem.innerHTML = aux.innerHTML;
	if (aux.children) {
		var children = aux.children;
		for (var i = 0; i < children.length; i++) {
			ret_elem.appendChild(children[i]);
		};
	}
	if (aux.eventListeners) {
		var el = aux.eventListeners;
		for (var i = el.length - 1; i >= 0; i--) {
			ret_elem.addEventListener(el.type,el.fn);
		};
	}
	if (aux.objectAttributes) {
		var oa = aux.objectAttributes;
		for (var i = 0; i < oa.length; i++) {
			ret_elem[oa[i].name] = oa[i].value;
		};
	}
	return ret_elem;
}
