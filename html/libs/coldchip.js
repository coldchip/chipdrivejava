var coldchip = true;

function $(id) {
	return document.getElementById(id);
}

function $$(item) {
	return document.createElement(item);
}

Element.prototype.remove = function() {
    this.parentElement.removeChild(this);
}

NodeList.prototype.remove = HTMLCollection.prototype.remove = function() {
    for(var i = this.length - 1; i >= 0; i--) {
        if(this[i] && this[i].parentElement) {
            this[i].parentElement.removeChild(this[i]);
        }
    }
}

function Panel(bind) {
	this.text;
	this.progress;
	this.bind = bind;
	this.show = function(data) {
		this.elem = document.createElement("div");
		this.elem.className = "panelBackground";
		var panel = document.createElement("div");
			panel.className = "panel";
		var XButtonHTML = '<svg width="29" height="29" style="vertical-align: middle;"><path d="M20.13 8.11l-5.61 5.61-5.6-5.61-.81.8 5.61 5.61-5.61 5.61.8.8 5.61-5.6 5.61 5.6.8-.8-5.6-5.6 5.6-5.62" fill-rule="evenodd"></path></svg>';
		var XButton = document.createElement("button");
			XButton.className = "panelCancelButton";
		addHTML(XButton, XButtonHTML);
		panel.appendChild(XButton);
		panel.appendChild(data);
		this.elem.appendChild(panel);
		this.bind.appendChild(this.elem);
		var that = this;
		XButton.onclick = function() {
			that.bind.removeChild(that.elem);
		};
	};
	this.hide = function() {
		this.bind.removeChild(this.elem);
	};

	this.addHTML = function(el, html) {
		var c = document.createElement("div");
		c.innerHTML = html;
		while (c.children.length > 0) {
			el.appendChild(c.children[0]);
		}
	};
}

