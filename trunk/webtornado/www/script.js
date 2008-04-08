function set_maxratio(id, maxratio) {
	document.getElementById("set_maxratio_" + id).innerHTML +=
		"<br><form method=get action='/webtornado/cgi-bin/action.pl'>" +
		"<input type=hidden name=action value=set_maxratio>" +
		"<input type=hidden name=id value=" + id + ">" +
		"<input type=text name=maxratio value='" + maxratio + "' style='width: 50px'>" +
		"<input type=submit style='width: 30px' value='OK'>" +
		"</form>";
	document.getElementById("set_maxratio_" + id).id = "set_maxratio_form_" + id;
}
function show_files(id) {
//    document.getElementById("files_" + id).innerHTML = document.getElementById("files_" + id + "_content").innerHTML;
	document.getElementById('files_' + id + '_content').innerHTML = '<script src="/webtornado?files=' + id + '" language="javascript"></script>';
}