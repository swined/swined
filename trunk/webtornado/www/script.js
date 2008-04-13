function set_maxratio(id, maxratio) {
	div = document.getElementById("set_maxratio_" + id);
	div.innerHTML +=
		"<br><form method=get action='/webtornado/cgi-bin/action.pl'>" +
		"<input type=hidden name=action value=set_maxratio>" +
		"<input type=hidden name=id value=" + id + ">" +
		"<input type=text name=maxratio value='" + maxratio + "' style='width: 50px'>" +
		"<input type=submit style='width: 30px' value='OK'>" +
		"</form>";
	div.onClick = function() {};
	//document.getElementById("set_maxratio_" + id).id = "set_maxratio_form_" + id;
}

function show_files(id) {
	var div = document.getElementById("files_" + id);
	var xhr = get_xhr();
	xhr.open('GET', '/webtornado?files=' + id, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState != 4) { return }
		if (xhr.status != 200) { return }
		div.innerHTML = xhr.responseText;
	};
	div.innerHTML = '[loading]';
	div.onClick = function() {};
	xhr.send(null);			 
}

function get_xhr(){
	var xmlhttp;
	try {
    		xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
	} catch (e) {
	        try {
	        	xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		} catch (E) {
			xmlhttp = false;
		}
	}
	if (!xmlhttp && typeof XMLHttpRequest!='undefined') {
		xmlhttp = new XMLHttpRequest();
	}
	return xmlhttp;
}

function onLoad() {
}