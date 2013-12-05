var loader;

function Loader () {
	var DEFAULT_WIDTH = 400;
	var DEFAULT_HEIGHT = 200;
	var SECOND = 1000;
	var FPS = 30;

	var _canvas;
	var _container;
	var _interval;
	var _main_drawing;
	var _width = DEFAULT_WIDTH;
	var _height = DEFAULT_HEIGHT;

	this.setContainer = function (container) {
		if (_canvas) _canvas.classList.add('hide');
		_container = container;
	};
	this.setWidth = function (width) {
		_width = width
	};
	this.setHeight = function (height) {
		_height = height;
	};
	this.setDimensions = function (width,height) {
		this.setWidth(width);
		this.setHeight(height);
	};
	this.start = function (prepend) {
		_canvas = new_elem({
			type:'canvas',
			classList:['load-canvas']
		});
		var dim = min(_width,_height);
		var d_dim = min(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		var scale = dim/d_dim;
		_main_drawing = new Drawing(_canvas,_width, _height,scale);
		if (prepend) _container.prependChild(_main_drawing.canvas);
		else _container.appendChild(_main_drawing.canvas);
		this.resume();
	};
	this.stop = function () {
		this.pause();
		if (_canvas) _canvas.classList.add('hide');
	};
	this.resume = function () {
		_interval = setInterval(function () {
			_main_drawing.draw();
		}, SECOND/FPS);
	};
	this.pause = function () {
		if (_interval) window.clearInterval(_interval);
	};

}

function Drawing (canvas, width, height, scale) {
	this.canvas  = canvas;
	this.context = this.canvas.getContext('2d');
	this.canvas.width = width;
	this.canvas.height = height;
	this.center = {'x':this.canvas.width/2, 'y':this.canvas.height/2};

	this.swirl = new Swirl(this, scale);

	this.draw = function () {
		if (this.context) {
			this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
			this.swirl.draw();
		} else {
			console.log('no context')
		}
	}
}

function Swirl (drawing, scale) {
	var _scale = scale;

	this.drawing = drawing;
	this.leader = {'x':0, 'y':0};

	this.rad = 0;   //radians defining movement of swirl
	this.d_rad = Math.PI/30; //change in radians
	this.approach_rate = 4;

	this.deviation = {'x':this.drawing.center.x*0.7,'y':this.drawing.center.y*0.7};

	this.tail_len = 10 + 1;
	this.points = [new Point(get_size(this.tail_len), this.leader.x, this.leader.y, this.drawing)];
	for (var i = this.tail_len - 1; i > 0; i--) {
		this.points.push(new Point(get_size(i), this.leader.x, this.leader.y, this.drawing));
	};

	this.update_rad = function () {
		this.rad += this.d_rad;
		this.rad %= Math.PI * 2;
	}

	this.update_leader_pos = function () {
		this.update_rad();
		this.points[0].x = this.drawing.center.x + (Math.sin(this.rad))*this.deviation.x;
		this.points[0].y =  this.drawing.center.y + (Math.sin(this.rad*2))*this.deviation.y;
	}

	this.update_points = function () {
		for (var i = 1; i < this.points.length; i++) {
			var prev_pt_diff  = {'x':this.points[i-1].x - this.points[i].x, 'y':this.points[i-1].y - this.points[i].y};
			this.points[i].x += prev_pt_diff.x / this.approach_rate;
			this.points[i].y += prev_pt_diff.y / this.approach_rate;
		};
	}

	this.draw = function () {
		this.update_leader_pos();
		this.update_points();
		for (var i = 0; i < this.points.length; i++) {
			this.points[i].draw();
		};
	}

	function get_size (position) {
		return position * _scale;
	}
}


function Point (radius, x, y, drawing) {
	this.radius = radius;
	this.x = x;
	this.y = y;
	this.drawing = drawing;

	this.draw = function () {
		this.drawing.context.beginPath();
		this.drawing.context.arc(this.x, this.y, this.radius, 0, 2*Math.PI);
		this.drawing.context.fill();
	}
}




(function init_loader () {
	loader = new Loader();
	loader.setContainer(document.getElementById('loader-container'));
})();