function popup() {
	this.popup;
	that = this;
	this.bindAlert = function(text, callback) {
		that.close();
		var popup = document.createElement("div");
		that.popup = popup; 
		popup.id = "popup";
		popup.className = "popup";
		popup.style.height = "100%";
		popup.style.width = "100%";
		popup.style.position = "absolute";
		popup.style.zIndex = "999";
		popup.style.top = "0";
		popup.style.left = "0";
		popup.style.overflow = "hidden";
		popup.style.wordWrap = "break-word";
		popup.addEventListener("touchmove", function(e) {
			if(e.target == popup) {
				e.preventDefault();
			}
		});
		popup.style.backgroundColor = "rgba(60, 60, 60, 0.5)";
		var txt = document.createElement("p");
			txt.style.display = "block";
			txt.style.textAlign = "left";
			txt.style.margin = "10px 0px 20px 15px";
			txt.style.fontWeight = "500";
			txt.style.fontSize = "18px";
			txt.className = "text";
			txt.innerHTML = text;
		var btn = document.createElement("div");
			btn.innerHTML = "OKAY";
			btn.style.display = "inline-block";
			btn.style.margin = "auto auto";
			btn.style.float = "right";
			btn.style.margin = "50px 15px 10px auto";
			btn.style.fontSize = "0.9em";
			btn.style.background = "#FFFFFF";
			btn.style.fontWeight = "600";
			btn.style.color = "#106cc8";
			btn.className = "text";
			btn.onclick = function() {
				document.body.removeChild(popup);
				callback();
			}
		var div = document.createElement("div");
		div.appendChild(txt);
		div.appendChild(btn);
		var popupInner = document.createElement("div");
			popupInner.style.animationName = "fadeIn";
			popupInner.style.animationDuration = "0.4s";
			popupInner.style.position = "relative";
			popupInner.style.width = "300px";
			popupInner.style.height = "auto";
			popupInner.style.margin = "50px auto";
			popupInner.style.padding = "10px";
			popupInner.style.background = "#fff";
			popupInner.style.overflow = "scroll";
			popupInner.style.boxShadow = "0 7px 8px -4px rgba(0,0,0,.2), 0 13px 19px 2px rgba(0,0,0,.14), 0 5px 24px 4px rgba(0,0,0,.12)";
			popupInner.style.borderRadius = "4px";
			popupInner.appendChild(div);
		popup.appendChild(popupInner);
		document.body.appendChild(popup);
	};
	this.bindConfirm = function(text, callback) {
		that.close();
		var popup = document.createElement("div");
		that.popup = popup; 
		popup.id = "popup";
		popup.className = "popup";
		popup.style.height = "100%";
		popup.style.width = "100%";
		popup.style.position = "absolute";
		popup.style.borderRadius = "4px";
		popup.style.zIndex = "999";
		popup.style.top = "0";
		popup.style.left = "0";
		popup.style.overflow = "hidden";
		popup.style.wordWrap = "break-word";
		popup.addEventListener("touchmove", function(e) {
			if(e.target == popup) {
				e.preventDefault();
			}
		});
		popup.style.backgroundColor = "rgba(60, 60, 60, 0.5)";
		var txt = document.createElement("p");
			txt.style.display = "block";
			txt.style.textAlign = "left";
			txt.style.margin = "10px 0px 20px 15px";
			txt.style.fontWeight = "500";
			txt.style.fontSize = "18px";
			txt.className = "text";
			txt.innerHTML = text;
		var cancel = document.createElement("div");
			cancel.innerHTML = "CANCEL";
			cancel.style.display = "inline-block";
			cancel.style.margin = "50px auto 10px 75px";
			//cancel.style.width = "40%";
			//cancel.style.height = "40px";
			cancel.style.fontSize = "0.9em";
			cancel.style.background = "#FFFFFF";
			cancel.style.fontWeight = "600";
			cancel.style.color = "#106cc8";
			cancel.className = "text";
			cancel.onclick = function() {
				document.body.removeChild(popup);
			}
		var btn = document.createElement("div");
			btn.innerHTML = "OK";
			btn.style.display = "inline-block";
			btn.style.margin = "auto auto";
			btn.style.float = "right";
			btn.style.margin = "50px 15px 10px auto";
			btn.style.fontSize = "0.9em";
			btn.style.background = "#FFFFFF";
			btn.style.fontWeight = "600";
			btn.style.color = "#106cc8";
			btn.className = "text";
			btn.onclick = function() {
				document.body.removeChild(popup);
				callback();
			}
		var div = document.createElement("div");
		div.appendChild(txt);
		div.appendChild(cancel);
		div.appendChild(btn);
		var popupInner = document.createElement("div");
			popupInner.style.animationName = "fadeIn";
			popupInner.style.animationDuration = "0.4s";
			popupInner.style.position = "relative";
			popupInner.style.width = "300px";
			popupInner.style.height = "auto";
			popupInner.style.margin = "50px auto";
			popupInner.style.padding = "10px";
			popupInner.style.background = "#fff";
			popupInner.style.overflow = "scroll";
			popupInner.style.boxShadow = "0 7px 8px -4px rgba(0,0,0,.2), 0 13px 19px 2px rgba(0,0,0,.14), 0 5px 24px 4px rgba(0,0,0,.12)";
			popupInner.style.borderRadius = "4px";
			popupInner.appendChild(div);
		popup.appendChild(popupInner);
		document.body.appendChild(popup);
	};
	this.bindPrompt = function(text, button, callback, cancelCallback) {
		that.close();
		var popup = document.createElement("div");
		that.popup = popup; 
		popup.id = "popup";
		popup.className = "popup";
		popup.style.height = "100%";
		popup.style.width = "100%";
		popup.style.position = "absolute";
		popup.style.borderRadius = "4px";
		popup.style.zIndex = "999";
		popup.style.top = "0";
		popup.style.left = "0";
		popup.style.overflow = "hidden";
		popup.style.wordWrap = "break-word";
		popup.addEventListener("touchmove", function(e) {
			if(e.target == popup) {
				e.preventDefault();
			}
		});
		popup.style.backgroundColor = "rgba(60, 60, 60, 0.5)";
		var txt = document.createElement("p");
			txt.style.display = "block";
			txt.style.textAlign = "left";
			txt.style.margin = "10px 0px 20px 15px";
			txt.style.fontWeight = "500";
			txt.style.fontSize = "18px";
			txt.className = "text";
			txt.innerHTML = text;
		var input = document.createElement("input");
			input.setAttribute("type", "text");
			input.style.display = "block";
			input.style.margin = "auto auto";
			input.style.fontSize = "1.1em";
			input.style.border = "none";
			input.style.borderBottom = "2px solid #7D7D7D";
			input.style.fontWeight = "500";
			input.style.color = "#7D7D7D";
			input.style.outline = "none";
			input.style.width = "90%";
			input.style.height = "35px";
			input.style.borderRadius = "0px";
			input.className = "text";
			input.style.boxSizing = "border-box";
		var cancel = document.createElement("div");
			cancel.innerHTML = "CANCEL";
			cancel.style.display = "inline-block";
			cancel.style.margin = "50px auto 10px 75px";
			//cancel.style.width = "40%";
			//cancel.style.height = "40px";
			cancel.style.fontSize = "0.9em";
			cancel.style.background = "#FFFFFF";
			cancel.style.fontWeight = "600";
			cancel.style.color = "#106cc8";
			cancel.className = "text";
			cancel.onclick = function() {
				if(cancelCallback) {
					cancelCallback();
				}
				document.body.removeChild(popup);
			}
		var btn = document.createElement("div");
			btn.innerHTML = button ? button : "Okay";
			btn.style.display = "inline-block";
			btn.style.margin = "auto auto";
			btn.style.float = "right";
			btn.style.margin = "50px 15px 10px auto";
			//btn.style.width = "40%";
			//btn.style.height = "40px";
			btn.style.fontSize = "0.9em";
			btn.style.background = "#FFFFFF";
			btn.style.fontWeight = "600";
			btn.style.color = "#106cc8";
			btn.className = "text";
			btn.onclick = function() {
				document.body.removeChild(popup);
				callback(input.value);
			}
		var div = document.createElement("div");
		div.appendChild(txt);
		div.appendChild(input);
		div.appendChild(cancel);
		div.appendChild(btn);
		var popupInner = document.createElement("div");
			popupInner.style.animationName = "fadeIn";
			popupInner.style.animationDuration = "0.4s";
			popupInner.style.position = "relative";
			popupInner.style.width = "300px";
			popupInner.style.height = "auto";
			popupInner.style.margin = "50px auto";
			popupInner.style.padding = "10px";
			popupInner.style.background = "#fff";
			popupInner.style.overflow = "scroll";
			popupInner.style.boxShadow = "0 7px 8px -4px rgba(0,0,0,.2), 0 13px 19px 2px rgba(0,0,0,.14), 0 5px 24px 4px rgba(0,0,0,.12)";
			popupInner.style.borderRadius = "4px";
			popupInner.appendChild(div);
		popup.appendChild(popupInner);
		document.body.appendChild(popup);
		input.focus();
	}
	this.close = function() {
		try {
			document.body.removeChild(that.popup);
		} catch(e) {
			try {
				document.body.removeChild(document.getElementById("popup"));
			} catch(e) {

			}
		}
	}
}

