function set_maxratio(id, maxratio) {
	div = document.getElementById("set_maxratio_" + id);
	div.innerHTML +=
		"<br><form method=get action='/webtornado/cgi-bin/action.pl'>" +
		"<input type=hidden name=action value=set_maxratio>" +
		"<input type=hidden name=id value=" + id + ">" +
		"<input type=text name=maxratio value='" + maxratio + "' style='width: 50px'>" +
		"<input type=submit style='width: 30px' value='OK'>" +
		"</form>";
	div.id += '_';
}

function show_files(id) {
	var div = document.getElementById('files_' + id);
	var xhr = get_xhr();
	xhr.open('GET', '/webtornado?files=' + id, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState != 4) { return }
		if (xhr.status != 200) { return }
		div.innerHTML = xhr.responseText;
	};
	div.innerHTML = '[loading]';
	div.setAttribute('onClick', '');
	xhr.send(null);			 
}

function show_peers(id) {
	var div = document.getElementById('peers_' + id);
	var xhr = get_xhr();
	xhr.open('GET', '/webtornado?peers=' + id, true);
	var c = div.innerHTML;
	xhr.onreadystatechange = function() {
		if (xhr.readyState != 4) { return }
		if (xhr.status != 200) { return }
		div.style.cssText = 'text-align: left;';
		div.innerHTML = xhr.responseText;
		div.setAttribute('onClick', 'hide_peers(' + id + ')');
	};
	div.innerHTML = '<font color=gray>[loading]</font>';
	div.setAttribute('onClick', '');
	xhr.send(null);			 
}

function hide_peers(id) {
	var div = document.getElementById('peers_' + id);
	var xhr = get_xhr();
	xhr.open('GET', '/webtornado?hide_peers=' + id, true);
	var c = div.innerHTML;
	xhr.onreadystatechange = function() {
		if (xhr.readyState != 4) { return }
		if (xhr.status != 200) { return }
		div.innerHTML = xhr.responseText;
		div.setAttribute('onClick', 'show_peers(' + id + ')');
	};
	div.style.cssText = 'text-align: center;';
	div.innerHTML = '<font color=gray>[loading]</font>';
	div.setAttribute('onClick', '');
	xhr.send(null);			 
}

function get_xhr(){
	var xmlhttp;
	try { xmlhttp = new ActiveXObject("Msxml2.XMLHTTP") } catch (e) {
	try { xmlhttp = new ActiveXObject("Microsoft.XMLHTTP") } catch (E) { xmlhttp = false } }
	if (!xmlhttp && typeof XMLHttpRequest!='undefined') xmlhttp = new XMLHttpRequest();
	return xmlhttp;
}

function onLoad() {
	var torrents = document.getElementById('torrents');
	for (var i in torrents.rows) {
		var row = torrents.rows.item(i);
		if (i == 0) continue;
		
		var id = row.getAttribute('wt:id');
		var fc = row.getAttribute('wt:fc');
		var mr = row.getAttribute('wt:mr');
		var sp = row.getAttribute('wt:sp');
		
		var ic = row.cells.item(0);
		for (var j in ic.getElementsByTagName('a')) {
			var c = ic.getElementsByTagName('a').item(j);
			if (!c) continue;
			if (c.getAttribute('class') == 'delete') c.setAttribute('href', '/webtornado/delete/' + id);
			if (c.getAttribute('class') == 'green') c.setAttribute('href', '/webtornado/stop/' + id);
			if (c.getAttribute('class') == 'black') c.setAttribute('href', '/webtornado/start/' + id);
		}
		
		var nc = row.cells.item(1);
		nc.style.cssText = 'text-align: left';
		if ((fc > 1) && ! document.getElementById('files_' + id)) 
		    nc.innerHTML += ' <div id="files_' + id + '" class="fd" onClick="show_files(' + id + ')">[' + fc + ' files]</div>';
		
		var pc = row.cells.item(5);
		pc.id = 'peers_' + id;
		if (pc.innerHTML > 0) {
			if (sp > 0) {
				pc.setAttribute('onClick', 'hide_peers(' + id + ')');
			} else {
				pc.setAttribute('onClick', 'show_peers(' + id + ')');
			}
		}

		var rc = row.cells.item(6);
		rc.id = 'set_maxratio_' + id;
		rc.setAttribute('onClick', 'set_maxratio(' + id + ', ' + mr + ')');
		
		if (!(i % 2)) row.style.cssText = 'background-color: #eeeeee';
	}
}