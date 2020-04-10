window.onload = function() {
	$("chipDriveButton").addEventListener("click", chipDrive);
}

function chipDrive() {
	window.location.href = "/admin";
}

function $(id) {
	return document.getElementById(id);
}