function Loader(bind) {
	this.elem;
	this.text;
	this.progress;
	this.bind = bind;
	this.show = function(data, color) {
		var bg = document.createElement("div");
			bg.style.width = "100%";
			bg.style.height = "100%";
			bg.style.position = "absolute";
			bg.style.zIndex = "999";
			bg.style.top = "0px";
			bg.style.left = "0px";
			bg.style.overflow = "hidden";
			bg.style.backgroundColor = "rgba(60, 60, 60, 0.6)";
		var ajaxHTML = '<div style="margin: 150px; text-align: center;"><svg style="margin: auto auto; animation: ajaxloadSpin 2s linear infinite;" width="50" height="50" viewBox="0 0 44 44"><circle style="stroke: #acb9bf;stroke-dasharray: 1,150; stroke-dashoffset: 0; stroke-linecap: round; -webkit-animation: ajaxload 1.5s ease-in-out infinite; animation: ajaxload 1.5s ease-in-out infinite;" cx="22" cy="22" r="20" fill="none" stroke-width="4"></circle></svg></div>';
		var text = document.createElement("p");
			this.text = text;
			text.innerHTML = data ? data : "";
			text.className = "text";
			text.style.marginTop = "20px";
			text.style.color = color ? color : "#FFFFFF";
		addHTML(bg, ajaxHTML);
		bg.appendChild(text);
		this.elem = bg;
		if(this.bind) {
			this.bind.appendChild(bg);
		} else {
			document.body.appendChild(bg);
		}
		
	};
	this.hideBackground = function(color) {
		this.elem.style.background = color;
	};
	this.changeText = function(text) {
		this.text.innerHTML = text;
	};
	this.changeProgress = function(value) {
		this.progress.style.width = value + "%";
	}
	this.hide = function() {
		if(this.bind) {
			this.bind.removeChild(this.elem);
		} else {
			document.body.removeChild(this.elem);
		}
	};
}

