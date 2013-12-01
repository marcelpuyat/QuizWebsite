(function init_blue_bar () {
	new BlueBarRadioMenu('blue-dropdown-radio');
})();

function BlueBarRadioMenu (className) {
	var _className = className;
	var _this = this;
	var _radio_menu_types = [
		{id:'requests-button',handler:RequestsHandler},
		{id:'messages-button',handler:MessagesHandler},
		{id:'notifications-button',handler:NotificationsHandler},
		{id:'settings-button',handler:SettingsHandler}
	];
	(function init () {
		init_radio_menus();
	})();

	function init_radio_menus () {
		var rg = new RadioGroup(_this);
		for (var i = 0; i < _radio_menu_types.length; i++) {
			var type = _radio_menu_types[i];
			var handler = new type.handler(_this);
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

function RadioGroup (blue_bar) {
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

function RequestsHandler (blue_bar) {
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

function MessagesHandler (blue_bar) {
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

function NotificationsHandler (blue_bar) {
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

function SettingsHandler (blue_bar) {
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