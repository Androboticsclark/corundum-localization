var blnLoadComplete = false;
var intSystemCursorX = 0;
var intSystemCursorY = 0;
var intSystemKeyTID = -1;
var intSystemKeyInterval = 100;
var intSystemLastKey = -1;
var intSystemLastMouse = -1;
var intSystemMouseTID = -1;
var intSystemMouseInterval = 100;
var intSystemScrollTID = -1;
var intSystemScrollInterval = 10;

function autoScrollToTop() {
	var top = getScrollTop();
	if (top <= 0) {
		if (intSystemScrollTID >= 0) intSystemScrollTID = clearIntervalEx(intSystemScrollTID);
	} else {
		if (intSystemScrollTID < 0) intSystemScrollTID = setInterval(autoScrollToTop, intSystemScrollInterval);
		setScrollTop(Math.floor(top / 1.3));
	}
}

function clearIntervalEx(intID) {
	if (intID >= 0) clearInterval(intID);
	return -1;
}

function clearTimeoutEx(intID) {
	if (intID >= 0) clearTimeout(intID);
	return -1;
}

function getBodyHeight() {
	if (document.body.innerHeight != undefined) return document.body.innerHeight;
	return -1;
}

function getBodyWidth() {
	if (document.body.innerWidth != undefined) return document.body.innerWidth;
	return -1;
}

function getScrollLeft() {
	if (window.pageXOffset != undefined) return window.pageXOffset;
	if (document.compatMode == "CSS1Compat") return document.body.parentNode.scrollLeft;
	if (document.body.scrollLeft != undefined) return document.body.scrollLeft;
	return -1;
}

function getScrollTop() {
	if (window.pageYOffset != undefined) return window.pageYOffset;
	if (document.compatMode == "CSS1Compat") return document.body.parentNode.scrollTop;
	if (document.body.scrollTop != undefined) return document.body.scrollTop;
	return -1;
}

function getWindowHeight() {
	if (window.innerHeight != undefined) return window.innerHeight;
	if (document.compatMode == "CSS1Compat") return document.body.parentNode.clientHeight;
	if (document.body.clientHeight != undefined) return document.body.clientHeight;
	return -1;
}

function getWindowWidth() {
	if (window.innerWidth != undefined) return window.innerWidth;
	if (document.compatMode == "CSS1Compat") return document.body.parentNode.clientWidth;
	if (document.body.clientWidth != undefined) return document.body.clientWidth;
	return -1;
}

function initKeyDown(event) {
	if (event == undefined && window.event != undefined) event = window.event;
	var intKey = event.keyCode;
	if (intSystemLastKey == intKey) return true;
	try {onKeyDown(intKey);} catch(ex) {}
	intSystemLastKey = intKey;
	intSystemKeyTID = clearIntervalEx(intSystemKeyTID);
	intSystemKeyTID = setInterval(initKeyPress, intSystemKeyInterval);
	initKeyPress();
	return true;
}

function initKeyPress() {
	try {onKeyPress(intSystemLastKey);} catch(ex) {}
}

function initKeyUp(event) {
	if (event == undefined && window.event != undefined) event = window.event;
	var intKey = event.keyCode;
	try {onKeyUp(intKey);} catch(ex) {}
	intSystemLastKey = -1;
	intSystemKeyTID = clearIntervalEx(intSystemKeyTID);
	return true;
}

function initLoad() {
	blnLoadComplete = true;
	try {onLoad();} catch(ex) {}
	return true;
}

function initMouseDown(event) {
	if (event == undefined && window.event != undefined) event = window.event;
	var intMouse = event.button;
	intSystemCursorX = event.clientX;
	intSystemCursorY = event.clientY;
	try {onMouseDown(intMouse, event.clientX, event.clientY);} catch(ex) {}
	intSystemLastMouse = intMouse;
	intSystemMouseTID = clearIntervalEx(intSystemMouseTID);
	intSystemMouseTID = setInterval(initMousePress, intSystemMouseInterval);
	initMousePress();
	return true;
}

function initMouseMove(event) {
	if (event == undefined && window.event != undefined) event = window.event;
	intSystemCursorX = event.clientX;
	intSystemCursorY = event.clientY;
	try {onMouseMove(intSystemLastMouse, event.clientX, event.clientY);} catch(ex) {}
	return true;
}

function initMousePress() {
	try {onMousePress(intSystemLastMouse, intSystemCursorX, intSystemCursorY);} catch(ex) {}
}

function initMouseUp(event) {
	if (event == undefined && window.event != undefined) event = window.event;
	var intMouse = event.button;
	intSystemCursorX = event.clientX;
	intSystemCursorY = event.clientY;
	try {onMouseUp(intMouse, event.clientX, event.clientY);} catch(ex) {}
	intSystemLastMouse = -1;
	intSystemMouseTID = clearIntervalEx(intSystemMouseTID);
	return true;
}

function initResize() {
	try {onResize();} catch(ex) {}
}

function initScroll() {
	try {onScroll();} catch(ex) {}
}

function setScrollLeft(value) {
	if (document.compatMode == "CSS1Compat") return document.body.parentNode.scrollLeft = value;
	if (document.body.scrollLeft != undefined) return document.body.scrollLeft = value;
	return -1;
}

function setScrollTop(value) {
	if (document.compatMode == "CSS1Compat") return document.body.parentNode.scrollTop = value;
	if (document.body.scrollTop != undefined) return document.body.scrollTop = value;
	return -1;
}

try {document.onkeydown = initKeyDown;} catch(ex) {}
try {document.onkeyup = initKeyUp;} catch(ex) {}
try {document.onmousedown = initMouseDown;} catch(ex) {}
try {document.onmousemove = initMouseMove;} catch(ex) {}
try {document.onmouseup = initMouseUp;} catch(ex) {}
try {document.onresize = initResize;} catch(ex) {}
try {document.onscroll = initScroll;} catch(ex) {}
try {window.onload = initLoad;} catch(ex) {}
