var ChipToken = function() {
	this.init = function() {

	}
	this.getToken = function(callback) {
		//send("POST", "/api/v1/otpToken", "token=1585053105", function(data) {
			//var dataJSON = JSON.parse(data);
			setTimeout(function() {
				callback(null);
			}, 100);
		//});
	}
}
