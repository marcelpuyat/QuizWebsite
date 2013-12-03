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
	var _handlers = {};
	var _radio_menu_types = [
		{name:'requests',id:'requests-button',holder_id:'requests-button-holder',handler:RequestsHandler},
		{name:'messages',id:'messages-button',holder_id:'messages-button-holder',handler:MessagesHandler},
		{name:'notifications',id:'notifications-button',holder_id:'notifications-button-holder',handler:NotificationsHandler},
		{name:'settings',id:'settings-button',holder_id:'settings-button-holder',handler:SettingsHandler}
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
			handler.disp = get_display(handler);
			handler.holder.appendChild(handler.disp);
			rg.push(handler);
			_handlers[type.name] = handler;
		};
	}

	this.update = function (handler) {
		var ul = handler.disp.menu.ul;
		ul.innerHTML = '';
		var i = 0;
		var ul_name = handler.getUlName() || ">";
		ul.appendChild(new_elem({type:'li',classList:['modal-header'],innerHTML:ul_name}));
		while (true) {
			if (handler.indexExists(i)) {
				var new_li = handler.liAtIndex(i);
				ul.appendChild(new_li);
				new_li.classList.add('pointable');
				if (handler.modalAtIndexExists(i)) {
					new_li.addEventListener('click',function () {
						_this.toModalAtIndex(i,handler);
					});
				}
				i++;
			} else {
				break;
			}
		}
	}

	this.toUserModal = function (user_id) {
		var handler = _handlers.messages;
		show_modal(handler.modalAtUser(user_id));
	}

	this.toModalAtIndex = function (index,handler) {
		show_modal(handler.modalAtIndex(index), handler);
	}

	function show_modal (elem, handler) {
		handler.disp.modal.m_body.innerHTML = '';
		handler.disp.modal.m_body.appendChild(elem);
		handler.disp.modal.classList.add('open');
	}
	function hide_modal (handler) {
		handler.disp.modal.classList.remove('open');
	}

	function get_display (handler) {
		var menu = get_menu();
		var modal = get_modal(handler);
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

	function get_modal (handler) {
		var modal_header = new_elem({
			type:'li',
			classList:['modal-header','pointable'],
			innerHTML:'<'
		});
		var modal_body = new_elem({
			type:'li',
			classList:['modal-body']
		});
		var ul = new_elem({
			type:'ul',
			classList:['blue-bar-modal'],
			children:[modal_header,modal_body]
		});
		ul.header = modal_header;
		ul.m_body = modal_body;

		modal_header.addEventListener('click',function () {
			hide_modal(handler);
		});

		return ul;
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

	this.getUlName = function () {
		return "";
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
				_data.user_list = create_user_list(data.messages);
				console.log(_data);
				_blue_bar.update(_this);
			}
		);
		
	}

	this.getUlName = function () {
		return "Messages";
	}

	this.liAtIndex = function (index) {
		var usr = _data.user_list[index];
		console.log('this');
		console.log(usr);
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
		var div = new_elem({
			type:'div',
			innerHTML:'hi'
		});
		return div;
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

	function messages_by_uid (messages) {
		
	}

	function create_user_list (messages) {
		var users_set = {}; //set
		var filtered = messages.filter(function (element) {
			if (element.user.id in users_set) return false;
			users_set[element.user.id] = true;
			return true;
		});
		var mapped = filtered.map(function (element) {
			return element.user;
		});
		return mapped;
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

	this.getUlName = function () {
		return "";
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

	this.getUlName = function () {
		return "";
	}

	this.liAtIndex = function (index) {
		
	}

	this.modalAtIndex = function (index) {
		
	}

	this.indexExists = function (index) {
		return false;
	}
}