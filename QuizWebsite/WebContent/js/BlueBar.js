var open_message_pane;

(function init_blue_bar () {
	var bbrm = new BlueBarRadioMenu(document.getElementById('user_id_stash').getAttribute('user_id'));
	open_message_pane = function (user) {
		console.log(user);
		bbrm.toUserModal(user);
	}
})();

function BlueBarRadioMenu (user_id) {
	var _user_id = user_id;
	var _this = this;
	var _handlers = {};
	var _rg;
	var _radio_menu_types = [
		{name:'requests',id:'requests-button',holder_id:'requests-button-holder',handler:RequestsHandler},
		{name:'messages',id:'messages-button',holder_id:'messages-button-holder',handler:MessagesHandler},
		{name:'notifications',id:'notifications-button',holder_id:'notifications-button-holder',handler:NotificationsHandler},
		{name:'settings',id:'settings-button',holder_id:'settings-button-holder',handler:SettingsHandler}
	];
	function init () {
		if (_user_id != -1) {
			init_radio_menus();
		}
	}

	function init_radio_menus () {
		_rg = new RadioGroup(_this);
		for (var i = 0; i < _radio_menu_types.length; i++) {
			var type = _radio_menu_types[i];
			var handler = new type.handler(_this, _user_id);
			handler.button = document.getElementById(type.id);
			handler.holder = document.getElementById(type.holder_id);
			handler.disp = get_display(handler);
			handler.holder.appendChild(handler.disp);
			handler.holder.appendChild(getNib());
			handler.notifications = getNotificationElem();
			handler.holder.appendChild(handler.notifications);
			handler.refresh();
			_rg.push(handler);
			_handlers[type.name] = handler;
		};
	}

	this.update = function (handler) {
		console.log('notifications');
		console.log(handler.getNumNotifications());
		_this.updateNotifications(handler);
		var ul = handler.disp.menu.ul;
		ul.innerHTML = '';
		var i = 0;
		var ul_name = handler.getUlName() || "";
		ul.appendChild(new_elem({type:'li',classList:['modal-header'],innerHTML:ul_name}));
		var lis = [];
		while (true) {
			if (handler.indexExists(i)) {
				var new_li = handler.liAtIndex(i);
				new_li.classList.add('menu-li')
				lis.push(new_li);
				new_li.i = i;
				
				if (handler.modalAtIndexExists(i)) {
					new_li.classList.add('pointable');
					new_li.appendChild(new_elem({
						type:'span',
						innerHTML:'&rsaquo;',
						classList:['right-modal-indicator']
					}));
					new_li.addEventListener('click',function () {
						var index = this.i;
						_this.toModalAtIndex(index,handler);
					});
				}
				i++;
			} else {
				break;
			}
		}
		var table_ul = new_elem({
			type:'ul',
			children:lis,
			classList:['table-view']
		});
		ul.appendChild(new_elem({
			type:'li',
			children:[table_ul]
		}));
	}

	this.updateNotifications = function (handler) {
		if (handler.notifications && handler.notifications.update) handler.notifications.update(handler.getNumNotifications());
	}

	this.toUserModal = function (user) {
		var handler = _handlers.messages;
		handler.refresh(function () {
			_rg.closeOthers(handler);
			var index = handler.userIndex(user);
			_this.toModalAtIndex(index,handler);
		});
	}

	this.toModalAtIndex = function (index,handler) {
		var header_title = (handler.modalAtTitleIndex) ? handler.modalAtTitleIndex(index) : "";
		show_modal(handler.modalAtIndex(index), handler, header_title);
	}

	function show_modal (elem, handler, header_title) {
		handler.disp.modal.m_body.innerHTML = '';
		handler.disp.modal.m_body.appendChild(elem);
		handler.disp.modal.classList.add('open');
		handler.disp.modal.header.title_disp.innerHTML = header_title;
		if (elem.afterLoad) setTimeout(elem.afterLoad,200);
	}

	function hide_modal (handler) {
		handler.disp.modal.classList.remove('open');
	}

	function getNotificationElem () {
		var elem = new_elem({
			type:'div',
			classList:['hide','notifications-elem']
		});
		elem.update = function (num_notifications) {
			if (num_notifications) {
				elem.classList.remove('hide');
				elem.innerHTML = num_notifications;
			} else {
				elem.classList.add('hide');
			}
		}
		return elem;
	}

	function getNib () {
		return new_elem({
			type:'span',
			classList:['menu-nib']
		});
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
		var modal_header_back = new_elem({
			type:'span',
			classList:['modal-go-back-button'],
			innerHTML:'&lsaquo;'
		});
		var modal_header_disp = new_elem({
			type:'span',
			classList:['modal-header-title']
		});
		var modal_header = new_elem({
			type:'li',
			classList:['modal-header','pointable'],
			children:[modal_header_back,modal_header_disp]
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
		ul.header.title_disp = modal_header_disp;

		modal_header_back.addEventListener('click',function () {
			hide_modal(handler);
		});
		modal_header.addEventListener('click', function () {
			if (handler.modalTopPressed) handler.modalTopPressed();
		})

		return ul;
	}

	init();
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

	this.getNumNotifications = function () {
		return (_data) ? _data.requests.length : 0;
	}

	this.refresh = function () {
		get_json_from_url(
			'/QuizWebsite/RelationServlet?action=requests&user_id='+_user_id,
			function (data) {
				_data = data;
				_blue_bar.update(_this);
			}
		);
		
	}

	this.getUlName = function () {
		return "Friend Requests";
	}

	this.liAtIndex = function (index) {
		var target_user_id = _data.requests[index].id;
		var name = new_elem({
			type:'span',
			innerHTML:_data.requests[index].first_name + ' ' + _data.requests[index].last_name
		});
		var accept = new_elem({
			type:'span',
			innerHTML:'accept',
			classList:['button','green','small','request-button']
		});
		var decline = new_elem({
			type:'span',
			innerHTML:'decline',
			classList:['button','red','small','request-button']
		});
		decline.addEventListener('click',
			function () {
				post_json_to_url(
					"/QuizWebsite/RelationServlet?user_a_id=" + _user_id + "&user_b_id=" + target_user_id + "&status=NO",
					{},
					function (data) {
						if (data.status == 'success') {
							_data.requests.splice(index,1); //remove from array
							accept.classList.add('hide');
							decline.classList.add('hide');
							_blue_bar.update(_this);
						}
					}
				);
			}
		);
		accept.addEventListener('click',
			function () {
				post_json_to_url(
					"/QuizWebsite/RelationServlet?user_a_id=" + _user_id + "&user_b_id=" + target_user_id + "&status=FRD",
					{},
					function (data) {
						if (data.status == 'success') {
							accept.classList.add('hide');
							decline.classList.add('hide');
						}
					}
				);
			}
		);
		return new_elem({
			type:'li',
			children:[name, accept, decline]
		});
	}

	this.modalAtIndex = function (index) {
		return false;
	}


	this.modalAtIndexExists = function (index) {
		return false;
	}

	this.indexExists = function (index) {
		return (index < _data.requests.length);
	}
}

function MessagesHandler (blue_bar, user_id) {
	var _user_id = user_id;
	var _blue_bar = blue_bar;
	var _data;
	var _this = this;
	var _modal_user_id;
	var _modal_messages_ul;

	this.getNumNotifications = function () {
		return (_data && _data.user_list) ? _data.user_list.reduce(function (previous,current,index,arr) {
			return previous + (current.unread ? 1 : 0);
		},0) : 0;
	}

	this.refresh = function (callback) {
		var callbackcalled = false;
		if (_data) {
			_blue_bar.update(_this);
			if (callback) callback();
			callbackcalled = true;
		}
		console.log('messages requested');
		get_json_from_url(
			'/QuizWebsite/MessageServlet?user_id='+_user_id,
			function (data) {
				console.log('messages loaded');
				_data = data;
				_data.user_list = create_user_list(data.messages);
				console.log('messages processed');
				console.log(_data);
				_blue_bar.update(_this);
				if (callback && !callbackcalled) callback();
			}
		);
		
	}

	this.getUlName = function () {
		return "Messages";
	}

	this.modalTopPressed = function () {
		if (_modal_messages_ul && _modal_messages_ul.firstChild) {
			_modal_messages_ul.firstChild.scrollIntoView(false);
		}
	}

	this.liAtIndex = function (index) {
		var usr = _data.user_list[index];
		var div = new_elem({
			type:'div',
			innerHTML:(usr.display_name && usr.display_name != '') ? usr.display_name : (usr.first_name + ' ' + usr.last_name)
		});
		var li = new_elem({
			type:'li',
			children:[div]
		});
		return li;
	}

	this.modalAtTitleIndex = function (index) {
		if (_data.user_list[index].display_name && _data.user_list[index].display_name != "") return _data.user_list[index].display_name;
		return _data.user_list[index].first_name + ' ' + _data.user_list[index].last_name;
	}

	this.modalAtIndex = function (index) {
		_modal_messages_ul = new_elem({
			type:'ul',
			classList:['chat-ul']
		});
		_modal_user_id = _data.user_list[index].id;
		var user_messages = messages_by_uid(_data.user_list[index].id, _data.messages);
		inform_messages_read(user_messages,_data.user_list[index]);
		for (var i = 0; i < user_messages.length; i++) {
			var body = new_elem({
				type:'div',
				classList:['message-body'],
				innerHTML:user_messages[i].body,
				attributes:[
					{name:'title',value:get_time_ago(user_messages[i].date)}
				]
			});
			_modal_messages_ul.prependChild(new_elem({
				type:'li',
				classList:['chat-message',user_messages[i].type],
				children:[body]
			}));
		};
		var messages_li = new_elem({
			type:'li',
			children:[_modal_messages_ul]
		})
		var compose_li = new_elem({
			type:'li',
			children:[get_compose_elem()]
		})
		var modal_ul = new_elem({
			type:'ul',
			classList:['messages-modal-ul'],
			children:[messages_li,compose_li]
		});
		modal_ul.afterLoad = function () {
			var modal_children = _modal_messages_ul.children;
			for (var i = 0; i < modal_children.length; i++) {
				modal_children[i].style.height = modal_children[i].firstChild.clientHeight + 'px';
			};
			if (_modal_messages_ul.lastChild) _modal_messages_ul.lastChild.scrollIntoView(false);
		};
		return modal_ul;
	}

	/* messages specific */
	this.userIndex = function (user) {
		var found = -1;
		for (var i = 0; i < _data.user_list.length; i++) {
			if (user.id == _data.user_list[i].id) {
				found = i;
				break;
			}
		};
		if (found === -1) {
			_data.user_list.splice(0,0,user);
			found = 0;
		}
		return found;
	}

	this.modalAtIndexExists = function (index) {
		return this.indexExists(index);
	}

	this.indexExists = function (index) {
		return (index < _data.user_list.length);
	}


	function messages_by_uid (user_id, messages) {
		return messages.filter(function (message) {
			return (message.user.id == user_id);
		});
	}

	function inform_messages_read (messages,user_list_user) {
		for (var i = 0; i < messages.length; i++) {
			if (messages[i].type == 'received' && !messages[i].was_read) {
				post_json_to_url(
					'/QuizWebsite/MessageServlet?action=read&message_id='+messages[i].message_id,
					{}
				);
			}
		};
		user_list_user.unread = false;
		_blue_bar.updateNotifications(_this);
	}

	function get_compose_elem () {
		var composition_input = new_elem({
			type:'input',
			attributes:[
				{name:'type',value:'text'}
			],
			classList:['chat-composition-input']
		});
		var send_button = new_elem({
			type:'div',
			innerHTML:'Send',
			classList:['chat-send-button','pointable']
		});
		send_button.addEventListener('click',function () {
			var message_body = composition_input.value;
			var send_to = _modal_user_id;
			post_json_to_url(
				'/QuizWebsite/MessageServlet?action=send',
				{
					user_from_id:_user_id,
					user_to_id:send_to,
					subject:'',
					body:message_body
				},
				function () {
					var body = new_elem({
						type:'div',
						classList:['message-body'],
						innerHTML:message_body
					});
					var new_li = new_elem({
						type:'li',
						classList:['chat-message','sent'],
						children:[body]
					});

					_modal_messages_ul.appendChild(new_li);
					new_li.scrollIntoView();

					composition_input.value = '';
				}
			);
		});
		return new_elem({
			type:'div',
			classList:['chat-composition-wrapper'],
			children:[composition_input,send_button]
		});
	}

	function create_user_list (messages) {
		var users_set = {}; //set
		var filtered = messages.filter(function (element) {
			if (element.user.id in users_set) return false;
			users_set[element.user.id] = true;
			return true;
		});
		var mapped = filtered.map(function (element) {
			var obj = element.user;
			obj.unread = (element.type == 'received' && !element.was_read)
			return obj;
		});
		return mapped;
	}

}

function NotificationsHandler (blue_bar, user_id) {
	var _user_id = user_id;
	var _blue_bar = blue_bar;
	var _data;
	var _this = this;

	this.getNumNotifications = function () {
		return 0;
	}

	this.refresh = function () {
		get_json_from_url(
			'/QuizWebsite/AnnouncementServlet',
			function (data) {
				_data = data;
				console.log('Announcement');
				console.log(data);
				_blue_bar.update(_this);
			}
		);
		
	}

	this.getUlName = function () {
		return "Announcements";
	}

	this.liAtIndex = function (index) {
		var admin = new_elem({
			type:'span',
			classList:['faint','notifications-username'],
			innerHTML: _data.announcements[index].admin.display_name
		});
		var subject = new_elem({
			type:'span',
			classList:[],
			innerHTML: _data.announcements[index].subject
		});
		return new_elem({
			type:'li',
			children:[admin, subject]
		});
	}

	this.modalAtIndex = function (index) {
		var title = new_elem({
			type:'h3',
			classList:['no-margin'],
			innerHTML:_data.announcements[index].subject
		});
		var body = new_elem({
			type:'p',
			classList:['no-margin'],
			innerHTML:_data.announcements[index].body
		});

		return new_elem({
			type:'div',
			children:[title, body]
		});
	}

	this.modalAtIndexExists = function (index) {
		return this.indexExists(index);
	}

	this.indexExists = function (index) {
		return (index < _data.announcements.length);
	}
}

function SettingsHandler (blue_bar, user_id) {
	var _user_id = user_id;
	var _blue_bar = blue_bar;
	var _this = this;

	var _settings_items = [getLogoutItem,getAccountSettings];

	this.getNumNotifications = function () {
		return 0;
	}

	this.refresh = function () {
		_blue_bar.update(_this);
	}//static

	this.getUlName = function () {
		return "Settings";
	}

	this.liAtIndex = function (index) {
		return _settings_items[index]();
	}

	this.modalAtIndex = function (index) {
		return false;
	}

	this.modalAtIndexExists = function (index) {
		return false;
	}

	this.indexExists = function (index) {
		return index < _settings_items.length;
	}

	function getAccountSettings () {
		var li = new_elem({
			type:'li',
			innerHTML:'Account Settings',
			classList:['pointable']
		});
		li.addEventListener('click', function () {
			window.location = '/QuizWebsite/Settings.jsp';
		});
		return li;
	}

	function getLogoutItem () {
		var li = new_elem({
			type:'li',
			innerHTML:'logout',
			classList:['pointable']
		});
		li.addEventListener('click', function () {
			var logout = window.confirm('Are you sure you\'d like to logout?');
			if (logout){
				post_json_to_url(
					'/QuizWebsite/UserServlet?api=logout',
					{},
					function (data) {
						if (data.status = 'success') {
							window.location = '/QuizWebsite/Login.jsp';
						}
					}
				);
			}
		});
		return li;
	}
}