window.onload = function() {
	$("chipDriveButton").addEventListener("click", chipDrive);
	$("chipDriveButtonv2").addEventListener("click", chipDrivev2);
}

function chipDrive() {
	window.location.href = "/drive";
}

function chipDrivev2() {
	window.location.href = "/drive/v2";
}

function $(id) {
	return document.getElementById(id);
}