function set_maxratio(id, maxratio) {
	div = document.getElementById("set_maxratio_" + id);
	div.innerHTML +=
		"<br><form method=get action='/webtornado/cgi-bin/action.pl'>" +
		"<input type=hidden name=action value=set_maxratio>" +
		"<input type=hidden name=id value=" + id + ">" +
		"<input type=text name=maxratio value='" + maxratio + "' style='width: 50px'>" +
		"<input type=submit style='width: 30px' value='OK'>" +
		"</form>";
	div.setAttribute('onClick', '');
}

function show_files(id) {
	var div = document.getElementById('files_' + id);
	var xhr = get_xhr('/webtornado?files=' + id);
	xhr.onreadystatechange = function() {
		if (xhr.readyState != 4) { return }
		if (xhr.status != 200) { return }
		div.innerHTML = xhr.responseText;
	};
	div.innerHTML = '<img src=/webtornado/img/loading.gif>';
	div.setAttribute('onClick', '');
	xhr.send(null);			 
}

function toggle_peers(id) {
	var div = document.getElementById('peers_' + id);
	var xhr = get_xhr('/webtornado?toggle=1&peers=' + id);
	xhr.onreadystatechange = function() {
		if (xhr.readyState != 4) { return }
		if (xhr.status != 200) { return }
		div.innerHTML = xhr.responseText;
	};
	div.innerHTML = '<img src=/webtornado/img/loading.gif>';
	xhr.send(null);			 
}

function get_xhr(url) {
	var xmlhttp;
	try { xmlhttp = new ActiveXObject("Msxml2.XMLHTTP") } catch (e) {
	try { xmlhttp = new ActiveXObject("Microsoft.XMLHTTP") } catch (E) { xmlhttp = false } }
	if (!xmlhttp && typeof XMLHttpRequest!='undefined') xmlhttp = new XMLHttpRequest();
	xmlhttp.open('GET', url, true);
	return xmlhttp;
}

function onLoad() {
	var torrents = document.getElementById('torrents');
	for (var i in torrents.rows) {
		var row = torrents.rows.item(i);
		var id = row.getAttribute('wt:id');
		var fc = row.getAttribute('wt:fc');
		var mr = row.getAttribute('wt:mr');
		
		if (!id) continue;
		
		var ic = row.cells.item(0);
		for (var j in ic.getElementsByTagName('a')) {
			var c = ic.getElementsByTagName('a').item(j);
			if (!c) continue;
			if (c.getAttribute('class') == 'delete') c.setAttribute('href', '/webtornado/delete/' + id);
			if (c.getAttribute('class') == 'green') c.setAttribute('href', '/webtornado/stop/' + id);
			if (c.getAttribute('class') == 'black') c.setAttribute('href', '/webtornado/start/' + id);
		}
		
		if (!(i % 2)) row.style.cssText = 'background-color: #eeeeee';
	}
}