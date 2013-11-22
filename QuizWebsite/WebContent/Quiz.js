/**
 * 
 */
function print_all_mc () {
	for (var i = 0; i < 20; i++) {
		var mc = document.getElementsByName('mult-c-option-'+i);
		for (var n = 0; n < mc.length; n++) {
			if(mc[n].checked) console.log(mc[n].value);
		};
	};
}
var wrapper_elem = document.getElementById('quiz-prompt-content');
var ui_handler;
function init_js(quiz_id) {
	resize_app();
	window.onresize = resize_app;
	ui_handler = new UIHandler(new QuizHandler(quiz_id, "/QuizWebsite/QuizServlet"),
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
			if (_q_handler.isMultiPage()) build_multipage(elem);
			else build_singlepage(elem);
		}, {});
	};

	this.start_test = function () {
		if (_q_handler.isMultiPage()) {
			enable_arrow_keys();
			_this.next();
		} else {
			add_singlepage_questions();
		}
	};

	this.next = function () {
		if (_q_handler.isMultiPage()) {
			var prev_it = _iterator;
			if (_q_handler.indexExists(++_iterator)) {
				/* inform quiz data recieved if legitamate question index */
				cycle_cards();
				load_mid(_iterator+1);
			} else if (_active) {
				_active = false;
				cycle_cards();
				/* load front card */
				var results_wrapper = document.getElementsByClassName('front')[0];
				/* load results into front card */
				this.finish_test(results_wrapper);
			}
		}
	};

	this.finish_test = function (wrapper_elem) {
		wrapper_elem.innerHTML = 'loading results...';
		_q_handler.waitForResults(function(elem, aux){
			aux.wrp_elem.innerHTML = '';
			aux.wrp_elem.appendChild(elem);
		}, {wrp_elem:wrapper_elem});
	};

	/* private methods */
	function build_multipage (elem) {
		add_card_deck();
		/* load front card */
		var info_wrapper = document.getElementsByClassName('front')[0];
		info_wrapper.innerHTML = "";
		info_wrapper.appendChild(elem);

		/* load middle card */
		load_mid(0);
	}
	function add_singlepage_questions () {
		_card_wrapper.innerHTML = '';
		var disp_card = document.createElement('div');
		_card_wrapper.appendChild(disp_card);
		disp_card.classList.add('card','front','tall');

		var header_title = document.createElement('h1');
		header_title.innerHTML = _q_handler.getQuizName();
		header_title.classList.add('center','space-under');
		disp_card.appendChild(header_title);
		for (var i = 0; _q_handler.indexExists(i); i++) {
			var new_elem = document.createElement('div');
			load_content(i, new_elem);
			new_elem.classList.add('space-under');
			disp_card.appendChild(new_elem);
		}
		var finish_button = document.createElement('input');
		finish_button.type = 'button';
		finish_button.value = 'Submit Quiz!';
		finish_button.classList.add('center-block','space-under');
		finish_button.addEventListener('click', function(){
			_this.finish_test(disp_card);
		});
		disp_card.appendChild(finish_button);
	}
	function build_singlepage (elem) {
		var disp_card = document.createElement('div');
		_card_wrapper.appendChild(disp_card);
		disp_card.classList.add('card','front','tall');
		var welcome_div = document.createElement('div');
		welcome_div.appendChild(elem);
		disp_card.appendChild(welcome_div);
		//add submit button
		
	}
	function add_card_deck () {
		var num_back_cards = 3;
		for (var i = 0; i < num_back_cards; i++) {
			var new_card = document.createElement('div');
			random_rotation(new_card);
			new_card.classList.add('card','index','back');
			_card_wrapper.appendChild(new_card);
		}
		var mid_card = document.createElement('div');
		mid_card.classList.add('card','index','middle');
		_card_wrapper.appendChild(mid_card);
		var front_card = document.createElement('div');
		front_card.classList.add('card','index','front');
		_card_wrapper.appendChild(front_card);
	}
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
		var card_classes = [['back'  ],
							['middle'],
							['front']
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
				random_rotation(elem);
				flip(elem);
			}

		}
		var middle = document.createElement('div');
		middle.classList.add('card','index','middle','tilt-align');
		_card_wrapper.appendChild(middle);
	}
	function random_rotation (elem) {
		var rot = (Math.random() * 8) - 4;
		elem.setCSS3Attr('Transform','rotateZ('+ rot +'deg)');
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
	                    	if(e && e.stopPropagation) {
	                    		e.stopPropagation();
	                    	} else {
	                    		e = window.event;
	                    		e.cancelBubble = true;
	                    	}
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


Node.prototype.attach_enter_listener = function(fn) {
	console.log('attaching');
	this.addEventListener('keydown', function (e) {
		switch (e.keyCode) {
			case (13):
				fn();
				if(e && e.stopPropagation) {
					e.stopPropagation();
				} else {
					e = window.event;
					e.cancelBubble = true;
				}
		}
	})
};



