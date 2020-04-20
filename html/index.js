window.onload = function() {
	$("chipDriveButton").addEventListener("click", chipDrive);
}

function chipDrive() {
	window.location.href = "/drive";
}

function $(id) {
	return document.getElementById(id);
}