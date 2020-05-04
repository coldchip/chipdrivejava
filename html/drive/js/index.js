window.onload = function() {
	window.addEventListener("resize", resize);
	apps.init();
};

var apps = {
	login: null,
	cd: null,
	init: function() {
		var language = window.navigator.userLanguage || window.navigator.language;
		$("header").style.background = "#FFFFFF";
		resize();

		$("profile").onclick = function() {
			new Contextual({
	            isSticky: false,
	            items: [
	            	new ContextualItem({icon: theme.previewIconData, label:'Open', onClick: () => {
						// send("POST");
					}}),
					new ContextualItem({icon: "/drive/img/exit.png", label:'Sign Out', onClick: () => {
							
					}})
	            ]
	        });
		};

		$("logo").src = "drive/img/logo.png";
		$("backarrow").onclick = function() {
			mainMenu();
		}
		var load = new Loader($("frame"));
		load.show();
		load.hideBackground("none");
		var ct = new ChipToken();
		ct.init();
		ct.getToken(function(token) {
			var cd = new ChipDrive();
			apps.cd = cd;
			cd.init({
				config: "/api/v1/getDriveConfig",
				event: {
					frame:  $("frame"),
					newBtn: $("newBtn"),
					quotaBar:  $("quota"),
					quotaStatus: $("quotaText")
				}, 
				credentials: {
					token: token
				},
				API_VERSION: 1.3
			}, function(instance) {
				load.hide();
				instance.setMode("NORMAL");
				instance.getFiles();
				initSidePanel({
					0:{ 
						name:"My Drive",
						icon: "folder",
						click: function() {
							instance.getFiles();
						},
					},
					1:{
						name: "Shared With Me",
						icon: "share-alt",
						click: function() {

						},
					}
				});
				instance.getQuota((data) => {
					$("quota").style.width = data.used + "%";
					$("quotaText").innerHTML = data.usedFormat + " of " + data.totalFormat + " used";
				});
				var resizeBtn = function() {
					if(isMobile() == true) {
						$("newBtn").style.display = "none";
						instance.showNewBtn();
					} else {
						$("newBtn").style.display = "block";
						instance.hideNewBtn();
					}
				}
				resizeBtn();
				window.addEventListener("resize", resizeBtn);
			}, function(url) {
				window.location.href = url;
			});
		});
	}
};

function initSidePanel(data) {
	for(i in data) {
		var drive = document.createElement("button");
		var icon = document.createElement("i");
		icon.className = "bucketIcon fa fa-" + data[i].icon;
		drive.appendChild(icon);
		drive.className = "driveButton";
		var driveText = document.createElement("span");
		driveText.className = "bucketLabel text";
		driveText.innerHTML = data[i].name;
		drive.appendChild(driveText);
		drive.onclick = data[i].click;
		$("driveList").appendChild(drive);
	}
}

function resize() {
	if(!isMobile()) {
		$("sidePanel").style.width = "250px";
		$("sidePanel").style.borderRight = "1px solid #DDDDDD";
		$("sidePanel").style.boxShadow = "none";
		$("sidePanel").style.top = "61px";
		$("frame").style.width = "calc(100% - 250px)";
		$("frame").style.marginLeft = "250px";
		$("backarrow").style.display = "none";
		$("search").style.display = "block";
	} else {
		$("sidePanel").style.width = "0px";
		$("sidePanel").style.borderRight = "none";
		$("sidePanel").style.boxShadow = "5px 0 5px -1px #888";
		$("sidePanel").style.top = "0px";
		$("sidePanelBg").style.display = "none";
		$("frame").style.width = "calc(100% - 0px)";
		$("frame").style.marginLeft = "0px";
		$("backarrow").style.display = "block";
		$("search").style.display = "none";
	}
}

function isMobile() {
	return window.innerWidth <= 800;
}

function mainMenu() {
	var sidePanel = $("sidePanel");
	var sidePanelBg = $("sidePanelBg");
	if(sidePanel.style.width != "250px") {
		sidePanel.style.width = "250px";
		sidePanelBg.style.display = "block";
		sidePanel.addEventListener("touchmove", function(e) {
			e.preventDefault();
		});
		sidePanelBg.onclick = function() {
			sidePanel.style.width = "0px";
			sidePanelBg.style.display = "none";
		}
		sidePanelBg.addEventListener("touchmove", function(e) {
			e.preventDefault();
			sidePanel.style.width = "0px";
			sidePanelBg.style.display = "none";
		});
	} else {
		sidePanel.style.width = "0px";
		sidePanelBg.style.display = "none";
	}
}
