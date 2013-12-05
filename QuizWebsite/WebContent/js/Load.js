var loader;

function Loader () {
	var _container;
	var _interval;
	var _main_drawing;

	var SECOND = 1000;
	var FPS = 30;

	this.setContainer = function (container) {
		if (_container) _container.innerHTML = '';
		_container = container;
	}
	this.start = function () {
		var canvas = new_elem({
			type:'canvas'
		});
		_main_drawing = new Drawing(canvas,400, 200);
		_container.appendChild(_main_drawing.canvas);
		this.resume();
	}
	this.stop = function () {
		this.pause();
		_container.innerHTML = '';
	}
	this.resume = function () {
		_interval = setInterval(function () {
			_main_drawing.draw();
		}, SECOND/FPS);
	}
	this.pause = function () {
		if (_interval) window.clearInterval(_interval);
	}

}

function Drawing (canvas, width, height) {
	this.canvas  = canvas;
	this.context = this.canvas.getContext('2d');
	this.canvas.width = width;
	this.canvas.height = height;
	this.center = {'x':this.canvas.width/2, 'y':this.canvas.height/2};

	this.swirl = new Swirl(this);

	this.draw = function () {
		if (this.context) {
			this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
			this.swirl.draw();
		} else {
			console.log('no context')
		}
	}
}

function Swirl (drawing) {
	this.drawing = drawing;
	this.leader = {'x':0, 'y':0};

	this.rad = 0;   //radians defining movement of swirl
	this.d_rad = Math.PI/30; //change in radians
	this.approach_rate = 4;

	this.deviation = {'x':this.drawing.center.x*0.7,'y':this.drawing.center.y*0.7};

	this.tail_len = 10 + 1;
	this.points = [new Point(this.tail_len, this.leader.x, this.leader.y, this.drawing)];
	for (var i = this.tail_len - 1; i > 0; i--) {
		this.points.push(new Point(i, this.leader.x, this.leader.y, this.drawing));
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