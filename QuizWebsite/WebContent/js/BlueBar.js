var open_message_pane;

(function init_blue_bar () {
	var bbrm = new BlueBarRadioMenu(document.getElementById('user_id_stash').getAttribute('user_id'));
	open_message_pane = function (user_id) {
		bbrm.toUserModal(user_id);
	}
})();

function BlueBarRadioMenu (user_id) {
	var _user_id = user_id;
	var _this = this;
	var _radio_menu_types = [
		{id:'requests-button',holder_id:'requests-button-holder',handler:RequestsHandler},
		{id:'messages-button',holder_id:'messages-button-holder',handler:MessagesHandler},
		{id:'notifications-button',holder_id:'notifications-button-holder',handler:NotificationsHandler},
		{id:'settings-button',holder_id:'settings-button-holder',handler:SettingsHandler}
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
			handler.holder = document.getElementById(type.holder_id);
			handler.disp = get_display();
			handler.holder.appendChild(handler.disp);
			rg.push(handler);
		};
	}

	this.update = function (handler) {
		var ul = handler.disp.menu.ul;
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

	this.toUserModal = function (user_id) {
		
	}

	function get_display () {
		var menu = get_menu();
		var modal = get_modal();
		var div = new_elem({
			type:'div',
			classList:['blue-bar-display','hide'],
			children:[menu,modal]
		});
		div.menu = menu;
		div.modal = modal;
		return div;
	}

	function get_menu () {
		var div = document.createElement('div');
		div.classList.add('blue-bar-menu');
		var ul = document.createElement('ul');
		div.ul = ul;
		div.appendChild(ul);
		return div;
	}

	function get_modal () {
		var div = new_elem({type:'div'});
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
				if (handler.disp.classList.contains('hide')) {
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
			if (_group[i] !== handler) _group[i].disp.classList.add('hide');
			else _group[i].disp.classList.remove('hide');
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
		var usr = _data.user_list[index];
		var div = new_elem({
			type:'div',
			innerHTML:usr.first_name + ' ' + usr.last_name
		});
		var li = new_elem({
			type:'li',
			children:[div]
		});
		return li;
	}

	this.modalAtIndex = function (index) {
		
	}

	/* messages specific */
	this.modalAtUser = function (user_id) {
		var found = -1;
		for (var i = 0; i < _data.user_list.length; i++) {
			if (user_id == _data.user_list[i].id) {
				found = i;
				break;
			}
		};
		if (found === -1) {
			_data.user_list.splice(0,0,{id:user_id});
			found = 0;
		}
		return modalAtIndex(found);
	}

	this.modalAtIndexExists = function (index) {
		return this.indexExists(index);
	}

	this.indexExists = function (index) {
		return (index < _data.user_list.length);
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