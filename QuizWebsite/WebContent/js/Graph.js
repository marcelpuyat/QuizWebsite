/**
 * 
 */
new GraphHandler(document.getElementById('graph-search-bar'),
				document.getElementById('graph-search-results'),
				 '/QuizWebsite/GraphServlet');

function GraphHandler (graph_search_bar, graph_search_ul, graph_url) {
	var _bar_elem = graph_search_bar;
	var _graph_search_ul = graph_search_ul;
	var _graph_url = graph_url;
	(function init () {
		_bar_elem.addEventListener('keyup',function () {
			var query = _bar_elem.value;
			get_json_from_url(_graph_url + '?query='+query, function (data) {
				update_display(query,data);
			});
		});
		window.addEventListener('click',function (e) {
			_graph_search_ul.innerHTML = '';
		});
		_bar_elem.addEventListener('click', function () {
			if 	(_graph_search_ul.children.length == 0) {
				get_json_from_url(_graph_url + '?query=', function (data) {
					update_display('',data);
				});
			} else {
				_bar_elem.innerHTML = '';
			}
		});
	})();

	function update_display (query, data) {
		_graph_search_ul.innerHTML = '';
		var results = data.results;
		if (results) {
			for (var i = 0; i < results.length; i++) {
				_graph_search_ul.appendChild(
					get_display_li(
						{href:results[i].url,
						text:format_result(query, results[i].name, results[i].category),
						type:results[i].type}
					)
				);
			};
		}
	}

	function format_result (query_text, result, category) {
		if (!query_text || query_text == '') return result;
		if (category == 'tag') return result;
		var result_lwr = result.toLowerCase();
		var query_lwr  = query_text.toLowerCase();

		var start = result_lwr.search(query_lwr);
		var end = start + query_lwr.length;
		return result.substring(0,start)+ '<b>' + result.substring(start,end) + '</b>' + result.substring(end);
	}

	function get_display_li (content) {
		var li = document.createElement('li');
		li.classList.add('graph-search-result');

		var type = document.createElement('span');
		type.innerHTML = content.type;

		var anchor = document.createElement('a');
		anchor.href = content.href;
		anchor.innerHTML = content.text;

		li.appendChild(type);
		li.appendChild(anchor);
		return li;
	}
}