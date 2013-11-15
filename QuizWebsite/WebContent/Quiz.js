/**
 * 
 */
var wrapper_elem = document.getElementById('quiz-prompt-content');
function init_js(quiz_id) {
	resize_app();
	window.onresize = resize_app;
	var ui_handler = new UIHandler(new QuizHandler(quiz_id, "/QuizWebsite/QuizServletStub"),
						document.getElementById('quiz-cards-wrapper'));
	ui_handler.run();
}

function UIHandler (quiz_handler, card_wrapper) {
	/* private vars */
	var _q_handler = quiz_handler;
	var _iterator = -1;//question index corresponding to front page. start on intro page
	var _active = true;
	var _this = this;
	var _card_wrapper = card_wrapper;

	/* public methods */
	this.run = function () {
		_q_handler.addEventListener('start-quiz',this.start_test);
		_q_handler.waitForLoad(function(elem, aux){
			/* load front card */
			var info_wrapper = document.getElementsByClassName('front')[0];
			info_wrapper.innerHTML = "";
			info_wrapper.appendChild(elem);

			/* load middle card */
			load_mid(0);
		}, {});
	};

	this.start_test = function () {
		enable_arrow_keys();
		_this.next();
	}

	this.next = function () {
		var prev_it = _iterator;
		if (_q_handler.indexExists(++_iterator)) {
			/* inform quiz data recieved if legitamate question index */
			if (_iterator >= 0) {
				_q_handler.informQuestionAnsweredAtIndex(_iterator);
			}
			cycle_cards();
			load_mid(_iterator+1);
		} else if (_active) {
			_active = false;
			cycle_cards();
			/* load front card */
			var results_wrapper = document.getElementsByClassName('front')[0];
			/* load results into front card */
			results_wrapper.innerHTML = 'loading results...';
			_q_handler.waitForResults(function(elem, aux){
				aux.wrp_elem.innerHTML = '';
				aux.wrp_elem.appendChild(elem);
			}, {wrp_elem:results_wrapper});
		}
	};

	/* private methods */
	function load_mid (index) {
		var mid_wrapper  = document.getElementsByClassName('middle')[0];
		var success = load_content(index, mid_wrapper);
	}
	function load_content (index, target_elem) {
		if (_q_handler && _q_handler.isLoaded()) {
			var elem_contents = _q_handler.getAtIndex(index);
			if (elem_contents) {
				target_elem.innerHTML = '';
				target_elem.appendChild(elem_contents);
				return true;
			} else {
				return false;
			}
		}
	}
	/* Steps:
	   > middle promoted to front
	   > front demoted to back
	   > create new middle
	*/
	function cycle_cards () {
		/* get all card elems, turn into standard array */
		var card_classes = [['back'  ],//'tilt-right'],
							['middle'],//'tilt-left' ],
							['front']// ,'tilt-align']
							];
		var cards = Array.prototype.slice.call(document.getElementsByClassName('card'));
		for (var i = 0; i < cards.length; i++){
			var elem = cards[i];
			if (match_classes(elem, ['middle'])) {
				elem.classList.remove('middle');
				elem.classList.add('front');
			} else if (match_classes(elem, ['front'])) {
				elem.classList.remove('front');
				elem.classList.add('back');
				var rot = (Math.random() * 8) - 4;
				elem.setCSS3Attr('Transform','rotateZ('+ rot +'deg)');
				flip(elem);
			}

		}
		var middle = document.createElement('div');
		middle.classList.add('card');
		middle.classList.add('middle');
		middle.classList.add('tilt-align');
		_card_wrapper.appendChild(middle);
	}
	function flip (elem) {
		elem.classList.add('flipping');
		setTimeout(function () {
			elem.classList.remove('flipping');
		}, 0.5 * 1000);
	}
	function match_classes(elem, class_match) {
		for (var i = 0; i < class_match.length; i++) {
			if (!elem.classList.contains(class_match[i])) return false;
		}
		return true;
	}
	function enable_arrow_keys () {
		document.onkeydown = function (e) {
			e = e || event;
			switch (e.keyCode) {
	                    case 37: break; //left
	                    case 38: break;
	                    case 39://right
	                    	_this.next();
	                    	break;
	                    case 40: break;
	                }
		}
	}
}


function resize_app() {
	document.getElementById('app-wrapper').style.width = 
		document.getElementById('app-selection-wrapper').parentElement.clientWidth 
		- document.getElementById('app-selection-wrapper').clientWidth - 10 + "px";
}

/* fixing javascripts TERRRRRRRIBLE mod operator, from Stack Overflow user Enrique */
Number.prototype.mod = function (n) {
	return ((this % n) + n) % n;
}

String.prototype.capitalizeFirst = function() {
	return this.charAt(0).toUpperCase() + this.slice(1);
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

