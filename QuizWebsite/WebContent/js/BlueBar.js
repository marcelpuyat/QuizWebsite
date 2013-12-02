(function init_blue_bar () {
	new BlueBarRadioMenu(document.getElementById('user_id_stash').getAttribute('user_id'));
})();

function BlueBarRadioMenu (user_id) {
	var _user_id = user_id;
	var _this = this;
	var _radio_menu_types = [
		{id:'requests-button',handler:RequestsHandler},
		{id:'messages-button',handler:MessagesHandler},
		{id:'notifications-button',handler:NotificationsHandler},
		{id:'settings-button',handler:SettingsHandler}
	];
	(function init () {
		if (_user_id != -1) {
			init_radio_menus();
		}
	})();

	function init_radio_menus () {
		var rg = new RadioGroup(_this);
		for (var i = 0; i < _radio_menu_types.length; i++) {
			var type = _radio_menu_types[i];
			var handler = new type.handler(_this, _user_id);
			handler.button = document.getElementById(type.id);
			handler.menu = get_menu();
			handler.button.appendChild(handler.menu);
			rg.push(handler);
		};
	}

	this.update = function (handler) {
		var ul = handler.menu.ul;
		ul.innerHTML = '';
		var i = 0;
		while (true) {
			if (handler.indexExists(i)) {
				ul.appendChild(handler.liAtIndex(i));
				i++;
			} else {
				break;
			}
		}
	}

	function get_menu () {
		var div = document.createElement('div');
		div.classList.add('hide','blue-bar-menu');
		var ul = document.createElement('ul');
		div.ul = ul;
		div.appendChild(ul);
		return div;
	}
}

function RadioGroup (blue_bar, user_id) {
	var _user_id = user_id;
	var _group = [];
	var _this = this;
	var _blue_bar = blue_bar;

	this.push = function (handler) {
		_group.push(handler);
		handler.button.addEventListener('click',
			function () {
				if (handler.menu.classList.contains('hide')) {
					_this.closeOthers(handler);
					handler.refresh();
				} else {
					_this.closeOthers();
				}
				
			}
		);
	}

	this.closeOthers = function (handler) {
		console.log('close others');
		console.log(handler);
		for (var i = 0; i < _group.length; i++) {
			if (_group[i] !== handler) _group[i].menu.classList.add('hide');
			else _group[i].menu.classList.remove('hide');
		};
	}
}

function RequestsHandler (blue_bar, user_id) {
	var _user_id = user_id;
	var _blue_bar = blue_bar;
	var _data;
	var _this = this;

	this.refresh = function () {
		get_json_from_url(
			'',
			function (data) {
				_data = data;
				_blue_bar.update(_this);
			}
		);
		
	}

	this.liAtIndex = function (index) {
		var li = document.createElement('li');
		li.innerHTML = index;
		return li;
	}

	this.modalAtIndex = function (index) {
		
	}

	this.indexExists = function (index) {
		return (index < 5);
	}
}

function MessagesHandler (blue_bar, user_id) {
	var _user_id = user_id;
	var _blue_bar = blue_bar;
	var _data;
	var _this = this;

	this.refresh = function () {
		get_json_from_url(
			'/QuizWebsite/MessageServlet?user_id='+_user_id,
			function (data) {
				console.log('here');
				console.log(data);
				_data = data;
				_data.user_list = create_user_list(data);
				console.log(_data);
				_blue_bar.update(_this);
			}
		);
		
	}

	this.liAtIndex = function (index) {
		/* compose message */
		if (index == 0) {
			var li = document.createElement('li');
			li.innerHTML = 'New Message';
			return li;
		} else {
			var li = document.createElement('li');
			var user = _data.user_list[index + 1];
			li.innerHTML = user.first_name + ' ' + user.last_name;
			return li;
		}
	}

	this.modalAtIndex = function (index) {
		
	}

	this.indexExists = function (index) {
		return (index <= _data.user_list.length);
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
			if (element.to_user != undefined) return element.to_user;
			if (element.from_user != undefined) return element.from_user;
			return -1;
		})
		var filtered = mapped.filter(function (element) {
			if (element.username in users_set) return false;
			users[element.username] = true;
			return true;
		});
		return filtered;
	}
}

function NotificationsHandler (blue_bar, user_id) {
	var _user_id = user_id;
	var _blue_bar = blue_bar;
	var _data;
	var _this = this;

	this.refresh = function () {
		get_json_from_url(
			'',
			function (data) {
				_data = data;
				_blue_bar.update(_this);
			}
		);
		
	}

	this.liAtIndex = function (index) {
		var li = document.createElement('li');
		li.innerHTML = index;
		return li;
	}

	this.modalAtIndex = function (index) {
		
	}

	this.indexExists = function (index) {
		return (index < 5);
	}
}

function SettingsHandler (blue_bar, user_id) {
	var _user_id = user_id;
	var _blue_bar = blue_bar;
	var _data;
	var _this = this;

	this.refresh = function () {
		get_json_from_url(
			'',
			function (data) {
				_data = data;
				_blue_bar.update(_this);
			}
		);
		
	}

	this.liAtIndex = function (index) {
		
	}

	this.modalAtIndex = function (index) {
		
	}

	this.indexExists = function (index) {
		return false;
	}
}