function Rating (settings) {

	var _wrapper = settings.wrapper;
	var _rated_fill = settings.rated_fill || 'rgb(200,200,200)';
	var _unrated_fill = settings.unrated_fill || 'rgb(240,240,0)';
	var _width = settings.width || 'inherit';
	var _height = settings.height || 'inherit';

	this.rating_drawing;

	function init () {
		var canvas = new_elem({
			type:'canvas'
		});
		_wrapper.appendChild(canvas);
		this.rating_drawing = new RatingDrawing(canvas);
	}

	init();
}


function RatingDrawing (canvas, width, height, scale) {
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
