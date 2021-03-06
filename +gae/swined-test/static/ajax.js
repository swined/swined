function xhr(url, onSuccess) {
  var xmlhttp;
  try {
    xmlhttp = new ActiveXObject("Msxml2.XMLHTTP")
  } catch (e) {
    try {
      xmlhttp = new ActiveXObject("Microsoft.XMLHTTP")
    } catch (E) {
      xmlhttp = false
    }
  }
  if (!xmlhttp && typeof XMLHttpRequest != 'undefined')
    xmlhttp = new XMLHttpRequest();
  xmlhttp.open('GET', url, true);
  xmlhttp.onreadystatechange = function() {
    if (xmlhttp.readyState != 4) { return }
    if (xmlhttp.status != 200) { return }
    onSuccess(xmlhttp);
  };
  xmlhttp.send(null);
}