function addHTML(el, html) {
	var c = document.createElement("div");
	c.innerHTML = html;
	while (c.children.length > 0)
		el.appendChild(c.children[0]);
}

function $(id) {
	return document.getElementById(id);
}

function getQuery(name) {
    url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return results[2].replace(/\+/g, ' ');
}

function send(method, path, data, callback, error) {
    var http = new XMLHttpRequest();
    http.open(method, path, true);
    http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    http.send(data);
    http.onreadystatechange = function()
    {
        if(this.readyState == 4)
        {
        	if(this.status == 200) {
            	callback(http.responseText);
        	} else {
        		error("Server responded with error " + this.status);
        	}
        }
    };
    http.onerror = function(e) {
    	error(e);
    }
}

class Contextual{
    /**
     * Creates a new contextual menu
     * @param {object} opts options which build the menu e.g. position and items
     * @param {boolean} opts.isSticky sets how the menu apears, follow the mouse or sticky
     * @param {Array<ContextualItem>} opts.items sets the default items in the menu
     */
    constructor(opts){   

    	var that = this;

        contextualCore.CloseMenu();

        this.position = opts.isSticky != null ? opts.isSticky : false;
        this.menuControl = contextualCore.CreateEl(`
            <ul class='contextualJs contextualMenu'>

            </ul>`);
        opts.items.forEach(i => {
            this.menuControl.appendChild(i.element);
        });
            
        if(event != undefined){
            event.stopPropagation()
            document.body.appendChild(this.menuControl);
            contextualCore.PositionMenu(this.position, event, this.menuControl);   
        }

        document.onclick = function(e){
            if(!e.target.classList.contains('contextualJs')){
                contextualCore.CloseMenu();
            }
        }    
    }
    /**
     * Adds item to this contextual menu instance
     * @param {ContextualItem} item item to add to the contextual menu
     */
    add(item){
        this.menuControl.appendChild(item);
    }
    /**
     * Makes this contextual menu visible
     */
    show(){
        event.stopPropagation()
        document.body.appendChild(this.menuControl);
        contextualCore.PositionMenu(this.position, event, this.menuControl);    
    }
    /**
     * Hides this contextual menu
     */
    hide(){
        event.stopPropagation()
        contextualCore.CloseMenu();
    }
    /**
     * Toggle visibility of menu
     */
    toggle(){
        event.stopPropagation()
        if(this.menuControl.parentElement != document.body){
            document.body.appendChild(this.menuControl);
            contextualCore.PositionMenu(this.position, event, this.menuControl);        
        }else{
            contextualCore.CloseMenu();
        }
    }
}  
class ContextualItem{
    /**
     * 
     * @param {Object} opts
     * @param {string} [opts.label] 
     * @param {string} [opts.type]
     * @param {Array<ContextualItem>} [opts.submenu]
     * @param {string} [opts.icon]
     * @param {string} [opts.shortcut]
     * @param {void} [opts.onClick] 
     * @param {string} [opts.custom]
     */
    constructor(opts){
        switch(opts.type){
            case 'seperator':
                this.element = contextualCore.CreateEl(`<li class='contextualJs contextualMenuSeperator'><div></div></li>`);
                break;
            case 'custom':
                this.element = contextualCore.CreateEl( `
                    <li class='contextualJs'>
                        <div class='contextualJs contextualMenuItem'>
                            <img src='${opts.icon == undefined? '' : opts.icon}' class='contextualJs contextualMenuItemIcon'/>
                            <span class='contextualJs contextualMenuItemTitle text'>${opts.label == undefined? 'No label' : opts.label}</span>
                            <span class='contextualJs contextualMenuItemOverflow submenu'>
                                <span class='contextualJs contextualMenuItemOverflowLine'></span>
                                <span class='contextualJs contextualMenuItemOverflowLine'></span>
                                <span class='contextualJs contextualMenuItemOverflowLine'></span>
                            </span>
                            <span class='contextualJs contextualMenuItemTip text'>${opts.shortcut == undefined? '' : opts.shortcut}</span>
                        </div>
                        <ul class='contextualJs contextualSubMenu contextualMenuHidden'>
                            <li class="contextualJs contextualHeader">
                                <input type='button' value='<' class='contextualJs'/>
                                <span class='contextualJs'>${opts.label == undefined? 'No label' : opts.label}</span>
                            </li>
                            <li class='contextualJs contextualCustomEl'>
                            </li>
                        </ul>
                    </li>`); 

                    let elOuter = document.querySelector('.contextualCustomEl');


                break;
            case 'submenu':
            case 'normal':
            default:
                this.element = contextualCore.CreateEl( `
                    <li class='contextualJs'>
                        <div class='contextualJs contextualMenuItem'>
                            <img src='${opts.icon == undefined? '' : opts.icon}' class='contextualJs contextualMenuItemIcon'/>
                            <span class='contextualJs contextualMenuItemTitle text'>${opts.label == undefined? 'No label' : opts.label}</span>
                            <span class='contextualJs contextualMenuItemOverflow ${opts.type === 'submenu'? '' : 'hidden'}'>
                                <span class='contextualJs contextualMenuItemOverflowLine'></span>
                                <span class='contextualJs contextualMenuItemOverflowLine'></span>
                                <span class='contextualJs contextualMenuItemOverflowLine'></span>
                            </span>
                            <span class='contextualJs contextualMenuItemTip'>${opts.shortcut == undefined? '' : opts.shortcut}</span>
                        </div>
                        <ul class='contextualJs contextualSubMenu contextualMenuHidden'>
                            <li class="contextualJs contextualHeader">
                                <input type='button' value='<' class='contextualJs contextualSubMenuClose'/>
                                <span class='contextualJs text'>${opts.label == undefined? 'No label' : opts.label}</span>
                            </li>
                        </ul>
                    </li>`);           

                let childMenu = this.element.querySelector('.contextualSubMenu'),
                    menuItem = this.element.querySelector('.contextualMenuItem');

                if(opts.submenu !== undefined){                    
                    opts.submenu.forEach(i => {
                        childMenu.appendChild(i.element);
                    });
                                
                    menuItem.addEventListener('click',() => {
                        menuItem.classList.add('SubMenuActive');
                        childMenu.classList.remove('contextualMenuHidden');
                    });

                    childMenu.querySelector('.contextualSubMenuClose').addEventListener('click',() => {
                        menuItem.classList.remove('SubMenuActive');
                        childMenu.classList.add('contextualMenuHidden');
                    });
                }else{
                    childMenu.parentElement.removeChild(childMenu);
                    this.element.addEventListener('click', () => {
                        event.stopPropagation();
                        if(opts.onClick !== undefined){ opts.onClick(); }  
                        contextualCore.CloseMenu();
                    });
                }     
        }
    }
}

