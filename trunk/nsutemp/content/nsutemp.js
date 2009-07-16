function xhr(url, onSuccess) {
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.open('GET', url, true);
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState != 4) { return }
		if (xmlhttp.status != 200) { return }
		onSuccess(xmlhttp);
	};
	xmlhttp.send(null);
}

function setText(text) {
	var wm = Components.classes["@mozilla.org/appshell/window-mediator;1"].getService(Components.interfaces.nsIWindowMediator);
	var enumerator = wm.getEnumerator('navigator:browser');
	while (enumerator.hasMoreElements()) {
		var status = enumerator.getNext().document.getElementById('NT-statusbar-label');
		if (status) {
			status.value = text;
		}
	}
};

function getTemp() {
	setText('loading');
	xhr('http://chronos.nsu.ru/tv/graph/loadata.php', function(rq) {
		var r = new RegExp("cnv.innerHTML = '(.+?)&deg;C'");
		var m = r.exec(rq.responseText);
		if (!m) {
			setText('shit happened');
			return;
		}
		setText(m[1]);
	});
}

function getTempT() {
	getTemp();
	setTimeout(getTempT, 900 * 1000);
}

window.addEventListener('load', getTempT, false);