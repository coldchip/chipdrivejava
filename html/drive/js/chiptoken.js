var ChipToken = function() {
	this.init = function() {

	}
	this.getToken = function(callback) {
			setTimeout(function() {
				callback(null);
			}, 100);
	}
}