const contextualCore = {
    PositionMenu: (docked, el, menu) => {
        if(docked){
            menu.style.left = ((el.target.offsetLeft + menu.offsetWidth) >= window.innerWidth) ? 
                ((el.target.offsetLeft - menu.offsetWidth) + el.target.offsetWidth)+"px"
                    : (el.target.offsetLeft)+"px";

            menu.style.top = ((el.target.offsetTop + menu.offsetHeight) >= window.innerHeight) ?
                (el.target.offsetTop - menu.offsetHeight)+"px"    
                    : (el.target.offsetHeight + el.target.offsetTop)+"px";
        }else{
            menu.style.left = ((el.clientX + menu.offsetWidth) >= window.innerWidth) ?
                ((el.clientX - menu.offsetWidth))+"px"
                    : (el.clientX)+"px";

            menu.style.top = ((el.clientY + menu.offsetHeight) >= window.innerHeight) ?
                (el.clientY - menu.offsetHeight)+"px"    
                    : (el.clientY)+"px";
        }
    },
    CloseMenu: () => {
        let openMenuItem = document.querySelector('.contextualMenu:not(.contextualMenuHidden)');
        if(openMenuItem != null) { 
        	document.body.removeChild(openMenuItem); 
        }      
    },
    CreateEl: (template) => {
        var el = document.createElement('div');
        el.innerHTML = template;
        return el.firstElementChild;
    }
};